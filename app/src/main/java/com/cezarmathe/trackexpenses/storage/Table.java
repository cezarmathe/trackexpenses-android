package com.cezarmathe.trackexpenses.storage;

import android.support.annotation.Nullable;
import android.util.Log;

import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Cezar Mathe <cearmathe @ gmail.com>
 * @version 0.1
 * @see super-csv.github.io/super-csv/index.html
 */
public abstract class Table<T> {

    /**
     * Provides hooks that are triggered by events related to tables.
     */
    public interface TableEventHook {
        /**
         * Triggers when the table file fails to be created.
         */
        void onTableFileCreationFail(String tag, Exception e);

        /**
         * Triggers when the reader fails to read the CSV file.
         */
        void onReadFail(String tag, Exception e);

        /**
         * Triggers when the reader successfully reads the CSV file.
         */
        void onReadSuccess(String tag);

        /**
         * Triggers when the writer fails to write
         */
        void onWriteFail(String tag, Exception e);

        /**
         * Triggers when the writer successfully writes the CSV file.
         */
        void onWriteSuccess(String tag);


        /**
         * Triggers when the reader fails to open or close
         */
        void onReaderMethodFail(String tag, Exception e);

        /**
         * Triggers when the writer fails to open or close
         */
        void onWriterMethodFail(String tag, Exception e);
    }


    /**
     * Variable used for logging.
     */
    public      final String            TAG;

    /**
     * Holds the file name for this table.
     */
    protected   final String            FILE_NAME;

    /**
     * Container for field naming, must be identical to the bean used as row.
     */
    protected   final String[]          NAME_MAPPING;

    /**
     * Container for reading cell processors
     */
    protected   final CellProcessor[]   READ_PROCESSORS;

    /**
     * Container for writing cell processors
     */
    protected   final CellProcessor[]   WRITE_PROCESSORS;

    protected   final Class<T>          TYPE;

    /**
     * Constructs a new Table.
     * @param tag tag used for logging.
     * @param fileName the file name of the table.
     * @param nameMapping name mapping for reading and writing
     * @param parentFolder the parent folder of this table.
     * @param list the TYPE of list used for keeping the contents
     * @param hook hook for notifying table events
     * @param readingProcessors
     * @param writingProcessors
     */
    public Table(String tag,
                 String fileName,
                 String[] nameMapping,
                 File parentFolder,
                 ArrayList<T> list,
                 TableEventHook hook,
                 CellProcessor[] readingProcessors,
                 CellProcessor[] writingProcessors,
                 Class<T> type) {

        this.TAG = tag;

        Log.d(TAG, "Table() called with: tag = [" + tag + "], fileName = [" + fileName + "], nameMapping = [" + Arrays.toString(nameMapping) + "], parentFolder = [" + parentFolder + "], list = [" + list + "], hook = [" + hook + "], readingProcessors = [" + Arrays.toString(readingProcessors) + "], writingProcessors = [" + Arrays.toString(writingProcessors) + "]");

        this.FILE_NAME = fileName;
        this.NAME_MAPPING = nameMapping;

        this.tableFile = new File(parentFolder, FILE_NAME);
        if (!tableFile.exists()) {
            Log.w(TAG, "table file " + FILE_NAME +" does not exist, attempting to create one");
            try {
                if (tableFile.createNewFile()) {
                    Log.i(TAG, "table file " + FILE_NAME + " created successfully");
                }
            } catch (IOException e) {
                Log.e(TAG, "table file " + FILE_NAME + " could not be created, error " + e.toString());
                hook.onTableFileCreationFail(TAG, e);
            }
        }

        this.contents = list;
        this.hook = hook;
        this.READ_PROCESSORS = readingProcessors;
        this.WRITE_PROCESSORS = writingProcessors;
        this.TYPE = type;

        this.internalLock = new Object();
        this.isLocked = false;
    }


    /**
     * File object representing the actual file on the file system.
     */
    private File            tableFile;

    /**
     * CSV reader
     * @see super-csv.github.io/super-csv/apidocs/org/supercsv/io/CsvBeanReader.html
     */
    private CsvBeanReader   reader;

    /**
     * CSV writer
     * @see super-csv.github.io/super-csv/apidocs/org/supercsv/io/CsvBeanWriter.html
     */
    private CsvBeanWriter   writer;

    /**
     * Container for table contents.
     */
    private ArrayList<T>    contents;

    /**
     * Hook for notifying table events.
     */
    private TableEventHook  hook;

    /**
     * Internal thread locks
     */
    private final Object                      internalLock;
    private volatile Boolean            isLocked;



    /**
     * Opens the reader.
     * @throws FileNotFoundException
     */
    private void openReader() throws FileNotFoundException {
        Log.d(TAG, "openReader() called");
        this.reader = new CsvBeanReader(
                new BufferedReader(new FileReader(this.tableFile)),
                CsvPreference.STANDARD_PREFERENCE
        );
    }

    /**
     * Closes the reader.
     * @throws IOException
     */
    private void closeReader() throws IOException {
        Log.d(TAG, "closeReader() called");
        if (this.reader != null) {
            this.reader.close();
            this.reader = null;
        }
    }


    /**
     * Opens the writer.
     * @throws IOException
     */
    private void openWriter() throws IOException {
        Log.d(TAG, "openWriter() called");
        this.writer = new CsvBeanWriter(
                new BufferedWriter(new FileWriter(this.tableFile)),
                CsvPreference.STANDARD_PREFERENCE
        );
    }

    /**
     * Closes the writer.
     * @throws IOException
     */
    private void closeWriter() throws IOException {
        Log.d(TAG, "closeWriter() called");
        if (this.writer != null) {
            this.writer.close();
            this.writer = null;
        }
    }



