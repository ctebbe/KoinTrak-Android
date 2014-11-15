package com.plutolabs.kointrak.address;

import so.chain.entity.Address;
import so.chain.entity.Network;

/**
 * @author ct.
 */
public class AddressField {

    final Network icon;
    final Address address;
    final double balance;

    public AddressField(Network network, Address address, double balance) {
        this.icon = network;
        this.address = address;
        this.balance = balance;
    }
}
