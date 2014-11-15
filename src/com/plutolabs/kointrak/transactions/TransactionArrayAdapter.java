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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author ct.
 */
public class TransactionArrayAdapter extends ArrayAdapter<Transaction> {

    private final int resource;
    private final Context context;
    private final List<Transaction> values;
    private final int day;
    private final int month;

    public TransactionArrayAdapter(Context context, int resource, List<Transaction> values) {
        super(context, resource, values);
        this.resource = resource;
        this.context = context;
        this.values = values;
        day = 0;
        month = 0;
    }

    @Override public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(resource, parent, false);
        Transaction transaction = values.get(position);

        TextView transactionIDView = (TextView) view.findViewById(R.id.transaction_id);
        String txid = transaction.getTxid();
        transactionIDView.setText(txid.substring(0,4)+"..."+txid.substring(txid.length()-5,txid.length()-1));

        TextView timeView = (TextView) view.findViewById(R.id.time);
        Date time=new java.util.Date((long)transaction.getTime()*1000);
        DateFormat df = new SimpleDateFormat("HH:mm:ss a\nMM/dd/yyyy");
        timeView.setText(String.valueOf(df.format(time)));

        TextView confirmView = (TextView) view.findViewById(R.id.confirmations);
        confirmView.setText(String.valueOf(transaction.getConfirmations()));

        TextView valueView = (TextView) view.findViewById(R.id.value);
        if(transaction.getIncoming() != null) { // incoming trans
            valueView.setText(String.valueOf(transaction.getIncoming().getValue()));
            valueView.setBackgroundColor(Color.GREEN);
            valueView.setTextColor(Color.BLACK);
        } else {                                // outgoing trans
            valueView.setText(String.valueOf(transaction.getOutgoing().getValue()));
            valueView.setBackgroundColor(Color.RED);
            valueView.setTextColor(Color.WHITE);
        }

        return view;
    }
}