    /**
     * Reads the entire CSV file into the contents table.
     */
    public final void read() {
        Log.d(TAG, "read() called");
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (isLocked) {
                    try {
                        synchronized (internalLock) {
                            internalLock.wait();
                        }
                    } catch (InterruptedException e) {
                        Log.e(TAG, "run: ", e);
                        return;
                    }
                }
                isLocked = true;
                try {
                    openReader();
                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                    isLocked = false;
                    synchronized (internalLock) {
                        internalLock.notify();
                    }
                    hook.onReaderMethodFail(TAG, e);
                    return;
                }
                try {
                    readMiddleWare();
                    hook.onReadSuccess(TAG);
                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                    hook.onReadFail(TAG, e);
                } finally {
                    try {
                        closeReader();
                    } catch (IOException e) {
                        Log.e(TAG, e.toString());
                        hook.onReaderMethodFail(TAG, e);
                    } finally {
                        isLocked = false;
                        synchronized (internalLock) {
                            internalLock.notify();
                        }
                    }
                }
            }
        }).start();
    }

    /**
     * Read a bean
     * @return the bean
     * @throws IOException
     */
    @Nullable
    private T readBean() throws IOException {
        return reader.read(TYPE, NAME_MAPPING, READ_PROCESSORS);
    }

    /**
     * Middleware for reading.
     * @throws IOException
     */
    private void readMiddleWare() throws IOException {
        Log.d(TAG, "readMiddleWare() called");
        T bean = readBean();
        while (bean != null) {
            contents.add(bean);
            Log.d(TAG, "readMiddleWare: read " + bean);
            bean = readBean();
        }
        Log.d(TAG, "readMiddleWare() returned");
    }



    /**
     * Writes the entire table into the CSV file.
     */
    public final Boolean write() {
        Log.d(TAG, "write() called");
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (isLocked) {
                    try {
                        synchronized (internalLock) {
                            internalLock.wait();
                        }
                    } catch (InterruptedException e) {
                        Log.e(TAG, "run: ", e);
                        return;
                    }
                }
                try {
                    openWriter();
                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                    synchronized (internalLock) {
                        internalLock.notify();
                    }
                    hook.onWriterMethodFail(TAG, e);
//                    return false;
                }
                try {
                    writeMiddleWare();
                    hook.onWriteSuccess(TAG);
                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                    hook.onWriteFail(TAG, e);
//                    return false;
                } finally {
                    try {
                        closeWriter();
                    } catch (IOException e) {
                        Log.e(TAG, e.toString());
                        hook.onWriterMethodFail(TAG, e);
                    } finally {
                        synchronized (internalLock) {
                            internalLock.notify();
                        }
                    }
                }
            }
        }).start();
        return true;
    }

    /**
     * Write a bean
     * @param bean
     * @throws IOException
     */
    private void writeBean(T bean) throws IOException {
        writer.write(bean, NAME_MAPPING, WRITE_PROCESSORS);
    }

    /**
     * Middleware for writing
     * @throws IOException
     */
    private void writeMiddleWare() throws IOException {
        Log.d(TAG, "writeMiddleWare() called");
        for (T bean : contents) {
            writeBean(bean);
            Log.d(TAG, "writeMiddleWare: wrote " + bean);
        }
        Log.d(TAG, "writeMiddleWare() returned");
    }



//    /**
//     * Rewrite the entire table file.
//     */
//    public final Boolean rewrite() {
//        Log.d(TAG, "rewrite() called");
//
//        if (!tableFile.delete()) {
//            Log.e(TAG, "rewrite: failed to delete table file");
//            return false;
//        }
//        try {
//            if (!tableFile.createNewFile()) {
//                Log.e(TAG, "rewrite: failed to recreate table file");
//                return false;
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//            return false;
//        }
//        return write();
//    }

    /**
     * Returns the size of the table.
     * @return
     */
    public final int getSize() {
        Log.d(TAG, "getSize() called");
        return contents.size();
    }

    /**
     * Checks if the given index is valid or not.
     * @param index the index that needs to be checked
     * @return
     */
    protected final boolean checkIfIndexIsValid(int index) {
        Log.d(TAG, "checkIfIndexIsValid() called with: index = [" + index + "]");
        return index >= 0 && index < getSize();
    }

    /**
     * Retrieves an object from the contents list.
     * @param index the index from the contents table
     * @return the object
     */
    public final T get(int index) {
        return contents.get(index);
    }

    /**
     * Retrieves the list of rows
     * @return the contents
     */
    public final ArrayList<T> get() {
        return contents;
    }

    /**
     * Adds a bean to the table.
     * @param bean the bean that needs to be added
     */
    public final void add(T bean) {
        Log.d(TAG, "add() called with: bean = [" + bean + "]");
        contents.add(bean);
        if (!write())
            contents.remove(contents.lastIndexOf(bean));
    }

    /**
     * Removes a bean from the table.
     * @param index the index of the bean
     */
    public final Boolean remove(int index) {
        Log.d(TAG, "remove() called with: index = [" + index + "]");
        if (checkIfIndexIsValid(index)) {
            contents.remove(index);
        }
        return write();
    }

    /**
     * Edits a bean from the table.
     * @param bean the new bean attributes
     * @param index the index of the bean
     */
    public Boolean edit(T bean, int index) {
        Log.d(TAG, "edit() called with: bean = [" + bean + "], index = [" + index + "]");
        if (checkIfIndexIsValid(index)) {
            contents.set(index, bean);
        }
        return write();
    }

}
