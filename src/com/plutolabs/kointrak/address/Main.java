package com.plutolabs.kointrak.address;

import android.app.ListActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import com.plutolabs.kointrak.KoinTrak;
import com.plutolabs.kointrak.R;
import com.plutolabs.kointrak.RegisterStatus;
import com.plutolabs.kointrak.impl.KoinTrakImpl;
import so.chain.entity.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

        adapter = new AddressArrayAdapter(this, R.layout.address, listItems);
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
            new RegisterWalletTask().execute(sampleAddress);
        }
    }

    public void updateAllAddresses(View view) {
        Map<Network, Set<Address>> registeredAddresses = koinTrak.getRegisteredAddresses();
        for (Map.Entry<Network, Set<Address>> entry : registeredAddresses.entrySet()) {
            for (Address address : entry.getValue()) {
                new UpdateWalletTask().execute(address);
            }
        }
    }

    private synchronized void createAddressListEntry(AddressBalance addressBalance) {
        Network network = addressBalance.getNetwork();

        double confirmedBalance = Double.valueOf(addressBalance.getConfirmedBalance());
        AddressField field = new AddressField(network, addressBalance.getAddress(), confirmedBalance);
        adapter.add(field);
        adapter.notifyDataSetChanged();
        updateTotalWorth();
    }

    private void updateTotalWorth() {
        new CalculateTotalWorthTask().execute();
    }

    private void updateAddressBalance(AddressBalance balance) {
        for (AddressField field : listItems) {
            if (field.getIcon().equals(balance.getNetwork())
                    && field.getAddress().equals(balance.getAddress())) {
                double confirmedBalance = Double.valueOf(balance.getConfirmedBalance());
                field.setBalance(confirmedBalance);
                adapter.notifyDataSetChanged();
                break;
            }
        }
    }

    private void calculateTotalWorth(List<PriceQuery> rates) {
        double totalWorth = 0.0;
        for (PriceQuery query : rates) {
            ArrayList<Price> differentExchanges = query.getPrices();
            if (differentExchanges.size() > 0) {
                // grab the first one for now
                totalWorth += Double.valueOf(differentExchanges.get(0).getPrice());
            }
        }

        // TODO update the actual field but it does not exist yet
    }

    private class RegisterWalletTask extends AsyncTask<String, Void, AddressBalance> {

        @Override
        protected AddressBalance doInBackground(String... sampleAddresses) {
            RegisterStatus status = koinTrak.registerWallet(sampleAddresses[0]);
            // display message to user
            Address address = status.getAddress();
            if (address != null) {
                AddressBalance addressBalance = koinTrak.getAddressBalance(address.getNetwork(), address);
                return addressBalance;
            }
            return null;
        }

        @Override
        protected void onPostExecute(AddressBalance balance) {
            createAddressListEntry(balance);
        }

    }

    private class UpdateWalletTask extends AsyncTask<Address, Void, AddressBalance> {

        @Override
        protected AddressBalance doInBackground(Address... addresses) {
            Address address = addresses[0];
            AddressBalance addressBalance = koinTrak.getAddressBalance(address.getNetwork(), address);
            return addressBalance;
        }

        @Override
        protected void onPostExecute(AddressBalance balance) {
            updateAddressBalance(balance);
        }

    }

    private class CalculateTotalWorthTask extends AsyncTask<Void, Void, List<PriceQuery>> {

        @Override
        protected List<PriceQuery> doInBackground(Void... ignore) {
            Network[] values = Network.values();
            ArrayList rates = new ArrayList(values.length);
            for (Network network : values) {
                PriceQuery exchangeRate = koinTrak.getExchangeRate(network);
                rates.add(exchangeRate);
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<PriceQuery> rates) {
            calculateTotalWorth(rates);
        }

    }

}
