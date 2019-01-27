package com.cezarmathe.trackexpenses.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.cezarmathe.trackexpenses.R;
import com.cezarmathe.trackexpenses.config.Defaults;
import com.cezarmathe.trackexpenses.fragments.history.OldHistoryListAdapter;
import com.cezarmathe.trackexpenses.storage.models.MoneyTableRow;

import java.util.ArrayList;

public class OldHistoryFragment extends Fragment {

    public static final String TAG = "OldHistoryFragment";

    private Defaults defaults;

    private ListView                    listView;
    private OldHistoryListAdapter       listAdapter;
    private ArrayList<MoneyTableRow>    list;

    private OnHistoryFragmentInteractionListener mListener;

    public OldHistoryFragment() {
        Log.d(TAG, "OldHistoryFragment() called");
    }

    public static OldHistoryFragment newInstance(ArrayList<MoneyTableRow> list) {
        Log.d(TAG, "newInstance() called with: list = [" + list + "]");
        OldHistoryFragment fragment = new OldHistoryFragment();
        fragment.list = list;
        Bundle args = new Bundle();
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
        } else {
            Log.d(TAG, "onCreate: using defaults as parameters");
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView() called with: inflater = [" + inflater + "], container = [" + container + "], savedInstanceState = [" + savedInstanceState + "]");

        Log.d(TAG, "onCreateView: inflating layout");
        View view = inflater.inflate(R.layout.old_fragment_history, container, false);

        Log.d(TAG, "onCreateView: retrieving ui components & initializing them");
        listView = view.findViewById(R.id.history_list);

        Log.i(TAG, "onCreateView: updating list");
        updateList();

        Log.i(TAG, "onCreateView: created view");
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

        if (this.listAdapter == null) {
            this.listAdapter = new OldHistoryListAdapter(context, this.list);
        }

        Log.i(TAG, "onAttach: attached");
    }

    @Override
    public void onDetach() {
        Log.d(TAG, "onDetach() called");
        super.onDetach();
        mListener = null;
        Log.i(TAG, "onDetach: detached");
    }

    public interface OnHistoryFragmentInteractionListener {
        boolean onItemDeletePressed(MoneyTableRow item);
        MoneyTableRow onItemEditPressed(MoneyTableRow item);
        ArrayList<MoneyTableRow> onUpdateListRequested();
    }

    public void updateList() {
        Log.d(TAG, "updateList() called");
        this.list = mListener.onUpdateListRequested();
        this.listAdapter = new OldHistoryListAdapter(getContext(), list);
        this.listView.setAdapter(this.listAdapter);
        Log.i(TAG, "updateList: updated");
    }
}
