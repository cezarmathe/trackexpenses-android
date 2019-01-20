package com.cezarmathe.trackexpenses.storage;

import android.content.Context;
import android.os.Environment;

import java.io.File;

public class Storage {

//    Constans
    public static final String TAG = "Storage";

//    Storage variables
    public StorageTableRow[] table;

//    Listener
    private OnStorageEventListener mListener;

    public Storage() {}

    public static Storage newInstace(Context context) {
        Storage storage = new Storage();
        if (context instanceof OnStorageEventListener) {
            storage.mListener = (OnStorageEventListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnStorageEventListener");
        }

//        File file = Environment.getExternalStoragePublicDirectory(Environment.getExternalStorageState());

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
