package com.cezarmathe.trackexpenses.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cezarmathe.trackexpenses.R;
import com.cezarmathe.trackexpenses.config.UserConfig;
import com.cezarmathe.trackexpenses.storage.types.Operation;
import com.cezarmathe.trackexpenses.storage.models.MoneyTableRow;
import com.cezarmathe.trackexpenses.storage.types.Tag;

import org.joda.time.DateTime;

import java.util.Currency;

public class QuickLogFragment extends Fragment {

    public  static final String TAG             = "QuickLog";

    private Currency    currency;
    private String      notes;
    private Operation   operation;
    private DateTime    dateTime;
    private Tag         tag;

//    UI event listener
    private OnQuickLogFragmentInteractionListener mListener;

//    UI elements
    private Button      logButton;
    private Button      currencyButton;
    private Button      timeButton;
    private Button      notesButton;
    private EditText    amountEditText;
    private Button      operationButton;
    private Button      tagButton;

    public QuickLogFragment() {
        Log.d(TAG, "QuickLogFragment() called");
    }

    public static QuickLogFragment newInstance() {
        Log.d(TAG, "newInstance() called");
        QuickLogFragment fragment = new QuickLogFragment();

        Bundle args = new Bundle();
        fragment.setArguments(args);

        Log.i(TAG, "newInstance: created " + TAG);
        Log.d(TAG, "newInstance() returned: " + fragment);
        return fragment;
    }

    public static QuickLogFragment newInstance(UserConfig userConfig) {
        Log.d(TAG, "newInstance() called with: userConfig = [" + userConfig + "]");
        QuickLogFragment quickLogFragment = new QuickLogFragment();

        quickLogFragment.tag = userConfig.getTags().size() != 0 ?
                userConfig.getTags().get(userConfig.getLastCurrencyIndex()) :
                UserConfig.DEFAULT_TAG;
        quickLogFragment.dateTime = userConfig.getSavedDateTime();
        quickLogFragment.operation = UserConfig.DEFAULT_OPERATION;
        quickLogFragment.currency = userConfig.getCurrencies().size() != 0 ?
                userConfig.getCurrencies().get(userConfig.getLastCurrencyIndex()) :
                UserConfig.DEFAULT_CURRENCY;
        quickLogFragment.notes = userConfig.getSavedNotes();

        Log.d(TAG, "newInstance() returned: " + quickLogFragment);
        return quickLogFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate() called with: savedInstanceState = [" + savedInstanceState + "]");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView() called with: inflater = [" + inflater + "], container = [" + container + "], savedInstanceState = [" + savedInstanceState + "]");

        Log.d(TAG, "onCreateView: inflating layout");
        View view = inflater.inflate(R.layout.fragment_quick_log, container, false);

        Log.d(TAG, "onCreateView: retrieving ui components & initializing them");
        logButton = view.findViewById(R.id.quicklog_log_button);
        logButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onLogButtonPressed();
            }
        });

        currencyButton = view.findViewById(R.id.quicklog_currency_button);
        currencyButton.setText(currency.toString());
        currencyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCurrencyButtonPressed();
            }
        });
        currencyButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                onCurrencyButtonLongPressed();
                return true;
            }
        });

        timeButton = view.findViewById(R.id.quicklog_date_button);
        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDateButtonPressed();
            }
        });

        notesButton = view.findViewById(R.id.quicklog_notes_button);
        notesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onNotesButtonPressed();
            }
        });

        amountEditText = view.findViewById(R.id.quicklog_amount_edittext);

        operationButton = view.findViewById(R.id.quicklog_operation_button);
        operationButton.setText(operation.toSign() );
        operationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onOperationButtonPressed();
            }
        });

        tagButton = view.findViewById(R.id.quicklog_tag_button);
        tagButton.setText(tag.getName());
        tagButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onTagButtonPressed();
            }
        });
        tagButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                onTagButtonLongPressed();
                return true;
            }
        });

        Log.i(TAG, "onCreateView: created view");
        Log.d(TAG, "onCreateView() returned: " + view);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG, "onAttach() called with: context = [" + context + "]");

        if (context instanceof OnQuickLogFragmentInteractionListener) {
            mListener = (OnQuickLogFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnHistoryFragmentInteractionListener");
        }

        Log.i(TAG, "onAttach: attached");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "onDetach() called");
        mListener = null;
        Log.i(TAG, "onDetach: detached");
    }

    public interface OnQuickLogFragmentInteractionListener {
        void        onLogButtonPressed(MoneyTableRow bean);
        Currency    onCurrencyButtonPressed();
        Currency    onCurrencyButtonLongPressed(Currency old);
        DateTime    onDateButtonPressed(DateTime old);
        String      onNotesButtonPressed(String old);
        void        onOperationButtonPressed(Operation operation);
        Tag         onTagButtonPressed(Tag old);
        Tag         onTagButtonLongPressed(Tag old);
    }

    public void onLogButtonPressed() {
        Log.d(TAG, "onLogButtonPressed() called");
        if (mListener != null) {

            if (amountEditText.getText().toString().equals("")) {
                Toast.makeText(getContext(), "You need to enter a number.", Toast.LENGTH_LONG).show();
                return;
            }

            MoneyTableRow bean = new MoneyTableRow();

            bean.setAmount(Double.parseDouble(amountEditText.getText().toString()));
            bean.setCurrency(currency);
            bean.setDateTime(DateTime.now());
            bean.setNotes(notes);
            bean.setOperation(operation);

            mListener.onLogButtonPressed(bean);

            amountEditText.setText("");
        }
    }

    public void onCurrencyButtonPressed() {
        Log.d(TAG, "onCurrencyButtonPressed() called");
        if (mListener != null) {
            currency = mListener.onCurrencyButtonPressed();
            currencyButton.setText(currency.toString());
        }
    }

    public void onCurrencyButtonLongPressed() {
        Log.d(TAG, "onCurrencyButtonLongPressed() called");
        if (mListener != null) {
            currency = mListener.onCurrencyButtonLongPressed(currency);
            currencyButton.setText(currency.toString());
        }
    }

    public void onDateButtonPressed() {
        Log.d(TAG, "onDateButtonPressed() called");
        if (mListener != null) {
            dateTime = mListener.onDateButtonPressed(dateTime);
        }
    }

    public void onNotesButtonPressed() {
        Log.d(TAG, "onNotesButtonPressed() called");
        if (mListener != null) {
            notes = mListener.onNotesButtonPressed(notes);
        }
    }

    public void onOperationButtonPressed() {
        Log.d(TAG, "onOperationButtonPressed() called");
        if (mListener != null) {
            switch (operationButton.getText().toString()) {
                case "+":
                    mListener.onOperationButtonPressed(Operation.ADDITION);
                    operationButton.setText(Operation.SUBTRACTION.toSign());
                    operation = Operation.SUBTRACTION;
                    break;
                case "-":
                    mListener.onOperationButtonPressed(Operation.SUBTRACTION);
                    operationButton.setText(Operation.ADDITION.toSign());
                    operation = Operation.ADDITION;
                    break;
            }
            Log.i(TAG, "onOperationButtonPressed: switched to " + operation.toString());
        }
    }

    public void onTagButtonPressed() {
        Log.d(TAG, "onTagButtonPressed() called");
        if (mListener != null) {
            tag = mListener.onTagButtonPressed(tag);
        }
    }

    public void onTagButtonLongPressed() {
        Log.d(TAG, "onTagButtonLongPressed() called");
        if (mListener != null) {
            tag = mListener.onTagButtonLongPressed(tag);
        }
    }
}
