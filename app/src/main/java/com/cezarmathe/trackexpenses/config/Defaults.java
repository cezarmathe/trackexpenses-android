package com.cezarmathe.trackexpenses.config;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.util.Pair;

import com.cezarmathe.trackexpenses.R;
import com.cezarmathe.trackexpenses.storage.types.Operation;

import java.util.Currency;

public final class Defaults {

    public static final String TAG = "Defaults";
    private static final String PREFERENCES_NAME = "com.cezarmathe.trackexpenses.config.Defaults";

    private static Pair<String, Integer> makeArgPair(String a, Integer b) {
        return new Pair<>(a, b);
    }

    private static SharedPreferences getSharedPreferences(Activity activity) {
        return activity.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    // TODO: 01/02/2019 add proper args and string resources
    /**
     * Args
     */
//    General
    public static final Pair<String, Integer> ARG_TIME_ZONE = makeArgPair("time_zone", R.string.app_name);
    public static final Pair<String, Integer> ARG_LOCALE = makeArgPair("locale", R.string.app_name);

    //    Dashboard
    public static final Pair<String, Integer> ARG_DASHBOARD_MENU_ITEM = makeArgPair("dashboard_menu_item", R.string.app_name);

    //    QuickLog
    public static final Pair<String, Integer> ARG_QUICK_LOG_CURRENCY    = makeArgPair("quick_log_currency", R.string.quick_log_currency);
    public static final Pair<String, Integer> ARG_QUICK_LOG_AMOUNT      = makeArgPair("quick_log_amount", R.string.quick_log_amount);
    public static final Pair<String, Integer> ARG_QUICK_LOG_NOTES       = makeArgPair("quick_log_notes", R.string.quick_log_notes);
    public static final Pair<String, Integer> ARG_QUICK_LOG_OPERATION   = makeArgPair("quick_log_operation", R.string.quick_log_operation);

//    History


    /**
     * Default values
     */
////    General
//
//
//    Dashboard
    public static final int DASHBOARD_DEFAULT_MENU_ITEM = R.id.quicklog_bottom_navigation_item;
//
////    QuickLog
//    public static final Double DEFAULT_QUICK_LOG_AMOUNT         = (double) R.string.quick_log_amount;
//    public String final Currency DEFAULT_QUICK_LOG_CURRENCY     = Currency.getInstance(R.string.quick_log_currency);
//    public String QUICK_LOG_NOTES                               = "";
//    public String QUICK_LOG_OPERATION                           = Operation.SUBTRACTION.toSign();
//
////    History


    /**
     * Methods
     */
    @NonNull
    public static Boolean getBoolean(@NonNull Activity activity, @NonNull Pair<String, Integer> arg) {
        return getSharedPreferences(activity).getBoolean(
                arg.first,
                Boolean.valueOf(activity.getString(arg.second))
        );
    }

    @NonNull
    public static String getString(@NonNull Activity activity, @NonNull Pair<String, Integer> arg) {
        return getSharedPreferences(activity).getString(
                arg.first,
                activity.getString(arg.second)
        );
    }

    @NonNull
    public static Integer getInt(@NonNull Activity activity, @NonNull Pair<String, Integer> arg) {
        return getSharedPreferences(activity).getInt(
                arg.first,
                Integer.valueOf(activity.getString(arg.second))
        );
    }

    @NonNull
    public static Long getLong(@NonNull Activity activity, @NonNull Pair<String, Integer> arg) {
        return getSharedPreferences(activity).getLong(
                arg.first,
                Long.valueOf(activity.getString(arg.second))
        );
    }

    @NonNull
    public static Float getFloat(@NonNull Activity activity, @NonNull Pair<String, Integer> arg) {
        return getSharedPreferences(activity).getFloat(
                arg.first,
                Float.valueOf(activity.getString(arg.second))
        );
    }

    @NonNull
    public static Double getDouble(@NonNull Activity activity, @NonNull Pair<String, Integer> arg) {
        return Double.valueOf(getSharedPreferences(activity).getString(
                arg.first,
                activity.getString(arg.second)
        ));
    }
}
