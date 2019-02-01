package com.cezarmathe.trackexpenses.fragments;

import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cezarmathe.trackexpenses.R;
import com.cezarmathe.trackexpenses.config.UserConfig;
import com.cezarmathe.trackexpenses.fragments.history.HistoryRecyclerViewAdapter;
import com.cezarmathe.trackexpenses.storage.models.MoneyTableRow;

import java.util.ArrayList;
import java.util.Locale;

public class HistoryFragment extends Fragment {

    public static final String TAG = "HistoryFragment";

    private Locale locale;

    private RecyclerView                            mView;
    private HistoryRecyclerViewAdapter              mAdapter;
    private OnHistoryFragmentInteractionListener    mListener;

    public HistoryFragment() {
        Log.d(TAG, "HistoryFragment() called");
    }

    public static HistoryFragment newInstance(UserConfig userConfig) {
        Log.d(TAG, "newInstance() called with: userConfig = [" + userConfig + "]");
        HistoryFragment fragment = new HistoryFragment();

        fragment.locale = userConfig.getLocale() != null ?
                userConfig.getLocale() :
                UserConfig.DEFAULT_LOCALE;

        Bundle args = new Bundle();
        fragment.setArguments(args);
        Log.d(TAG, "newInstance() returned: " + fragment);
        Log.i(TAG, "newInstance: created");
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate() called with: savedInstanceState = [" + savedInstanceState + "]");

        if (getArguments() != null) {
            Log.d(TAG, "onCreate: using passed arguments as parameters");
        } else {
            Log.d(TAG, "onCreate: using defaults as parameters");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView() called with: inflater = [" + inflater + "], container = [" + container + "], savedInstanceState = [" + savedInstanceState + "]");
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        Log.d(TAG, "onCreateView: setting the mAdapter");
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            mView = (RecyclerView) view;
            mView.setLayoutManager(new LinearLayoutManager(context));
            mAdapter = new HistoryRecyclerViewAdapter(mListener.onUpdateListRequested(),
                    mListener,
                    locale
            );
            mView.setAdapter(mAdapter);
        }
        updateList();
        Log.d(TAG, "onCreateView() returned: " + view);
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG, "onAttach() called with: context = [" + context + "]");
        if (context instanceof OnHistoryFragmentInteractionListener) {
            mListener = (OnHistoryFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnHistoryFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "onDetach() called");
        mListener = null;
    }

    public interface OnHistoryFragmentInteractionListener {
        boolean                     onItemDeletePressed(MoneyTableRow item, int index);
        MoneyTableRow               onItemEditPressed(MoneyTableRow item, int index);
        ArrayList<MoneyTableRow>    onUpdateListRequested();
    }

    public void updateList() {
        mAdapter.setList(mListener.onUpdateListRequested());
        mView.setAdapter(mAdapter);
    }
}
