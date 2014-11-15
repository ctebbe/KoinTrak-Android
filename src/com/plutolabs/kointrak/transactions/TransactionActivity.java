package com.plutolabs.kointrak.transactions;

import android.app.ListActivity;
import android.os.Bundle;

/**
 * @author ct.
 */
public class TransactionActivity extends ListActivity {
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
    }
}
