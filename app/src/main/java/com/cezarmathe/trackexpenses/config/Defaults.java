package com.cezarmathe.trackexpenses.config;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.cezarmathe.trackexpenses.R;
import com.cezarmathe.trackexpenses.storage.types.Operation;

public class Defaults {

    public  static final String TAG                 = "Defaults";
    private static final String PREFERENCES_NAME    = "com.cezarmathe.trackexpenses.config.Defaults";

    private SharedPreferences preferences;

    public int DASHBOARD_DEFAULT_MENU_ITEM = R.id.quicklog_bottom_navigation_item;

    public Double QUICK_LOG_AMOUNT      = 0.00;
    public String QUICK_LOG_CURRENCY    = "RON";
    public String QUICK_LOG_NOTES       = "";
    public String QUICK_LOG_OPERATION   = Operation.SUBTRACTION.toSign();

    private Defaults(){}

    public static Defaults newInstance(Activity activity) {

        Defaults defaults = new Defaults();

        defaults.preferences = activity.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);

        return defaults;
    }
}
