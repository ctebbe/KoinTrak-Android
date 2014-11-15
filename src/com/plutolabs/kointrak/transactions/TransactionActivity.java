package com.plutolabs.kointrak.transactions;

import android.app.ListActivity;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.plutolabs.kointrak.KoinTrak;
import com.plutolabs.kointrak.R;
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
        //setContentView(android.R.id.list);

        /*
        SoChain sc = new SoChainImpl();
        ArrayList<Transaction> Btrans = sc.getAddress(Network.BTC, "1BZtK1FWw2nF5mm6mFYXvbZ2z98XbPq2Lw").getTxs();

        Transaction transaction = new Transaction();
        transaction.
        AddressField[] sampleValues = new AddressField[] {
                new AddressField(Network.DOGE, address, 1.0),
                new AddressField(Network.BTC, address, 1.0),
                new AddressField(Network.LTC, address, 1.0)
        };

        // set up custom adapter
        listItems = new ArrayList<AddressField>();
        listItems.addAll(Arrays.asList(sampleValues));

        AddressArrayAdapter adapter = new AddressArrayAdapter(this, R.layout.address, listItems);
        setListAdapter(adapter);
        */
        String[] sampleAddresses = new String[] {
                "1GheiHDiAnmYo3sE5czpPbV7u5MNeUAqFC",
                //13p5iQkqBEVgKmPeJqEL2LBRS44PjX1dZL
        };
        adapter = new TransactionArrayAdapter(this, R.layout.transaction, new ArrayList<Transaction>());
        setListAdapter(adapter);

        new TransactionTask().execute(sampleAddresses);
    }

    private void updateTransactions(List<Transaction> transactions) {
        for(Transaction trans : transactions) {
            adapter.add(trans);
            adapter.notifyDataSetChanged();
        }
    }


    private class TransactionTask extends AsyncTask<String, Void, List<Transaction>> {

        @Override
        protected List<Transaction> doInBackground(String... sampleAddresses) {
            Address address = koinTrak.getAddress(Network.BTC, sampleAddresses[0]);
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
