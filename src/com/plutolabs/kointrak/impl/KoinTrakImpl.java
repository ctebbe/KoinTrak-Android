package com.plutolabs.kointrak.impl;

import com.plutolabs.kointrak.KoinTrak;
import io.shapeshift.api.ShapeShiftImpl;
import so.chain.SoChainImpl;
import so.chain.entity.*;
import so.chain.entity.Currency;

import java.io.IOException;
import java.util.*;

public class KoinTrakImpl implements KoinTrak {

    // TODO remove *TEST networks from Network enum in SoChain. For now, just iterate over POSSIBLE_NETWORKS.
    public static final Network[] POSSIBLE_NETWORKS = new Network[] { Network.BTC, Network.DOGE, Network.LTC };

    private static KoinTrakImpl instance;

    private final SoChainImpl soChain;
    private final ShapeShiftImpl shapeShift;

    // TODO we probably need a concurrent map
    private final Map<Network, Set<Address>> registeredAddresses;

    public synchronized static KoinTrak getInstance() {
        if (instance == null) {
            instance = new KoinTrakImpl();
        }
        return instance;
    }

    private KoinTrakImpl() {
        soChain = SoChainImpl.getInstance();
        shapeShift = ShapeShiftImpl.getInstance();

        registeredAddresses = new HashMap<Network, Set<Address>>();
    }


    @Override
    public String registerWallet(String address) {
        Network network = isValidAddress(address);
        if (network == null) {
            return createAddressNotInSupportedNetworkErrorMessage(address);
        }
        else {
            return registerWallet(network, address);
        }
    }

    @Override
    public AddressBalance getAddressBalance(Network network, Address address) {
        try {
            return soChain.getAddressBalance(network, address.getAddress());
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public PriceQuery getExchangeRate(Network network) {
        try {
            return soChain.getPrice(network, Currency.USD);
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public Map<Network, Set<Address>> getRegisteredAddresses() {
        return registeredAddresses;
    }

    private Network isValidAddress(String address) {
        Network validNetwork = null;
        for (Network network : POSSIBLE_NETWORKS) {
            try {
                AddressValid addressValid = soChain.getAddressValid(network, address);
                if (addressValid.isValid()) {
                    validNetwork = addressValid.getNetwork();
                    break;
                }
            } catch (IOException e) {}
        }

        return validNetwork;
    }

    private String registerWallet(Network network, String addressString) {
        Address address = null;
        try {
            address = soChain.getAddress(network, addressString);
        } catch (IOException e) {
            return createUnknownErrorMessage(network, addressString);
        }

        if (address != null) {
            Set<Address> registeredAddresses = this.registeredAddresses.get(network);
            if (registeredAddresses == null) {
                registeredAddresses = new HashSet<Address>();
            }
            registeredAddresses.add(address);
            return network + " Wallet Address '" + addressString + "\' was successfully registered.";
        }
        else {
            return createUnknownErrorMessage(network, addressString);
        }
    }

    private String createAddressNotInSupportedNetworkErrorMessage(String address) {
        return "Wallet Address \'" + address + "\' " + " is not a valid address on supported networks: " + prettyPrintArray(POSSIBLE_NETWORKS);
    }

    /**
     * unknown error, sue us
     */
    private String createUnknownErrorMessage(Network network, String address) {
        return "Whoops, error registering " + network + " Wallet Address '" + address + ". Try again...";
    }

    private static String prettyPrintArray(Network[] networks) {
        String delim = ", ";
        StringBuilder builder = new StringBuilder("[");
        for (Network network : networks) {
            // TODO change to network.getName() once jar is rebuilt.
            builder.append(delim + network.toString());
        }
        builder.append("]");
        return builder.toString().replaceFirst(delim, "");
    }

}
