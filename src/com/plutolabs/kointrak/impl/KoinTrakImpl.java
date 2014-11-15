package com.plutolabs.kointrak.impl;

import com.plutolabs.kointrak.KoinTrak;
import com.plutolabs.kointrak.RegisterStatus;
import io.shapeshift.api.ShapeShiftImpl;
import so.chain.impl.SoChainImpl;
import so.chain.entity.*;
import so.chain.impl.SoChainImpl;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class KoinTrakImpl implements KoinTrak {

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
    public RegisterStatus registerWallet(String address) {
        Network network = isValidAddress(address);
        if (network == null) {
            return new RegisterStatus(createAddressNotInSupportedNetworkErrorMessage(address));
        }
        else {
            return registerWallet(network, address);
        }
    }

    public Address getAddress(Network network, String addressString) {
        try {
            return soChain.getAddress(network, addressString);
        } catch (IOException e) {}
        return null;
    }

    @Override
    public AddressBalance getAddressBalance(Network network, String address) {
        try {
            return soChain.getAddressBalance(network, address);
        } catch (IOException e) {
            return null;
        }
    }
    @Override
    public AddressBalance getAddressBalance(Network network, Address address) {
       return getAddressBalance(network, address.getAddress());
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
        for (Network network : Network.values()) {
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

    private RegisterStatus registerWallet(Network network, String addressString) {
        Address address = getAddress(network, addressString);

        if (address != null) {
            Set<Address> registeredAddresses = this.registeredAddresses.get(network);
            if (registeredAddresses == null) {
                registeredAddresses = new HashSet<Address>();
            }
            registeredAddresses.add(address);
            return new RegisterStatus(createSuccessfulRegisterMessage(network, addressString), address);
        }
        else {
            return new RegisterStatus(createUnknownErrorMessage(network, addressString));
        }
    }

    private String createSuccessfulRegisterMessage(Network network, String addressString) {
        return network + " Wallet Address '" + addressString + "\' was successfully registered.";
    }

    private String createAddressNotInSupportedNetworkErrorMessage(String address) {
        return "Wallet Address \'" + address + "\' " + " is not a valid address on supported networks: " + prettyPrintArray(Network.values());
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
            builder.append(delim + network.getName());
        }
        builder.append("]");
        return builder.toString().replaceFirst(delim, "");
    }

}
