package com.cezarmathe.trackexpenses.storage;

import android.util.Log;

import com.cezarmathe.trackexpenses.storage.models.MoneyTableRow;

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
import java.util.List;

/**
 * @author Cezar Mathe <cearmathe @ gmail.com>
 * @version 0.1
 * @see super-csv.github.io/super-csv/index.html
 */
public abstract class Table {

    /**
     * Provides hooks that are triggered by events related to tables.
     */
    public interface TableEventHook {
        /**
         * Triggers when the table file fails to be created.
         */
        void onTableFileCreationFail();

        /**
         * Triggers when the reader fails to read the CSV file.
         */
        void onReadFail();

        /**
         * Triggers when the reader successfully reads the CSV file.
         */
        void onReadSuccess();
    }

    /**
     * Variable used for logging.
     */
    public      final String    TAG;

    /**
     * Holds the file name for this table.
     */
    public      final String    FILE_NAME;

    /**
     * Container for field naming, must be identical to the bean used as row.
     */
    protected   final String[]  NAME_MAPPING;


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
                 TableEventHook hook) {

        this.TAG = tag;
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
            }
        }

        this.contents = list;
        this.hook = hook;
    }

    protected Type beanType;

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
    public      ArrayList<?>    contents;

    /**
     * Hook for notifying table events.
     */
    protected   TableEventHook  hook;

    /**
     * Opens the reader.
     * @throws FileNotFoundException
     */
    protected void openReader() throws FileNotFoundException {
        this.reader = new CsvBeanReader(
                new BufferedReader(new FileReader(this.tableFile)),
                CsvPreference.STANDARD_PREFERENCE
        );
    }

    /**
     * Closes the reader.
     * @throws IOException
     */
    protected void closeReader() throws IOException {
        if (this.reader != null) {
            this.reader.close();
            this.reader = null;
        }
    }

    /**
     * Opens the writer.
     * @throws IOException
     */
    protected void openWriter() throws IOException {
        this.writer = new CsvBeanWriter(
                new BufferedWriter(new FileWriter(this.tableFile)),
                CsvPreference.STANDARD_PREFERENCE
        );
    }

    /**
     * Closes the writer.
     * @throws IOException
     */
    protected void closeWriter() throws IOException {
        if (this.writer != null) {
            this.writer.close();
            this.writer = null;
        }
    }

    /**
     * Reads the entire CSV file into the contents table.
     */
    protected void read() {
        try {
            openReader();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return;
        }
        try {
            contents = readMiddleWare();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        } finally {
            try {
                closeReader();
            } catch (IOException e) {
                Log.e(TAG, e.toString());
            }
        }
    }

    /**
     * Writes the entire table into the CSV file.
     */
    protected void write() {
        try {
            openWriter();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return;
        }
        try {
            writeMiddleWare(contents);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        } finally {
            try {
                closeWriter();
            } catch (IOException e) {
                Log.e(TAG, e.toString());
            }
        }
    }

    /**
     * Middleware for reading.
     * @throws IOException
     */
    protected abstract ArrayList readMiddleWare() throws IOException;

    /**
     * Middlware for writing.
     * @param beans the beans that need to be written
     * @throws IOException
     */
    protected abstract void writeMiddleWare(ArrayList beans) throws IOException;

    /**
     * Adds a bean to the table.
     * @param bean the bean that needs to be added
     * @param <T> the bean type
     */
    public abstract <T extends Object> void add(T bean);

    /**
     * Removes a bean from the table.
     * @param index the index of the bean
     */
    public abstract void remove(int index);

    /**
     * Edits a bean from the table.
     * @param bean the new bean attributes
     * @param index the index of the bean
     * @param <T> the bean type
     */
    public abstract <T extends Object> void edit(T bean, int index);

}
