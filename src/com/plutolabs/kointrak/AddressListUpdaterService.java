package com.plutolabs.kointrak;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import com.plutolabs.kointrak.impl.KoinTrakImpl;
import so.chain.entity.Address;
import so.chain.entity.AddressBalance;
import so.chain.entity.Network;

import java.util.Map;
import java.util.Set;

public class AddressListUpdaterService extends IntentService {

    public static final String UPDATE_ADDRESS_ENTRY_BROADCAST_ACTION = "com.plutolabs.kointrak.update.address.entry.action";
    public static final String UPDATE_ADDRESS_ENTRY_BROADCAST_KEY = "com.plutolabs.kointrak.update.address.entry.key";

    private KoinTrak koinTrak;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public AddressListUpdaterService(String name) {
        super(name);
        koinTrak = KoinTrakImpl.getInstance();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Map<Network, Set<Address>> registeredAddresses = koinTrak.getRegisteredAddresses();
        for (Map.Entry<Network, Set<Address>> entry : registeredAddresses.entrySet()) {
            Network network = entry.getKey();
            for (Address address : entry.getValue()) {
                AddressBalance addressBalance = koinTrak.getAddressBalance(network, address);
                sendBroadcast(addressBalance);
            }
        }
    }

    private void sendBroadcast(AddressBalance addressBalance) {
        Intent intent = new Intent(UPDATE_ADDRESS_ENTRY_BROADCAST_ACTION);
        intent.putExtra(UPDATE_ADDRESS_ENTRY_BROADCAST_KEY, addressBalance);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

}
