package com.cezarmathe.trackexpenses;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.AnimatorRes;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.cezarmathe.trackexpenses.config.UserConfig;
import com.cezarmathe.trackexpenses.fragments.HistoryFragment;
import com.cezarmathe.trackexpenses.fragments.QuickLogFragment;
import com.cezarmathe.trackexpenses.storage.Storage;
import com.cezarmathe.trackexpenses.storage.Table;
import com.cezarmathe.trackexpenses.storage.models.MoneyTableRow;
import com.cezarmathe.trackexpenses.storage.types.Operation;
import com.cezarmathe.trackexpenses.storage.types.Tag;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Currency;

public class Dashboard extends Activity implements QuickLogFragment.OnQuickLogFragmentInteractionListener,
        Table.TableEventHook,
        HistoryFragment.OnHistoryFragmentInteractionListener {

//    Activity variables
    public  static final String TAG                 = "Dashboard";
    private static final String ARG_NAVIGATION_ITEM = "navigation_item_id";
    private static final int    DEFAULT_MENU_ITEM   = R.id.quicklog_bottom_navigation_item;

    private BottomNavigationView navigationView;
    private int activeMenuItem;
    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch(item.getItemId()) {
                case R.id.quicklog_bottom_navigation_item:
                    if (activeMenuItem == R.id.quicklog_bottom_navigation_item) {
                        changeFragment(quickLogFragment);
                        return true;
                    }
                    if (activeMenuItem == R.id.history_bottom_navigation_item) {
                        changeFragment(quickLogFragment, historyFragment, R.animator.slide_in_right, R.animator.slide_out_left);
                    }
                    activeMenuItem = item.getItemId();
                    return true;
                case R.id.history_bottom_navigation_item:
                    if (activeMenuItem == R.id.quicklog_bottom_navigation_item) {
                        changeFragment(historyFragment, quickLogFragment, R.animator.slide_in_left, R.animator.slide_out_right);
                    }
                    activeMenuItem = item.getItemId();
                    return true;
                case R.id.statistics_bottom_navigation_item:
                    activeMenuItem = item.getItemId();
                    return true;
                default:
                    navigationView.setSelectedItemId(DEFAULT_MENU_ITEM);
                    return true;
            }
        }
    };
//    --------------------

//    Storage variables
    private Storage     storage;
    private UserConfig  userConfig;
//    --------------------

//    Quick log fragment variables
    private QuickLogFragment quickLogFragment;
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

        Log.d(TAG, "onCreate: initializing storage");
        storage = Storage.newInstance(this);

        Log.d(TAG, "onCreate: loading user configs");
        userConfig = UserConfig.read(storage.getFile(UserConfig.FILE_NAME));
