package com.cezarmathe.trackexpenses.config;

import android.graphics.Path;
import android.support.annotation.NonNull;
import android.util.Log;

import com.cezarmathe.trackexpenses.storage.types.Operation;
import com.cezarmathe.trackexpenses.storage.types.Tag;
import com.google.gson.Gson;

import org.joda.time.DateTime;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Locale;

public class UserConfig {

    /**
     * The user config file name
     */
    public static final transient String FILE_NAME = "user_configs.json";

    public static final transient String TAG = "UserConfig";

    /**
     * Fields
     */

//    General
    private Locale locale;
    public static final transient Locale DEFAULT_LOCALE = Locale.US;

//    QuickLog
    private ArrayList<Tag>  tags;
    private Integer         lastTagIndex;
    public static final transient Integer   DEFAULT_COLOR = 0;
    public static final transient Tag       DEFAULT_TAG = new Tag("-", DEFAULT_COLOR);

    public static final transient Operation DEFAULT_OPERATION = Operation.SUBTRACTION;

    private ArrayList<Currency> currencies;
    private Integer             lastCurrencyIndex;
    public static final transient Currency DEFAULT_CURRENCY = Currency.getInstance(DEFAULT_LOCALE);

    private DateTime    savedDateTime;
    private String      savedNotes;



    public UserConfig() {
        Log.d(TAG, "UserConfig() called");
        tags = new ArrayList<>();
        lastTagIndex = 0;
        currencies = new ArrayList<>();
        lastCurrencyIndex = 0;
        savedDateTime = DateTime.now();
        savedNotes = "";
    }

    /**
     * Serializing and deserializing
     */
    public static void write(@NonNull UserConfig config, File file) {
        Log.d(TAG, "write() called with: config = [" + config + "], file = [" + file + "]");
        Gson gson = new Gson();
        if (!file.exists()) {
            Log.w(TAG, "write: config file does not exist");
            try {
                if (!file.createNewFile()) {
                    Log.w(TAG, "write: failed to create config file");
                }
                Log.i(TAG, "write: created new config file");
            } catch (IOException e) {
                Log.e(TAG, "write: ", e);
                return;
            }
        }
        try {
            FileWriter fw = new FileWriter(file);
            fw.write(gson.toJson(config));
        } catch (IOException e) {
            Log.e(TAG, "write: ", e);
        }
    }

    @NonNull
    public static UserConfig read(File file) {
        Log.d(TAG, "read() called with: file = [" + file + "]");
        UserConfig userConfig = new UserConfig();
        if (!file.exists()) {
            Log.w(TAG, "read: config file does not exist");
            Log.d(TAG, "read() returned: " + userConfig);
            return userConfig;
        }

        Gson gson = new Gson();
        try {
            userConfig = gson.fromJson(
                    new FileReader(file),
                    UserConfig.class
            );
            if (userConfig == null) {
                userConfig = new UserConfig();
                Log.d(TAG, "read() returned: " + userConfig);
                return new UserConfig();
            }
            Log.d(TAG, "read() returned: " + userConfig);
            return userConfig;
        } catch (FileNotFoundException e) {
            Log.e(TAG, "read: ", e);
            Log.d(TAG, "read() returned: " + userConfig);
            return userConfig;
        }
    }

    /**
     * Methods
     */

    public ArrayList<Tag> getTags() {
        return tags;
    }

    public void setTags(ArrayList<Tag> tags) {
        this.tags = tags;
    }

    public Integer getLastTagIndex() {
        return lastTagIndex;
    }

    public void setLastTagIndex(Integer lastTagIndex) {
        this.lastTagIndex = lastTagIndex;
    }

    public ArrayList<Currency> getCurrencies() {
        return currencies;
    }

    public void setCurrencies(ArrayList<Currency> currencies) {
        this.currencies = currencies;
    }

    public Integer getLastCurrencyIndex() {
        return lastCurrencyIndex;
    }

    public void setLastCurrencyIndex(Integer lastCurrencyIndex) {
        this.lastCurrencyIndex = lastCurrencyIndex;
    }

    public DateTime getSavedDateTime() {
        return savedDateTime;
    }

    public void setSavedDateTime(DateTime savedDateTime) {
        this.savedDateTime = savedDateTime;
    }

    public String getSavedNotes() {
        return savedNotes;
    }

    public void setSavedNotes(String savedNotes) {
        this.savedNotes = savedNotes;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }
}
