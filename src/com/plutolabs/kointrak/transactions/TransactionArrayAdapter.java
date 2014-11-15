package com.plutolabs.kointrak.transactions;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.plutolabs.kointrak.R;
import so.chain.entity.Transaction;

import java.util.List;

/**
 * @author ct.
 */
public class TransactionArrayAdapter extends ArrayAdapter<Transaction> {

    private final int resource;
    private final Context context;
    private final List<Transaction> values;

    public TransactionArrayAdapter(Context context, int resource, List<Transaction> values) {
        super(context, resource, values);
        this.resource = resource;
        this.context = context;
        this.values = values;
    }

    @Override public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(resource, parent, false);
        Transaction transaction = values.get(position);

        TextView transactionIDView = (TextView) view.findViewById(R.id.transaction_id);
        transactionIDView.setText(transaction.getTxid());

        TextView timeView = (TextView) view.findViewById(R.id.time);
        timeView.setText(String.valueOf(transaction.getTime()));

        TextView confirmView = (TextView) view.findViewById(R.id.confirmations);
        confirmView.setText(String.valueOf(transaction.getConfirmations()));

        TextView valueView = (TextView) view.findViewById(R.id.value);
        if(transaction.getIncoming() != null) { // incoming trans
            valueView.setText(String.valueOf(transaction.getIncoming().getValue()));
            valueView.setBackgroundColor(Color.GREEN);
        } else {                                // outgoing trans
            valueView.setText(String.valueOf(transaction.getOutgoing().getValue()));
            valueView.setBackgroundColor(Color.RED);
        }

        return view;
    }
}
