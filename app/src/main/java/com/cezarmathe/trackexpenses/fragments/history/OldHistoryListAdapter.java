package com.cezarmathe.trackexpenses.fragments.history;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.cezarmathe.trackexpenses.R;
import com.cezarmathe.trackexpenses.fragments.OldHistoryFragment;
import com.cezarmathe.trackexpenses.storage.models.MoneyTableRow;

import java.util.ArrayList;

public class OldHistoryListAdapter extends ArrayAdapter<MoneyTableRow> implements View.OnClickListener, View.OnGenericMotionListener {

    private class ItemViewHolder {
        public TextView operationDisplay;
        public TextView amountDisplay;
        public TextView currencyDisplay;
        public TextView timeDisplay;
        public TextView dateDisplay;
        public Button   editButton;
        public Button   deleteButton;
    }

    public static final String TAG = "OldHistoryListAdapter";

    private int                         lastPosition = -1;
    private ArrayList<MoneyTableRow>    list;
    private Context                     context;

    private OldHistoryFragment.OnHistoryFragmentInteractionListener mListener;


    public OldHistoryListAdapter(@NonNull Context context, /*OldHistoryFragment.OnHistoryFragmentInteractionListener listener, */ArrayList<MoneyTableRow> list) {
        super(context, R.layout.item_history);
        Log.d(TAG, "OldHistoryListAdapter() called with: context = [" + context + "], list = [" + list + "]");
        this.context = context;
//        this.mListener = listener;
        this.list = list;

        Log.i(TAG, "OldHistoryListAdapter: created");
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Log.d(TAG, "getView() called with: position = [" + position + "], convertView = [" + convertView + "], parent = [" + parent + "]");

        final MoneyTableRow item = getItem(position);
        ItemViewHolder viewHolder;
        final View view;

        if (convertView == null) {
            Log.d(TAG, "getView: creating new view");
            viewHolder = new ItemViewHolder();

            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_history, parent, false);

            viewHolder.operationDisplay = convertView.findViewById(R.id.item_history_operation);
            viewHolder.amountDisplay    = convertView.findViewById(R.id.item_history_amount);
            viewHolder.currencyDisplay  = convertView.findViewById(R.id.item_history_currency);
            viewHolder.timeDisplay      = convertView.findViewById(R.id.item_history_time);
            viewHolder.dateDisplay      = convertView.findViewById(R.id.item_history_date);
            viewHolder.editButton       = convertView.findViewById(R.id.item_history_edit);
            viewHolder.deleteButton     = convertView.findViewById(R.id.item_history_delete);

            view = convertView;
            convertView.setTag(viewHolder);
        } else {
            Log.d(TAG, "getView: reusing old view");
            viewHolder = (ItemViewHolder) convertView.getTag();
            view = convertView;
        }

        // TODO: 24/01/2019 add proper animations
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.anim);
        view.startAnimation(animation);

        lastPosition = position;

        Log.d(TAG, "getView: adding data to the ui");
        viewHolder.operationDisplay.setText(item.getOperation().toSign());
        viewHolder.amountDisplay.setText(String.valueOf(item.getAmount()));
        viewHolder.currencyDisplay.setText(item.getCurrency().toString());
        viewHolder.timeDisplay.setText(item.getTime().hour + ":" + item.getTime().minute);
        viewHolder.dateDisplay.setText(item.getTime().day + "/" + item.getTime().month + "/" + item.getTime().year);

        Log.d(TAG, "getView: adding event listeners");
        viewHolder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MoneyTableRow mItem = mListener.onItemEditPressed(item);
                if (mItem != null) {
                    list.set(position, mItem);
                }
            }
        });

        viewHolder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener.onItemDeletePressed(item)) {
                    list.remove(position);
                }
            }
        });
        Log.d(TAG, "getView() returned: " + view);
        Log.i(TAG, "getView: created view");
        return view;
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public boolean onGenericMotion(View view, MotionEvent motionEvent) {
        return false;
    }

    public void setList(ArrayList<MoneyTableRow> list) {
        this.list = list;
    }

}
