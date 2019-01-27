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

import com.cezarmathe.trackexpenses.R;
import com.cezarmathe.trackexpenses.config.Defaults;
import com.cezarmathe.trackexpenses.storage.types.Operation;
import com.cezarmathe.trackexpenses.storage.types.Time;
import com.cezarmathe.trackexpenses.storage.models.MoneyTableRow;

import java.util.Currency;

public class QuickLogFragment extends Fragment {

    public  static final String TAG             = "QuickLog";
    private static final String ARG_AMOUNT      = "amount";
    private static final String ARG_CURRENCY    = "currency";
    private static final String ARG_NOTES       = "notes";
    private static final String ARG_OPERATION   = "operation";
    private static final String ARG_TIME        = "time";

    private Defaults defaults;

    private double      amount;
    private Currency    currency;
    private String      notes;
    private Operation   operation;
    private Time        time;

//    UI event listener
    private OnQuickLogFragmentInteractionListener mListener;

//    UI elements
    private Button      logButton;
    private Button      currencyButton;
    private Button      timeButton;
    private Button      notesButton;
    private EditText    amountEditText;
    private Button      operationButton;

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

    public static QuickLogFragment newInstance(Double amount, Currency currency, String notes, Operation operation) {
        Log.d(TAG, "newInstance() called with: amount = [" + amount + "], currency = [" + currency + "], notes = [" + notes + "], operation = [" + operation + "]");
        QuickLogFragment fragment = new QuickLogFragment();

        Bundle args = new Bundle();
        args.putDouble(ARG_AMOUNT,    amount);
        args.putString(ARG_CURRENCY,  currency.toString());
        args.putString(ARG_NOTES,     notes);
        args.putString(ARG_OPERATION, operation.toSign());
        fragment.setArguments(args);

        Log.i(TAG, "newInstance: created " + TAG);
        Log.d(TAG, "newInstance() returned: " + fragment);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate() called with: savedInstanceState = [" + savedInstanceState + "]");

        Log.d(TAG, "onCreate: initializing defaults");
        defaults = Defaults.newInstance(getActivity());

        if (getArguments() != null) {
            Log.d(TAG, "onCreate: using passed arguments as parameters");
            amount      =                       getArguments().getDouble(ARG_AMOUNT,    defaults.QUICK_LOG_AMOUNT   );
            currency    = Currency.getInstance( getArguments().getString(ARG_CURRENCY,  defaults.QUICK_LOG_CURRENCY ));
            notes       =                       getArguments().getString(ARG_NOTES,     defaults.QUICK_LOG_NOTES    );
            operation   = Operation.parseString(getArguments().getString(ARG_OPERATION, defaults.QUICK_LOG_OPERATION));
        } else {
            Log.d(TAG, "onCreate: using defaults as parameters");
            amount      =                       defaults.QUICK_LOG_AMOUNT;
            currency    = Currency.getInstance( defaults.QUICK_LOG_CURRENCY);
            notes       =                       defaults.QUICK_LOG_NOTES;
            operation   = Operation.parseString(defaults.QUICK_LOG_OPERATION);
        }
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
        if (currency != null) {
            currencyButton.setText(currency.toString());
        } else {
            currencyButton.setText(defaults.QUICK_LOG_CURRENCY);
        }
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
                return false;
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
        amountEditText.setText(defaults.QUICK_LOG_AMOUNT.toString());

        operationButton = view.findViewById(R.id.quicklog_operation_button);
        operationButton.setText(operation.toSign() );
        operationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onOperationButtonPressed();
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
        Currency    onCurrencyButtonLongPressed();
        void        onDateButtonPressed(boolean save, Time Time);
        void        onNotesButtonPressed(boolean save, String notes);
        void        onOperationButtonPressed(Operation operation);
    }

    public void onLogButtonPressed() {
        Log.d(TAG, "onLogButtonPressed() called");
        if (mListener != null) {
            Log.d(TAG, "log button pressed");
            MoneyTableRow bean = new MoneyTableRow();

            bean.setAmount(Double.parseDouble(amountEditText.getText().toString()));
            bean.setCurrency(currency);
            bean.setTime(Time.newInstance());
            bean.setNotes(notes);
            bean.setOperation(operation);

            mListener.onLogButtonPressed(bean);

            // TODO: 24/01/2019 quality of life changes
        }
    }

    public void onCurrencyButtonPressed() {
        Log.d(TAG, "onCurrencyButtonPressed() called");
        if (mListener != null) {
            Log.d(TAG, "currency button pressed");
            currency = mListener.onCurrencyButtonPressed();
            currencyButton.setText(currency.toString());
        }
    }

    public void onCurrencyButtonLongPressed() {
        Log.d(TAG, "onCurrencyButtonLongPressed() called");
        if (mListener != null) {
            Log.d(TAG, "currency button long pressed");
            currency = mListener.onCurrencyButtonLongPressed();
            currencyButton.setText(currency.toString());
        }
    }

    public void onDateButtonPressed() {
        Log.d(TAG, "onDateButtonPressed() called");
        if (mListener != null) {
            Log.d(TAG, "date button pressed");
            mListener.onDateButtonPressed(false, Time.newInstance());
        }
    }

    public void onNotesButtonPressed() {
        Log.d(TAG, "onNotesButtonPressed() called");
        if (mListener != null) {
            notes = "This is a test";
            mListener.onNotesButtonPressed(false, notes);
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
}
