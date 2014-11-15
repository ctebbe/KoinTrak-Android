package com.plutolabs.kointrak.address;

import so.chain.entity.Address;
import so.chain.entity.Network;

/**
 * @author ct.
 */
public class AddressField {

    private final Network icon;
    private final Address address;
    private double balance;

    public AddressField(Network network, Address address, double balance) {
        this.icon = network;
        this.address = address;
        this.balance = balance;
    }

    public Network getIcon() {
        return icon;
    }

    public Address getAddress() {
        return address;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

}
