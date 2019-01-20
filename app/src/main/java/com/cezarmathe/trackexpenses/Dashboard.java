package com.cezarmathe.trackexpenses;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.util.Log;
import android.view.MenuItem;

import com.cezarmathe.trackexpenses.config.Defaults;
import com.cezarmathe.trackexpenses.fragments.QuickLogFragment;
import com.cezarmathe.trackexpenses.storage.Operation;
import com.cezarmathe.trackexpenses.storage.Storage;
import com.cezarmathe.trackexpenses.storage.StorageDate;
import com.cezarmathe.trackexpenses.storage.StorageTableRow;

import java.util.Currency;

public class Dashboard extends Activity implements QuickLogFragment.OnFragmentInteractionListener {

//    Activity variables
    public  static final String TAG                 = "Dashboard";
    private static final String ARG_NAVIGATION_ITEM = "navigation_item_id";

    private BottomNavigationView navigationView;
    private int activeMenuItem;
    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch(item.getItemId()) {
                case R.id.quicklog_bottom_navigation_item:
                    activeMenuItem = item.getItemId();
                    if (quickLogFragment.isVisible()) {
                        Log.i(TAG, "already loaded " + QuickLogFragment.TAG);
                        return true;
                    }
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.add(R.id.fragment_container, quickLogFragment);
                    ft.commit();
                    Log.i(TAG, "loaded " + QuickLogFragment.TAG);
                    return true;
                case R.id.history_bottom_navigation_item:
                    activeMenuItem = item.getItemId();
                    return true;
                case R.id.statistics_bottom_navigation_item:
                    activeMenuItem = item.getItemId();
                    return true;
                default:
                    navigationView.setSelectedItemId(defaults.DASHBOARD_DEFAULT_MENU_ITEM);
                    return true;
            }
        }
    };
//    --------------------

//    Storage variables
    private Storage storage;
    private Defaults        defaults;
//    --------------------

//    Quick log fragment variables
    private QuickLogFragment quickLogFragment;

    private static final String ARG_QUICK_LOG_AMOUNT    = "quick_log_amount";
    private static final String ARG_QUICK_LOG_CURRENCY  = "quick_log_currency";
    private static final String ARG_QUICK_LOG_DATE      = "quick_log_date";
    private static final String ARG_QUICK_LOG_NOTES     = "quick_log_notes";
    private static final String ARG_QUICK_LOG_OPERATION = "quick_log_operation";

    private Double      quickLogAmount;
    private Currency    quickLogCurrency;
    private StorageDate quickLogDate;
    private String      quickLogNotes;
    private Operation   quickLogOperation;
//    --------------------

//    Activity methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Log.i(TAG, "load defaults");
        defaults = Defaults.newInstance(this);

        Log.i(TAG, "initialize view objects");
        navigationView = findViewById(R.id.bottom_navigation_view);
        navigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);

        Log.i(TAG, "trying to load saved instance state");
        if (savedInstanceState != null) {
            Log.i(TAG, "saved instance state is available");
            activeMenuItem      =                       savedInstanceState.getInt   (ARG_NAVIGATION_ITEM,       defaults.DASHBOARD_DEFAULT_MENU_ITEM);
            quickLogAmount      =                       savedInstanceState.getDouble(ARG_QUICK_LOG_AMOUNT,      defaults.QUICK_LOG_AMOUNT           );
            quickLogCurrency    = Currency.getInstance( savedInstanceState.getString(ARG_QUICK_LOG_CURRENCY,    defaults.QUICK_LOG_CURRENCY         ));
            quickLogNotes       =                       savedInstanceState.getString(ARG_QUICK_LOG_NOTES,       defaults.QUICK_LOG_NOTES);
            quickLogOperation   = Operation.fromString( savedInstanceState.getString(ARG_QUICK_LOG_OPERATION,   defaults.QUICK_LOG_OPERATION        ));
            quickLogFragment    = QuickLogFragment.newInstance(
                    quickLogAmount,
                    quickLogCurrency,
                    quickLogNotes,
                    quickLogOperation
            );
        } else {
            Log.i(TAG, "no saved instance state is available");
            activeMenuItem      = defaults.DASHBOARD_DEFAULT_MENU_ITEM;
            quickLogFragment    = QuickLogFragment.newInstance(
                                            defaults.QUICK_LOG_AMOUNT,
                    Currency.getInstance(   defaults.QUICK_LOG_CURRENCY ),
                                            defaults.QUICK_LOG_NOTES,
                    Operation.fromString(   defaults.QUICK_LOG_OPERATION)
            );
        }

//        Log.i(TAG, "loading ui fragment");
        navigationView.setSelectedItemId(activeMenuItem);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(TAG, "saving instance state");

        outState.putInt(ARG_NAVIGATION_ITEM, activeMenuItem);

        if (quickLogAmount != null) {
            outState.putDouble(ARG_QUICK_LOG_AMOUNT, quickLogAmount);
        }
        if (quickLogOperation != null) {
            outState.putString(ARG_QUICK_LOG_OPERATION, quickLogOperation.toSign());
        }
        if (quickLogNotes != null) {
            if (!quickLogNotes.equals("")) {
                outState.putString(ARG_QUICK_LOG_NOTES, quickLogNotes);
            }
        }
        if (quickLogDate != null) {
            outState.putString(ARG_QUICK_LOG_DATE, quickLogDate.toString());
        }
        if (quickLogCurrency != null) {
            outState.putString(ARG_QUICK_LOG_CURRENCY, quickLogCurrency.toString());
        }

        Log.i(TAG, "saved instance state");
    }

//    --------------------

//    Quick log fragment methods
    @Override
    public void onLogButtonPressed(StorageTableRow dataRow) {

    }

    @Override
    public Currency onCurrencyButtonPressed() {
        quickLogCurrency = Currency.getInstance("RON");
        return quickLogCurrency;
    }

    @Override
    public Currency onCurrencyButtonLongPressed() {
        quickLogCurrency = Currency.getInstance("RON");
        return quickLogCurrency;
    }

    @Override
    public void onDateButtonPressed(boolean save, StorageDate date) {
        if (save) {
            quickLogDate = date;
            Log.i(TAG, "saved " + QuickLogFragment.TAG + " date " + date.toString());
        } else {
            quickLogDate = null;
            Log.i(TAG, "unsaved " + QuickLogFragment.TAG + " date");
        }
    }

    @Override
    public void onNotesButtonPressed(boolean save, String notes) {
        if (save) {
            quickLogNotes = notes;
            Log.i(TAG, "saved " + QuickLogFragment.TAG + " notes " + notes);
        } else {
            quickLogNotes = "";
            Log.i(TAG, "unsaved " + QuickLogFragment.TAG + " notes");
        }
    }

    @Override
    public void onOperationButtonPressed(Operation operation) {
        quickLogOperation = operation;
        Log.i(TAG, "saved " + QuickLogFragment.TAG + " operation " + operation.toString());
    }
    //    --------------------

}
