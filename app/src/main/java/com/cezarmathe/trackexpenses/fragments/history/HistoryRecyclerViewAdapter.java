package com.cezarmathe.trackexpenses.fragments.history;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.cezarmathe.trackexpenses.R;
import com.cezarmathe.trackexpenses.fragments.HistoryFragment;
import com.cezarmathe.trackexpenses.fragments.HistoryFragment.OnHistoryFragmentInteractionListener;
import com.cezarmathe.trackexpenses.storage.models.MoneyTableRow;

import java.util.ArrayList;
import java.util.Locale;

public class HistoryRecyclerViewAdapter extends RecyclerView.Adapter<HistoryRecyclerViewAdapter.ViewHolder> {

    public static final String TAG = "HistoryRecyclerViewAdapter";

    private         ArrayList<MoneyTableRow>                list;
    private final   OnHistoryFragmentInteractionListener    mListener;
    private final   Locale                                  locale;

    public HistoryRecyclerViewAdapter(ArrayList<MoneyTableRow> items, HistoryFragment.OnHistoryFragmentInteractionListener listener, Locale locale) {
        Log.d(TAG, "HistoryRecyclerViewAdapter() called with: items = [" + items + "], listener = [" + listener + "]");
        list = items;
        mListener = listener;
        this.locale = locale;
        Log.i(TAG, "HistoryRecyclerViewAdapter: created");
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder() called with: parent = [" + parent + "], viewType = [" + viewType + "]");
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_history, parent, false);
        ViewHolder v = new ViewHolder(view);
        Log.d(TAG, "onCreateViewHolder() returned: " + v);
        return v;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder() called with: holder = [" + holder + "], position = [" + position + "]");
        holder.mItem = list.get(position);

        Log.d(TAG, "onBindViewHolder: setting ui elements");

        holder.operationDisplay.setText(holder.mItem.getOperation().toSign());
        holder.amountDisplay.setText(String.format(
                locale,
                "%.2f",
                holder.mItem.getAmount()
        ));
        holder.currencyDisplay.setText(holder.mItem.getCurrency().toString());
        holder.timeDisplay.setText(
                holder.mItem.getDateTime().toString("HH:mm", locale)
        );
        holder.dateDisplay.setText(
                holder.mItem.getDateTime().toString("dd.MM.yyyy")
        );

        Log.d(TAG, "onBindViewHolder: setting event listeners");
        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MoneyTableRow item = mListener.onItemEditPressed(holder.mItem, holder.getAdapterPosition());
                if (item != null) {
                    list.set(holder.getAdapterPosition(), item);
                }
            }
        });

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener.onItemDeletePressed(holder.mItem, holder.getAdapterPosition())) {
                    notifyItemRemoved(holder.getAdapterPosition());
                    notifyItemRangeChanged(holder.getAdapterPosition(), list.size());
                }
            }
        });

        Log.d(TAG, "onBindViewHolder: binded");
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount() called");
        return list.size();
    }

    public void setList(ArrayList<MoneyTableRow> list) {
        this.list = list;
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
            Log.d(TAG, "ViewHolder() called with: view = [" + view + "]");
            mView = view;

            operationDisplay = view.findViewById(R.id.item_history_operation);
            amountDisplay    = view.findViewById(R.id.item_history_amount);
            currencyDisplay  = view.findViewById(R.id.item_history_currency);
            timeDisplay      = view.findViewById(R.id.item_history_time);
            dateDisplay      = view.findViewById(R.id.item_history_date);
            editButton       = view.findViewById(R.id.item_history_edit);
            deleteButton     = view.findViewById(R.id.item_history_delete);

            Log.d(TAG, "ViewHolder: created");
        }

        @Override
        public String toString() {
            Log.d(TAG, "toString() called");
            return super.toString() + " '" + amountDisplay.getText() + "'";
        }

    }
}
