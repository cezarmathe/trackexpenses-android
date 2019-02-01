package com.cezarmathe.trackexpenses.config;

import android.support.annotation.NonNull;
import android.util.Log;

import com.cezarmathe.trackexpenses.storage.types.Tag;
import com.google.gson.Gson;
import com.google.gson.annotations.Expose;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class UserConfig {

    // TODO: 01/02/2019 move from Defaults to UserConfig

    /**
     * The user config file name
     */
    public static final transient String FILE_NAME = "user_configs.json";

    public static final transient String TAG = "UserConfig";

    /**
     * Fields
     */
    private ArrayList<Tag>   tags;
    private Integer             lastTagIndex;

    private ArrayList<String>   currencies;
    private Integer             lastCurrencyIndex;


    /**
     * POJO methods
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

    public ArrayList<String> getCurrencies() {
        return currencies;
    }

    public void setCurrencies(ArrayList<String> currencies) {
        this.currencies = currencies;
    }

    public Integer getLastCurrencyIndex() {
        return lastCurrencyIndex;
    }

    public void setLastCurrencyIndex(Integer lastCurrencyIndex) {
        this.lastCurrencyIndex = lastCurrencyIndex;
    }

    public UserConfig() {
        Log.d(TAG, "UserConfig() called");
        tags = new ArrayList<>();
        currencies = new ArrayList<>();
        lastCurrencyIndex = 0;
        lastTagIndex = 0;
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
}
