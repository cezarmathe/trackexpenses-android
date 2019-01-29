package com.cezarmathe.trackexpenses;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.cezarmathe.trackexpenses.config.Defaults;
import com.cezarmathe.trackexpenses.fragments.HistoryFragment;
import com.cezarmathe.trackexpenses.fragments.QuickLogFragment;
import com.cezarmathe.trackexpenses.storage.Storage;
import com.cezarmathe.trackexpenses.storage.Table;
import com.cezarmathe.trackexpenses.storage.models.MoneyTableRow;
import com.cezarmathe.trackexpenses.storage.types.Operation;
import com.cezarmathe.trackexpenses.storage.types.Time;

import java.util.ArrayList;
import java.util.Currency;

public class Dashboard extends Activity implements QuickLogFragment.OnQuickLogFragmentInteractionListener,
        Table.TableEventHook,
        HistoryFragment.OnHistoryFragmentInteractionListener {

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
                    changeFragment(quickLogFragment);
                    return true;
                case R.id.history_bottom_navigation_item:
                    activeMenuItem = item.getItemId();
                    changeFragment(historyFragment);
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
    private static final String ARG_QUICK_LOG_TIME      = "quick_log_time";
    private static final String ARG_QUICK_LOG_NOTES     = "quick_log_notes";
    private static final String ARG_QUICK_LOG_OPERATION = "quick_log_operation";

    private Double      quickLogAmount;
    private Currency    quickLogCurrency;
    private Time        quickLogTime;
    private String      quickLogNotes;
    private Operation   quickLogOperation;
//    --------------------

//    History fragment variables
    private HistoryFragment historyFragment;
//    --------------------

//    Activity methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate() called with: savedInstanceState = [" + savedInstanceState + "]");
        setContentView(R.layout.activity_dashboard);

        Log.d(TAG, "onCreate: loading defaults");
        defaults = Defaults.newInstance(this);

        Log.d(TAG, "onCreate: initializing storage");
        storage = Storage.newInstance(this);

        Log.d(TAG, "onCreate: initializing view objects");
        navigationView = findViewById(R.id.bottom_navigation_view);
        navigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);

        if (savedInstanceState != null) {
            Log.i(TAG, "onCreate: saved instance state is available");
            activeMenuItem      =                       savedInstanceState.getInt   (ARG_NAVIGATION_ITEM,       defaults.DASHBOARD_DEFAULT_MENU_ITEM);
            quickLogAmount      =                       savedInstanceState.getDouble(ARG_QUICK_LOG_AMOUNT,      defaults.QUICK_LOG_AMOUNT           );
            quickLogCurrency    = Currency.getInstance( savedInstanceState.getString(ARG_QUICK_LOG_CURRENCY,    defaults.QUICK_LOG_CURRENCY         ));
            quickLogNotes       =                       savedInstanceState.getString(ARG_QUICK_LOG_NOTES,       defaults.QUICK_LOG_NOTES            );
            quickLogOperation   = Operation.parseString(savedInstanceState.getString(ARG_QUICK_LOG_OPERATION,   defaults.QUICK_LOG_OPERATION        ));
            quickLogFragment    = QuickLogFragment.newInstance(
                    quickLogAmount,
                    quickLogCurrency,
                    quickLogNotes,
                    quickLogOperation
            );

            historyFragment = HistoryFragment.newInstance(1);
        } else {
            Log.i(TAG, "onCreate: no saved instance state is available");
            activeMenuItem      = defaults.DASHBOARD_DEFAULT_MENU_ITEM;
            quickLogFragment    = QuickLogFragment.newInstance(
                                            defaults.QUICK_LOG_AMOUNT,
                    Currency.getInstance(   defaults.QUICK_LOG_CURRENCY ),
                                            defaults.QUICK_LOG_NOTES,
                    Operation.parseString(  defaults.QUICK_LOG_OPERATION)
            );

            historyFragment = HistoryFragment.newInstance(1);
        }

        Log.d(TAG, "onCreate: selecting default frqgment");
        navigationView.setSelectedItemId(activeMenuItem);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState() called with: outState = [" + outState + "]");

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
            outState.putString(ARG_QUICK_LOG_TIME, quickLogTime.toString());
        }
        if (quickLogCurrency != null) {
            outState.putString(ARG_QUICK_LOG_CURRENCY, quickLogCurrency.toString());
        }

        Log.i(TAG, "onSaveInstanceState: saved instance state");
    }

    private void changeFragment(Fragment fragment) {
        Log.d(TAG, "changeFragment() called with: fragment = [" + fragment + "]");
        if (fragment.isVisible()) {
            Log.i(TAG, "changeFragment: fragment " + fragment.getClass().toString() + " already visible");
            return;
        }

        FragmentTransaction ft = getFragmentManager().beginTransaction();

        if (quickLogFragment.isVisible()) {
            ft.detach(quickLogFragment);
        }
        if (historyFragment.isVisible()) {
            ft.detach(historyFragment);
        }

        if (!fragment.isAdded()) {
            Log.d(TAG, "changeFragment: adding fragment");
            ft.add(R.id.fragment_container, fragment);
        }

        ft.attach(fragment);
        ft.commit();
        Log.i(TAG, "changeFragment: changed fragment to " + fragment.getClass().toString());
    }