//        ArrayList<Currency> a = userConfig.getCurrencies();
//        a.add(Currency.getInstance("RON"));
//        ArrayList<Tag> b = userConfig.getTags();
//        b.add(new Tag("Parents", 1234));
//        UserConfig.write(userConfig, storage.getFile(UserConfig.FILE_NAME));


        Log.d(TAG, "onCreate: initializing view objects");
        navigationView = findViewById(R.id.bottom_navigation_view);
        navigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);

        Log.d(TAG, "onCreate: creating fragments");
        quickLogFragment = QuickLogFragment.newInstance(userConfig);
        historyFragment = HistoryFragment.newInstance(userConfig);

        if (savedInstanceState != null) {
            Log.i(TAG, "onCreate: saved instance state is available");
            activeMenuItem = savedInstanceState.getInt(ARG_NAVIGATION_ITEM, DEFAULT_MENU_ITEM);
        } else {
            Log.i(TAG, "onCreate: no saved instance state is available");
            activeMenuItem      = DEFAULT_MENU_ITEM;
        }

        Log.d(TAG, "onCreate: selecting default fragment");
        navigationView.setSelectedItemId(activeMenuItem);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState() called with: outState = [" + outState + "]");

        outState.putInt(ARG_NAVIGATION_ITEM, activeMenuItem);

        Log.i(TAG, "onSaveInstanceState: saved instance state");
    }

    private void changeFragment(Fragment newFragment, Fragment oldFragment, @AnimatorRes Integer in, @AnimatorRes Integer out) {
        Log.d(TAG, "changeFragment() called with: fragment = [" + newFragment + "], in = [" + in + "], out = [" + out + "]");
        if (newFragment.isVisible()) {
            Log.i(TAG, "changeFragment: fragment " + newFragment.getClass().toString() + " already visible");
            return;
        }

        FragmentTransaction ft = getFragmentManager().beginTransaction();
//        if (quickLogFragment.isVisible()) {
//            ft.detach(quickLogFragment);
//        }
//        if (historyFragment.isVisible()) {
//            ft.detach(historyFragment);
//        }
//
//        if (!fragment.isAdded()) {
//            Log.d(TAG, "changeFragment: adding fragment");
//            ft.add(R.id.fragment_container, fragment);
//        }
        ft.setCustomAnimations(in, out);
        ft.replace(R.id.fragment_container, newFragment);
        ft.detach(oldFragment);
        ft.commit();
        Log.i(TAG, "changeFragment: changed fragment to " + newFragment.getClass().toString());

//        oldFragment.setExitTransition(new Fade().setDuration(1000).setStartDelay(500));
//        newFragment.setEnterTransition(new Fade().setDuration(1500).setStartDelay(750));
//        ft.replace(R.id.fragment_container, newFragment);
//        ft.commitAllowingStateLoss();
    }

    private void changeFragment(Fragment fragment) {
        Log.d(TAG, "changeFragment() called with: fragment = [" + fragment + "]");
        if (fragment.isVisible()) {
            return;
        }
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.animator.enter_from_down, R.animator.leave_to_top);
        ft.add(R.id.fragment_container, fragment);
        ft.commit();
    }

    @Override
    protected void onPause() {
        super.onPause();
        UserConfig.write(userConfig, storage.getFile(UserConfig.FILE_NAME));
    }

    @Override
    protected void onResume() {
        super.onResume();
        userConfig = UserConfig.read(storage.getFile(UserConfig.FILE_NAME));
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        float x1 = 0, x2;
//        switch(event.getAction())
//        {
//            case MotionEvent.ACTION_DOWN:
//                x1 = event.getX();
//                break;
//            case MotionEvent.ACTION_UP:
//                x2 = event.getX();
//                float deltaX = x2 - x1;
//                if (deltaX > 100) {
//                    if (activeMenuItem == R.id.quicklog_bottom_navigation_item) {
//                        navigationView.setSelectedItemId(R.id.history_bottom_navigation_item);
//                    }
//                } else if (deltaX < -100) {
//                    if (activeMenuItem == R.id.history_bottom_navigation_item) {
//                        navigationView.setSelectedItemId(R.id.quicklog_bottom_navigation_item);
//                    }
//                }
//                break;
//        }
//        return super.onTouchEvent(event);
//    }

    //    --------------------

//    Quick log fragment methods
    @Override
    public void onLogButtonPressed(MoneyTableRow bean) {
        Log.d(TAG, "onLogButtonPressed() called with: bean = [" + bean + "]");
        storage.moneyTable.add(bean);
    }

    @Override
    public Currency onCurrencyButtonPressed() {
        Log.d(TAG, "onCurrencyButtonPressed() called");

        Currency quickLogCurrency = UserConfig.DEFAULT_CURRENCY;

        if (userConfig.getCurrencies().size() == 0) {
            return quickLogCurrency;
        }
        if (userConfig.getLastCurrencyIndex() == userConfig.getCurrencies().size() - 1) {
            userConfig.setLastCurrencyIndex(0);
        } else {
            userConfig.setLastCurrencyIndex(userConfig.getLastCurrencyIndex() + 1);
        }
        quickLogCurrency = userConfig.getCurrencies().get(userConfig.getLastCurrencyIndex());
        Log.d(TAG, "onCurrencyButtonPressed() returned: " + quickLogCurrency);
        return quickLogCurrency;
    }

    @Override
    public Currency onCurrencyButtonLongPressed(Currency old) {
        Log.d(TAG, "onCurrencyButtonLongPressed() called with: old = [" + old + "]");

        // TODO: 01/02/2019 make a popup for selecting the currency

        return old;
    }

    @Override
    public DateTime onDateButtonPressed(DateTime old) {
        Log.d(TAG, "onDateButtonPressed() called with: old = [" + old + "]");

        // TODO: 01/02/2019 make a popup for selecting another date and time

//        if (save) {
//            quickLogDateTime = dateTime;
//            Log.i(TAG, "saved " + QuickLogFragment.TAG + " DateTime " + dateTime.toString());
//        } else {
//            quickLogDateTime = null;
//            Log.i(TAG, "unsaved " + QuickLogFragment.TAG + " DateTime");
//        }
        return old;
    }

    @Override
    public String onNotesButtonPressed(String old) {
        Log.d(TAG, "onNotesButtonPressed() called with: old = [" + old + "]");

        // TODO: 01/02/2019 make a popup for writing the notes

//        if (save) {
//            quickLogNotes = notes;
//            Log.i(TAG, "saved " + QuickLogFragment.TAG + " notes " + notes);
//        } else {
//            quickLogNotes = "";
//            Log.i(TAG, "unsaved " + QuickLogFragment.TAG + " notes");
//        }
        return old;
    }

    @Override
    public void onOperationButtonPressed(Operation operation) {
        Log.d(TAG, "onOperationButtonPressed() called with: operation = [" + operation + "]");
    }

    @Override
    public Tag onTagButtonPressed(Tag old) {
        Tag quickLogTag = UserConfig.DEFAULT_TAG;

        if (userConfig.getTags().size() == 0) {
            return quickLogTag;
        }
        if (userConfig.getLastCurrencyIndex() == userConfig.getCurrencies().size() - 1) {
            userConfig.setLastCurrencyIndex(0);
        } else {
            userConfig.setLastCurrencyIndex(userConfig.getLastCurrencyIndex() + 1);
        }
        quickLogTag = userConfig.getTags().get(userConfig.getLastTagIndex());
        return quickLogTag;
    }

    @Override
    public Tag onTagButtonLongPressed(Tag old) {

        // TODO: 01/02/2019 make a popup for selecting the tag or adding/removing tags

        return old;
    }
//    --------------------

//    History fragment methods
    @Override
    public boolean onItemDeletePressed(MoneyTableRow item, int index) {
        Log.d(TAG, "onItemDeletePressed() called with: item = [" + item + "]");
        return storage.moneyTable.remove(index);
    }

    @Override
    public MoneyTableRow onItemEditPressed(MoneyTableRow item, int index) {
        Log.d(TAG, "onItemEditPressed() called with: item = [" + item + "]");
        return storage.moneyTable.edit(item, index);
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
