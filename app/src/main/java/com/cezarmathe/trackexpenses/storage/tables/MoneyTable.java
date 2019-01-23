package com.cezarmathe.trackexpenses.storage.tables;

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
import java.util.ArrayList;

public class MoneyTable {

    public static final String      TAG             = "MoneyTable";
    public static final String      FILE_NAME       = "money_table.csv";
    public static final String[]    NAME_MAPPING    = {"time", "operation", "amount", "currency", "notes"};

    private File                            file;
    private CsvBeanReader                   reader;
    private CsvBeanWriter                   writer;
    public  ArrayList<MoneyTableRow>    contents;

    public MoneyTable() {}

    public static MoneyTable newInstance(File dir) {
        Log.i(TAG, "new instance");
        MoneyTable table = new MoneyTable();
        table.file = new File(dir, FILE_NAME);
        if (!table.file.exists()) {
            Log.w(TAG, "table file " + FILE_NAME + " does not exist");
            try {
                if (!table.file.createNewFile()) {
                    Log.w(TAG, "could not create table file");
                    return null;
                }
            } catch (IOException e) {
                Log.e(TAG, e.toString());
            }
        }
        table.contents = new ArrayList<>();
        return table;
    }

    private void openReader() throws FileNotFoundException {
        this.reader = new CsvBeanReader(
                new BufferedReader(new FileReader(this.file)),
                CsvPreference.STANDARD_PREFERENCE
        );

    }

    private void closeReader() throws IOException {
        if (this.reader != null) {
            this.reader.close();
            this.reader = null;
        }
    }

    private void openWriter() throws IOException {
        this.writer = new CsvBeanWriter(
                new BufferedWriter(new FileWriter(this.file)),
                CsvPreference.STANDARD_PREFERENCE
        );
    }

    private void closeWriter() throws IOException {
        if (this.writer != null) {
            this.writer.close();
            this.writer = null;
        }
    }

    public void read() {
        try {
            openReader();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return;
        }

        try {
            MoneyTableRow bean;
            while ( (bean = reader.read(MoneyTableRow.class, NAME_MAPPING)) != null) {
                contents.add(bean);
            }
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

    public void write() {
        try {
            openWriter();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return;
        }

        try {
            for (final MoneyTableRow bean : contents) {
                writer.write(bean, NAME_MAPPING);
            }
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
}
