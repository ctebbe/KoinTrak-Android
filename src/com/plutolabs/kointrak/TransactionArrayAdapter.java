package com.plutolabs.kointrak;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import so.chain.entity.Transaction;

/**
 * @author ct.
 */
public class TransactionArrayAdapter extends ArrayAdapter<Transaction> {

    private final int resource;
    private final Context context;
    private final Transaction[] values;

    public TransactionArrayAdapter(Context context, int resource, Transaction[] values) {
        super(context, resource, values);
        this.resource = resource;
        this.context = context;
        this.values = values;
    }

    @Override public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(resource, parent, false);
        Transaction transaction = values[position];

        TextView transactionIDView = (TextView) view.findViewById(R.id.transaction_id);
        transactionIDView.setText(transaction.getTxid());

        TextView timeView = (TextView) view.findViewById(R.id.time);
        timeView.setText(transaction.getTime());

        TextView confirmView = (TextView) view.findViewById(R.id.confirmations);
        confirmView.setText(transaction.getConfirmations());

        TextView valueView = (TextView) view.findViewById(R.id.value);
        if(transaction.getIncoming() != null) { // incoming trans
            valueView.setText(String.valueOf(transaction.getIncoming().getValue()));
        } else {                                // outgoing trans
            valueView.setText(String.valueOf(transaction.getOutgoing().getValue()));
        }

        return view;
    }
}
