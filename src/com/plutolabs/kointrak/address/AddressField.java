package com.plutolabs.kointrak.address;

import so.chain.entity.Address;
import so.chain.entity.Network;

import java.io.Serializable;

/**
 * @author ct.
 */
public class AddressField implements Serializable {

    private final Network icon;
    private final String address;
    private double balance;

    public AddressField(Network network, String address, double balance) {
        this.icon = network;
        this.address = address;
        this.balance = balance;
    }

    public Network getIcon() {
        return icon;
    }

    public String getAddress() {
        return address;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

}
