package com.cezarmathe.trackexpenses;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.cezarmathe.trackexpenses.config.Defaults;
import com.cezarmathe.trackexpenses.fragments.QuickLogFragment;
import com.cezarmathe.trackexpenses.storage.Storage;
import com.cezarmathe.trackexpenses.storage.types.Operation;
import com.cezarmathe.trackexpenses.storage.types.Time;
import com.cezarmathe.trackexpenses.storage.models.MoneyTableRow;

import java.util.Currency;

public class Dashboard extends Activity implements QuickLogFragment.OnFragmentInteractionListener, Storage.OnStorageEventListener {

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
    private Storage     storage;
    private Defaults    defaults;
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
    private Time quickLogTime;
    private String      quickLogNotes;
    private Operation quickLogOperation;
//    --------------------

//    Activity methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Log.i(TAG, "load defaults");
        defaults = Defaults.newInstance(this);

        Log.i(TAG, "initialize storage");
        storage = Storage.newInstace(this);

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
            quickLogOperation   = Operation.parseString( savedInstanceState.getString(ARG_QUICK_LOG_OPERATION,   defaults.QUICK_LOG_OPERATION        ));
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
                    Operation.parseString(   defaults.QUICK_LOG_OPERATION)
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
        if (quickLogTime != null) {
            outState.putString(ARG_QUICK_LOG_DATE, quickLogTime.toString());
        }
        if (quickLogCurrency != null) {
            outState.putString(ARG_QUICK_LOG_CURRENCY, quickLogCurrency.toString());
        }

        Log.i(TAG, "saved instance state");
    }

//    --------------------

//    Quick log fragment methods
    @Override
    public void onLogButtonPressed(MoneyTableRow bean) {
        storage.moneyTable.contents.add(bean);
        storage.moneyTable.write();
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
    public void onDateButtonPressed(boolean save, Time Time) {
        if (save) {
            quickLogTime = Time;
            Log.i(TAG, "saved " + QuickLogFragment.TAG + " Time " + Time.toString());
        } else {
            quickLogTime = null;
            Log.i(TAG, "unsaved " + QuickLogFragment.TAG + " Time");
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

//    Storage methods
    @Override
    public void isExternalStorageWritable(boolean is) {
        if (!is) {
            Toast.makeText(this, "external storage not writable", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void isExternalStorageReadable(boolean is) {
        if (!is) {
            Toast.makeText(this, "external storage not readable", Toast.LENGTH_LONG).show();
        }
    }
//    --------------------
}
