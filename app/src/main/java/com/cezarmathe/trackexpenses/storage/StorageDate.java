package com.cezarmathe.trackexpenses.storage;

import java.util.Calendar;

public class StorageDate {

    public int hour;
    public int minute;

    public int day;
    public int month;
    public int year;


    public StorageDate() {}

    public static StorageDate newInstance() {
        StorageDate storageDate = new StorageDate();

        Calendar calendar = Calendar.getInstance();

        storageDate.hour = calendar.get(Calendar.HOUR);
        storageDate.minute = calendar.get(Calendar.MINUTE);

        storageDate.day = calendar.get(Calendar.DAY_OF_MONTH);
        storageDate.month = calendar.get(Calendar.MONTH);
        storageDate.year = calendar.get(Calendar.YEAR);

        return storageDate;
    }

    public static StorageDate newInstance(int hour, int minute, int day, int month, int year) {
        StorageDate storageDate = new StorageDate();

        storageDate.hour = hour;
        storageDate.minute = minute;
        storageDate.day = day;
        storageDate.month = month;
        storageDate.year = year;

        return storageDate;
    }
}