//    --------------------

//    Quick log fragment methods
    @Override
    public void onLogButtonPressed(MoneyTableRow bean) {
        Log.d(TAG, "onLogButtonPressed() called with: bean = [" + bean + "]");
        storage.moneyTable.add(bean);
        historyFragment.updateList();
    }

    @Override
    public Currency onCurrencyButtonPressed() {
        Log.d(TAG, "onCurrencyButtonPressed() called");
        quickLogCurrency = Currency.getInstance("RON");
        return quickLogCurrency;
    }

    @Override
    public Currency onCurrencyButtonLongPressed() {
        Log.d(TAG, "onCurrencyButtonLongPressed() called");
        quickLogCurrency = Currency.getInstance("RON");
        return quickLogCurrency;
    }

    @Override
    public void onDateButtonPressed(boolean save, Time Time) {
        Log.d(TAG, "onDateButtonPressed() called with: save = [" + save + "], Time = [" + Time + "]");
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
        Log.d(TAG, "onNotesButtonPressed() called with: save = [" + save + "], notes = [" + notes + "]");
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
        Log.d(TAG, "onOperationButtonPressed() called with: operation = [" + operation + "]");
        quickLogOperation = operation;
        Log.i(TAG, "saved " + QuickLogFragment.TAG + " operation " + operation.toString());
    }
//    --------------------

//    History fragment methods
    @Override
    public boolean onItemDeletePressed(MoneyTableRow item) {
        Log.d(TAG, "onItemDeletePressed() called with: item = [" + item + "]");
        return false;
    }

    @Override
    public MoneyTableRow onItemEditPressed(MoneyTableRow item) {
        Log.d(TAG, "onItemEditPressed() called with: item = [" + item + "]");
        return null;
    }

    @Override
    public ArrayList<MoneyTableRow> onUpdateListRequested() {
        Log.d(TAG, "onUpdateListRequested() called");
        return storage.moneyTable.get();
    }
//    --------------------

//    Table event hooks
    @Override
    public void onTableFileCreationFail(String tag, Exception e) {
        Log.d(TAG, "onTableFileCreationFail() called with: tag = [" + tag + "], e = [" + e + "]");
        if (tag.equals(storage.moneyTable.TAG)) {
            makeShortToast("Failed to create " + tag + " file");
        }
    }

    @Override
    public void onReadFail(String tag, Exception e) {
        Log.d(TAG, "onReadFail() called with: tag = [" + tag + "], e = [" + e + "]");
        makeLongToast("Failed to read the money table.");
    }

    @Override
    public void onReadSuccess(String tag) {
        Log.d(TAG, "onReadSuccess() called with: tag = [" + tag + "]");
    }

    @Override
    public void onWriteFail(String tag, Exception e) {
        Log.d(TAG, "onWriteFail() called with: tag = [" + tag + "], e = [" + e + "]");
        makeLongToast("Failed to write the money table.");
    }

    @Override
    public void onWriteSuccess(String tag) {
        Log.d(TAG, "onWriteSuccess() called with: tag = [" + tag + "]");
    }

    @Override
    public void onReaderMethodFail(String tag, Exception e) {
        Log.d(TAG, "onReaderMethodFail() called with: tag = [" + tag + "], e = [" + e + "]");
    }

    @Override
    public void onWriterMethodFail(String tag, Exception e) {
        Log.d(TAG, "onWriterMethodFail() called with: tag = [" + tag + "], e = [" + e + "]");
    }
    //    --------------------

//    Other methods
    public void makeShortToast(String test) {
        Log.d(TAG, "makeShortToast() called with: test = [" + test + "]");
        Toast.makeText(getApplicationContext(), test, Toast.LENGTH_SHORT).show();
    }

    public void makeLongToast(String test) {
        Log.d(TAG, "makeLongToast() called with: test = [" + test + "]");
        Toast.makeText(getApplicationContext(), test, Toast.LENGTH_LONG).show();
    }
//    --------------------
}
