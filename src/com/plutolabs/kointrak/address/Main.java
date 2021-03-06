package com.plutolabs.kointrak.address;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.widget.*;
import com.plutolabs.kointrak.KoinTrak;
import com.plutolabs.kointrak.R;
import com.plutolabs.kointrak.RegisterStatus;
import com.plutolabs.kointrak.api.AddressImport;
import com.plutolabs.kointrak.api.KoinTrakAPIImpl;
import com.plutolabs.kointrak.impl.KoinTrakImpl;
import com.plutolabs.kointrak.transactions.TransactionActivity;
import so.chain.entity.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Main extends ListActivity {

    public static final String CLICKED_ADDRESS = "CLICKED_ADDRESS";
    private KoinTrak koinTrak;
    private double totalWorth;

    //LIST OF ARRAY STRINGS WHICH WILL SERVE AS LIST ITEMS
    private ArrayList<AddressField> listItems;

    //DEFINING A STRING ADAPTER WHICH WILL HANDLE THE DATA OF THE LISTVIEW
    private AddressArrayAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        // set up custom adapter
        listItems = new ArrayList<AddressField>();
//        listItems.addAll(Arrays.asList(sampleValues));

        adapter = new AddressArrayAdapter(this, R.layout.address, listItems);
        setListAdapter(adapter);

        koinTrak = KoinTrakImpl.getInstance();

        ImageView addAddress = ((ImageView) findViewById(R.id.add_address));
        addAddress.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    buttonClick(v);
                }
                return false;
            }
        });

        ImageView refereshView = ((ImageView) findViewById(R.id.refresh));
        refereshView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    buttonClick(v);
                }
                return false;
            }
        });
    }

    private void inputAddressFromKoinTrakCloud(String addressKey) {
        new APITask().execute(addressKey);
    }

    public void buttonClick(View v) {
        switch(v.getId()) {
            case R.id.refresh:
                updateAllAddresses();
                break;
            case R.id.add_address:
                //Toast.makeText(v.getContext(), "add_address", Toast.LENGTH_LONG).show();
                final EditText editText = new EditText(this);
                editText.setInputType(InputType.TYPE_CLASS_NUMBER);

                new AlertDialog.Builder(this)
                        .setTitle("KoinTrak address key entry")
                        .setMessage("Enter KoinTrak.com address key:")
                        .setView(editText)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                String addressKey = editText.getText().toString();
                                inputAddressFromKoinTrakCloud(addressKey);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                            }
                        })
                        .show();

                break;
            default:
        }
    }

    public void registerAddress(View view, ArrayList<String> addresses) {
        String[] sampleAddresses = new String[] {
                //"1AhTjUMztCihiTyA4K6E3QEpobjWLwKhkR"
                "1AhTjUMztCihiTyA4K6E3QEpobjWLwKhkR","1KFHE7w8BhaENAswwryaoccDb6qcT6DbYY","16sNuq9KhPqypikeaLDQiQKTj1qekGrYf9","13p5iQkqBEVgKmPeJqEL2LBRS44PjX1dZL","1GZwCeoCyU7brDfayRiXLP6bnVDwyvaegP","1M4vL7mvrKcG2mHas11snsbpktXtqNYJiD","1CjPR7Z5ZSyWk6WtXvSFgkptmpoi4UM9BC","LTsUmTPBKV5U7pjhSjPC9fV5puyC3esiHP","LRWghxxUVo8vDZ6jf3uNUeqD35hPL1Xvem","LXYZ7kotvjodt1bd2CbMPi24ktZwBMcrpv","LfHsbL5kvyrKHZyk391B4tfay6uAyMrhxr","D8EyEfuNsfQ3root9R3ac54mMcLmoNBW6q","DDTtqnuZ5kfRT5qh2c7sNtqrJmV3iXYdGG","D6defLv3odMN5wmUyMxMzyDKJ3SRYR6Nqr","DMqRVLrhbam3Kcfddpxd6EYvEBbpi3bEpP","DEaSQxoeQarU6Si3uJp2rjfqN7b6UxkS9W"
        };
//        for(String Addr: sampleAddresses){
//            new RegisterWalletTask().execute(Addr);
//        }
        for (String Addr : addresses) {
            new RegisterWalletTask().execute(Addr);
            Toast toast = Toast.makeText(this,"Adding "+Addr+" from KoinTrak cloud.", Toast.LENGTH_SHORT);
            toast.show();
        }

    }

    public void updateAllAddresses() {
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
        TextView tv = (TextView) findViewById(R.id.total_txt);
        tv.setText(String.format("$%.2f USD", totalWorth));
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
        updateTotalWorth();
    }

    private void calculateTotalWorth(List<PriceQuery> rates) {
        double totalWorth = 0.0;
        if (rates != null) {
            for (PriceQuery query : rates) {
                ArrayList<Price> differentExchanges = query.getPrices();
                if (differentExchanges.size() > 0) {
                    // grab the first one for now
                    Double exchangeRate = Double.valueOf(differentExchanges.get(0).getPrice());
                    double coinSum = calculateTotalCoins(query.getNetwork());
                    totalWorth += exchangeRate * coinSum;
                }
            }
        }
        this.totalWorth = totalWorth;
        updateTotalWorth();
    }

    private double calculateTotalCoins(Network network) {
        double balance = 0;
        for (AddressField field : listItems) {
            if (field.getIcon().equals(network)) {
                balance += field.getBalance();
            }
        }
        return balance;
    }


    public void switchToTransactionActivity(AddressField addressField) {
        Intent intent = new Intent(this, TransactionActivity.class);
//        EditText editText = (EditText) findViewById(R.id.edit_message);
//        String message = editText.getText().toString();
        intent.putExtra(CLICKED_ADDRESS, addressField);
        startActivity(intent);
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
            return rates;
        }

        @Override
        protected void onPostExecute(List<PriceQuery> rates) {
            calculateTotalWorth(rates);
        }

    }

    private class APITask extends AsyncTask<String, Void, AddressImport> {

        @Override
        protected AddressImport doInBackground(String... inputAddress) {
            KoinTrakAPIImpl koinAPI = new KoinTrakAPIImpl();
            try {
                AddressImport addr = koinAPI.getAddressFromCode(inputAddress[0]);
                if (addr != null) {
                    return addr;
                }
                return null;
            } catch (Exception err){
                return null;
            }
        }

        @Override
        protected void onPostExecute(AddressImport addrin) {
            registerAddress(null,addrin.getData());
        }

    }}
