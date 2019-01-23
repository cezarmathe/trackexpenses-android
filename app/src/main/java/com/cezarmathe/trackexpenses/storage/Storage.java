package com.cezarmathe.trackexpenses.storage;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.cezarmathe.trackexpenses.storage.tables.MoneyTable;

import java.io.File;

public class Storage {

//    Constans
    public  static final String TAG                 = "Storage";
    private static final String TABLE_BASE_PATH     = "/tables";
    private static final String STORAGE_BASE_PATH   = "/com.cezarmathe.trackexpenses";

    //    Listener
    private OnStorageEventListener mListener;

//    Storage variables
    private File    storageDir;
    private File    tableDir;

//    Tables
    public MoneyTable moneyTable;

    public Storage() {}

    public static Storage newInstace(Context context) {
        Log.i(TAG, "new instance");
        Storage storage = new Storage();
        if (context instanceof OnStorageEventListener) {
            storage.mListener = (OnStorageEventListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnStorageEventListener");
        }

//        boolean is = storage.isExternalStorageReadable() && storage.isExternalStorageWritable();
//
//        if (is) {
//            Log.w(TAG, "external storage is not readable/writable");
//            return null;
//        }

        storage.storageDir = new File(Environment.getExternalStorageDirectory(), STORAGE_BASE_PATH);
        if (!storage.storageDir.exists()) {
            Log.w(TAG, "storage directory does not exist");
            if (!storage.storageDir.mkdirs()) {
                Log.w(TAG, "could not create storage directory");
                return null;
            }
        }
        storage.tableDir = new File(storage.storageDir, TABLE_BASE_PATH);
        if (!storage.tableDir.exists()) {
            Log.w(TAG, "table directory does not exist");
            if (!storage.tableDir.mkdirs()) {
                Log.w(TAG, "could not create table directory");
                return null;
            }
        }

        storage.moneyTable = MoneyTable.newInstance(storage.tableDir);

        return storage;
    }

    public interface OnStorageEventListener {
        void isExternalStorageWritable(boolean is);
        void isExternalStorageReadable(boolean is);
    }

    private boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        boolean is = Environment.MEDIA_MOUNTED.equals(state);
        mListener.isExternalStorageWritable(is);
        return is;
    }

    private boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        boolean is = Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
        mListener.isExternalStorageReadable(is);
        return is;
    }


}
