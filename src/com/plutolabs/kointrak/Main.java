package com.plutolabs.kointrak;

import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.plutolabs.kointrak.address.AddressArrayAdapter;
import com.plutolabs.kointrak.address.AddressField;
import com.plutolabs.kointrak.impl.KoinTrakImpl;
import so.chain.entity.Address;
import so.chain.entity.AddressBalance;
import so.chain.entity.Network;

import java.util.ArrayList;
import java.util.Arrays;

public class Main extends ListActivity {

    private KoinTrak koinTrak;

    //LIST OF ARRAY STRINGS WHICH WILL SERVE AS LIST ITEMS
    private ArrayList<AddressField> listItems;

    //DEFINING A STRING ADAPTER WHICH WILL HANDLE THE DATA OF THE LISTVIEW
    private AddressArrayAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(android.R.id.list);

//        Address address = new Address();
//        address.setAddress("1BZtK1FWw2nF5mm6mFYXvbZ2z98XbPq2Lw");
//        AddressField[] sampleValues = new AddressField[] {
//            new AddressField(Network.DOGE, address, 1.0),
//            new AddressField(Network.BTC, address, 1.0),
//            new AddressField(Network.LTC, address, 1.0)
//        };

        // set up custom adapter
        listItems = new ArrayList<AddressField>();
//        listItems.addAll(Arrays.asList(sampleValues));

        AddressArrayAdapter adapter = new AddressArrayAdapter(this, R.layout.address, listItems);
        setListAdapter(adapter);

        koinTrak = KoinTrakImpl.getInstance();
        registerAddress(null);
    }

    public void registerAddress(View view) {
        String[] sampleAddresses = new String[] {
                "16sNuq9KhPqypikeaLDQiQKTj1qekGrYf9",
                "13p5iQkqBEVgKmPeJqEL2LBRS44PjX1dZL",
                "DMqRVLrhbam3Kcfddpxd6EYvEBbpi3bEpP",
                "LTsUmTPBKV5U7pjhSjPC9fV5puyC3esiHP"
        };
        for (String sampleAddress : sampleAddresses) {
            RegisterStatus status = koinTrak.registerWallet(sampleAddress);
            // display message to user
            Address address = status.getAddress();
            if (address != null) {
                createAddressListEntry(address);
            }
        }
    }

    private synchronized void createAddressListEntry(Address address) {
        Network network = address.getNetwork();
        AddressBalance addressBalance = koinTrak.getAddressBalance(network, address);
        double confirmedBalance = Double.valueOf(addressBalance.getConfirmedBalance());
        AddressField field = new AddressField(network, address, confirmedBalance);
        adapter.add(field);
        adapter.notifyDataSetChanged();
        updateTotalAssets(network, confirmedBalance);
    }

    private void updateTotalAssets(Network network, double confirmedBalance) {
        // TODO update total assets box
    }

    // Broadcast receiver for receiving status updates from the IntentService
    private class ResponseReceiver extends BroadcastReceiver {

        private ResponseReceiver() {
        }

        // Called when the BroadcastReceiver gets an Intent it's registered to receive
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(AddressListUpdaterService.UPDATE_ADDRESS_ENTRY_BROADCAST_ACTION)) {
                AddressBalance balance = (AddressBalance) intent.getSerializableExtra(AddressListUpdaterService.UPDATE_ADDRESS_ENTRY_BROADCAST_KEY);
                // TODO find and update the entry corresponding to this address and network
                for (AddressField field : listItems) {
                    if (field.getIcon().equals(balance.getNetwork())
                            && field.getAddress().getAddress().equals(balance.getAddress())) {
                        double confirmedBalance = Double.valueOf(balance.getConfirmedBalance());
                        field.setBalance(confirmedBalance);
                        adapter.notifyDataSetChanged();
                        break;
                    }
                }
            }
        }

    }

}