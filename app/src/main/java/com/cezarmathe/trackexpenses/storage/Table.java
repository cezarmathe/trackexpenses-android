package com.cezarmathe.trackexpenses.storage;

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
import java.lang.reflect.Type;
import java.util.ArrayList;

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
    public      final String    TAG;

    /**
     * Holds the file name for this table.
     */
    protected   final String    FILE_NAME;

    /**
     * Container for field naming, must be identical to the bean used as row.
     */
    protected   final String[]  NAME_MAPPING;

    /**
     * Container for cell processors
     */
    protected   final CellProcessor[] PROCESSORS;


    /**
     * Constructs a new Table.
     * @param tag tag used for logging.
     * @param fileName the file name of the table.
     * @param nameMapping name mapping for reading and writing
     * @param parentFolder the parent folder of this table.
     * @param list the type of list used for keeping the contents
     * @param hook hook for notifying table events
     */
    public Table(String tag,
                 String fileName,
                 String[] nameMapping,
                 File parentFolder,
                 ArrayList list,
                 TableEventHook hook,
                 CellProcessor[] processors) {

        this.TAG = tag;

        Log.d(TAG, "Table() called with: tag = [" + tag + "], fileName = [" + fileName + "], nameMapping = [" + nameMapping + "], parentFolder = [" + parentFolder + "], list = [" + list + "], hook = [" + hook + "], processors = [" + processors + "]");

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
        this.PROCESSORS = processors;
    }


    /**
     * File object representing the actual file on the file system.
     */
    protected   File            tableFile;

    /**
     * CSV reader
     * @see super-csv.github.io/super-csv/apidocs/org/supercsv/io/CsvBeanReader.html
     */
    protected   CsvBeanReader   reader;

    /**
     * CSV writer
     * @see super-csv.github.io/super-csv/apidocs/org/supercsv/io/CsvBeanWriter.html
     */
    protected   CsvBeanWriter   writer;

    /**
     * Container for table contents.
     */
    protected   ArrayList<T>    contents;

    /**
     * Hook for notifying table events.
     */
    protected   TableEventHook  hook;


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
    public void read() {
        Log.d(TAG, "read() called");
        try {
            openReader();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            hook.onReaderMethodFail(TAG, e);
            return;
        }
        try {
            contents = readMiddleWare();
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
            }
        }
    }

    /**
     * Writes the entire table into the CSV file.
     */
    public void write() {
        // TODO: 24/01/2019 rewrite the entire file
        // TODO: 24/01/2019 add method for partial writing from the file
        Log.d(TAG, "write() called");
        try {
            openWriter();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            hook.onWriterMethodFail(TAG, e);
            return;
        }
        try {
            writeMiddleWare(contents);
            hook.onWriteSuccess(TAG);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            hook.onWriteFail(TAG, e);
        } finally {
            try {
                closeWriter();
            } catch (IOException e) {
                Log.e(TAG, e.toString());
                hook.onWriterMethodFail(TAG, e);
            }
        }
    }

    public void write(int begin, int end) {
        Log.d(TAG, "write() called with: begin = [" + begin + "], end = [" + end + "]");
    }

    public void write(int index, boolean fromZero) {
        Log.d(TAG, "write() called with: index = [" + index + "], fromZero = [" + fromZero + "]");
    }


    /**
     * Returns the size of the table.
     * @return
     */
    public int getSize() {
        Log.d(TAG, "getSize() called");
        return contents.size();
    }

    /**
     * Checks if the given index is valid or not.
     * @param index the index that needs to be checked
     * @return
     */
    protected boolean checkIfIndexIsValid(int index) {
        Log.d(TAG, "checkIfIndexIsValid() called with: index = [" + index + "]");
        return index >= 0 && index < getSize();
    }

    /**
     * Retrieves an object from the contents list.
     * @param index the index from the contents table
     * @return the object
     */
    public T get(int index) {
        return contents.get(index);
    }

    /**
     * Retrieves the list of rows
     * @return the contents
     */
    public ArrayList<T> get() {
        return contents;
    }


    /**
     * Middleware for reading.
     * @throws IOException
     */
    protected abstract ArrayList<T> readMiddleWare() throws IOException;

    /**
     * Middlware for writing.
     * @param beans the beans that need to be written
     * @throws IOException
     */
    protected abstract void writeMiddleWare(ArrayList<T> beans) throws IOException;

    /**
     * Adds a bean to the table.
     * @param bean the bean that needs to be added
     */
    public abstract void add(T bean);

    /**
     * Removes a bean from the table.
     * @param index the index of the bean
     */
    public abstract void remove(int index);

    /**
     * Edits a bean from the table.
     * @param bean the new bean attributes
     * @param index the index of the bean
     */
    public abstract void edit(T bean, int index);

}
