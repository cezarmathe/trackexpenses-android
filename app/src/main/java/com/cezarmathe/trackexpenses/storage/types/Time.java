package com.cezarmathe.trackexpenses.storage.types;

import android.support.annotation.Nullable;

import java.util.Calendar;

public class Time {

    public int hour;
    public int minute;

    public int day;
    public int month;
    public int year;


    public Time() {}

    public static Time newInstance() {
        Time storageTime = new Time();

        Calendar calendar = Calendar.getInstance();

        storageTime.hour    = calendar.get(Calendar.HOUR);
        storageTime.minute  = calendar.get(Calendar.MINUTE);

        storageTime.day     = calendar.get(Calendar.DAY_OF_MONTH);
        storageTime.month   = calendar.get(Calendar.MONTH);
        storageTime.year    = calendar.get(Calendar.YEAR);

        return storageTime;
    }

    public static Time newInstance(int hour, int minute, int day, int month, int year) {
        Time storageTime = new Time();

        storageTime.hour = hour;
        storageTime.minute = minute;
        storageTime.day = day;
        storageTime.month = month;
        storageTime.year = year;

        return storageTime;
    }

    @Override
    public String toString() {
        return hour     +
                "-"     +
                minute  +
                ":"     +
                day     +
                "-"     +
                month   +
                "-"     +
                year;
    }

    public Time parseString(@Nullable String string) {
        if (string == null) {
            return null;
        }
        String[] fields = string.split("[\\-:]");
        if (fields.length != 5) {
            return null;
        }
        Time time = Time.newInstance();
        time.hour = Integer.parseInt(fields[0]);
        time.minute = Integer.parseInt(fields[1]);
        time.day = Integer.parseInt(fields[2]);
        time.month = Integer.parseInt(fields[3]);
        time.year = Integer.parseInt(fields[4]);
        return time;
    }
}
