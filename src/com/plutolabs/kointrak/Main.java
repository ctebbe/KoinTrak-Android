package com.plutolabs.kointrak;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;
import com.plutolabs.kointrak.impl.KoinTrakImpl;
import so.chain.entity.Address;
import so.chain.entity.AddressBalance;
import so.chain.entity.Network;

public class Main extends ListActivity {

    private KoinTrak koinTrak;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Address address = new Address();
        address.setAddress("test address");
        AddressField[] values = new AddressField[] {
            new AddressField(Network.DOGE, address, 1.0),
            new AddressField(Network.BTC, address, 1.0),
            new AddressField(Network.LTC, address, 1.0)
        };
        AddressArrayAdapter adapter = new AddressArrayAdapter(this,R.layout.address,values);
        setListAdapter(adapter);
        koinTrak = KoinTrakImpl.getInstance();
    }

    @Override protected void onListItemClick(ListView lv, View v, int position, long id) {
        String item = (String) getListAdapter().getItem(position);
        Toast.makeText(this, item, Toast.LENGTH_LONG).show();
    }

    public void registerAddress(View view) {
        String addressString = null; // get address input
        RegisterStatus status = koinTrak.registerWallet(addressString);
        // display message to user
        Address address = status.getAddress();
        if (address != null) {
            createAddressListEntry(address);
        }
    }

    private void createAddressListEntry(Address address) {
        AddressBalance addressBalance = koinTrak.getAddressBalance(address.getNetwork(), address);
        // create new list entry
        // update image box
        // update address field
        String balance = addressBalance.getConfirmedBalance() + " " + addressBalance.getNetwork(); // update balance box
    }

}