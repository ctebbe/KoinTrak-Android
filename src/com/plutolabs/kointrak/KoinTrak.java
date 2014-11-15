package com.plutolabs.kointrak;

import so.chain.entity.Address;
import so.chain.entity.AddressBalance;
import so.chain.entity.Network;
import so.chain.entity.PriceQuery;

import java.util.Map;
import java.util.Set;

public interface KoinTrak {

    public String registerWallet(String address);

    public AddressBalance getAddressBalance(Network network, Address address);

    public PriceQuery getExchangeRate(Network network);

    public Map<Network, Set<Address>> getRegisteredAddresses();

}
