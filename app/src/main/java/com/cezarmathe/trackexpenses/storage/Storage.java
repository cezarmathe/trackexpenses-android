package com.cezarmathe.trackexpenses.storage;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.cezarmathe.trackexpenses.storage.tables.MoneyTable;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Storage {

//    Constans
    public  static final String TAG                 = "Storage";
    private static final String TABLE_BASE_PATH     = "/tables";
    private static final String STORAGE_BASE_PATH   = "/com.cezarmathe.trackexpenses";

    //    Listener
    private Table.TableEventHook mHook;

//    Storage variables
    private File    storageDir;
    private File    tableDir;

//    Tables
    public MoneyTable moneyTable;

    public Storage() {}

    public static Storage newInstance(Context context) {
        Log.d(TAG, "newInstance() called with: context = [" + context + "]");
        Storage storage = new Storage();

        if (context instanceof Table.TableEventHook) {
            storage.mHook = (Table.TableEventHook) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnStorageEventListener");
        }

        storage.storageDir = new File(Environment.getExternalStorageDirectory(), STORAGE_BASE_PATH);
        if (!storage.storageDir.exists()) {
            Log.i(TAG, "newInstance: storage directory does not exist");
            if (!storage.storageDir.mkdirs()) {
                Log.w(TAG, "newInstance: could not create storage directory");
                return null;
            }
            Log.i(TAG, "newInstance: created storage directory");
        }

        storage.tableDir = new File(storage.storageDir, TABLE_BASE_PATH);
        if (!storage.tableDir.exists()) {
            Log.i(TAG, "newInstance: table directory does not exist");
            if (!storage.tableDir.mkdirs()) {
                Log.w(TAG, "newInstance: could not create table directory");
                return null;
            }
            Log.i(TAG, "newInstance: created table directory");
        }

        Log.i(TAG, "newInstance: initializing tables");
        storage.moneyTable = new MoneyTable(
                storage.tableDir,
                storage.mHook
        );

        Log.i(TAG, "newInstance: reading tables from phone storage");
        storage.moneyTable.read();

        Log.i(TAG, "newInstance: created Storage");
        Log.d(TAG, "newInstance() returned: " + storage);
        return storage;
    }

    public File getFile(String filename) {
        return new File(storageDir, filename);
    }
}
