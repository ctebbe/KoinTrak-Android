package com.plutolabs.kointrak;

import so.chain.entity.Address;

public class RegisterStatus {

	private String message;
	private Address address;

	public RegisterStatus(String message) {
		this(message, null);
	}

	public RegisterStatus(String message, Address address) {
		this.message = message;
		this.address = address;
	}

	public String getMessage() {
		return message;
	}

	public Address getAddress() {
		return address;
	}
}
