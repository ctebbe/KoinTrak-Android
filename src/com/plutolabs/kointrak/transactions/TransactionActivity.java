package com.plutolabs.kointrak.transactions;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.plutolabs.kointrak.KoinTrak;
import com.plutolabs.kointrak.R;
import com.plutolabs.kointrak.address.AddressField;
import com.plutolabs.kointrak.address.Main;
import com.plutolabs.kointrak.impl.KoinTrakImpl;
import so.chain.entity.Address;
import so.chain.entity.Network;
import so.chain.entity.Transaction;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author ct.
 */
public class TransactionActivity extends ListActivity {

    KoinTrak koinTrak = KoinTrakImpl.getInstance();
    TransactionArrayAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new TransactionArrayAdapter(this, R.layout.transaction, new ArrayList<Transaction>());
        setListAdapter(adapter);

        Intent intent = getIntent();
        new TransactionTask().execute((AddressField) intent.getSerializableExtra(Main.CLICKED_ADDRESS));
    }

    private void updateTransactions(List<Transaction> transactions) {
        for(Transaction trans : transactions) {
            adapter.add(trans);
            adapter.notifyDataSetChanged();
        }
    }


    private class TransactionTask extends AsyncTask<AddressField, Void, List<Transaction>> {

        @Override
        protected List<Transaction> doInBackground(AddressField... params) {
            AddressField clickedAddress = params[0];
            Address address = koinTrak.getAddress(clickedAddress.getIcon(), clickedAddress.getAddress());
            if (address != null) {
                return address.getTxs();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Transaction> transactions) {
            updateTransactions(transactions);
        }

    }

}
