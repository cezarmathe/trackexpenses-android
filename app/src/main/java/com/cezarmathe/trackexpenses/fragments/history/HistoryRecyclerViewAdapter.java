package com.cezarmathe.trackexpenses.fragments.history;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.cezarmathe.trackexpenses.R;
import com.cezarmathe.trackexpenses.fragments.HistoryFragment.OnListFragmentInteractionListener;
import com.cezarmathe.trackexpenses.storage.models.MoneyTableRow;

import java.util.ArrayList;

public class HistoryRecyclerViewAdapter extends RecyclerView.Adapter<HistoryRecyclerViewAdapter.ViewHolder> {

    private final ArrayList<MoneyTableRow> list;
    private final OnListFragmentInteractionListener mListener;

    public HistoryRecyclerViewAdapter(ArrayList<MoneyTableRow> items, OnListFragmentInteractionListener listener) {
        list = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = list.get(position);
        holder.mIdView.setText(list.get(position).id);
        holder.mContentView.setText(list.get(position).content);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View           mView;
        public final TextView       operationDisplay;
        public final TextView       amountDisplay;
        public final TextView       currencyDisplay;
        public final TextView       timeDisplay;
        public final TextView       dateDisplay;
        public final Button         editButton;
        public final Button         deleteButton;

        public MoneyTableRow  mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;

            operationDisplay = view.findViewById(R.id.item_history_operation);
            amountDisplay    = view.findViewById(R.id.item_history_amount);
            currencyDisplay  = view.findViewById(R.id.item_history_currency);
            timeDisplay      = view.findViewById(R.id.item_history_time);
            dateDisplay      = view.findViewById(R.id.item_history_date);
            editButton       = view.findViewById(R.id.item_history_edit);
            deleteButton     = view.findViewById(R.id.item_history_delete);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + amountDisplay.getText() + "'";
        }
    }
}
