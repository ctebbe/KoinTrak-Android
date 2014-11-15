package com.plutolabs.kointrak;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import so.chain.entity.Address;
import so.chain.entity.Network;

public class Main extends ListActivity {
    /**
     * Called when the activity is first created.
     */
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
        ArrayAdapter<AddressField> adapter = new ArrayAdapter<AddressField>(this,R.layout.address,values);
        setListAdapter(adapter);
        setContentView(R.layout.main);
    }

    @Override protected void onListItemClick(ListView lv, View v, int position, long id) {
        String item = (String) getListAdapter().getItem(position);
        Toast.makeText(this, item, Toast.LENGTH_LONG).show();
    }
}