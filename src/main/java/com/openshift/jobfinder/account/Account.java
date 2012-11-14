package com.openshift.jobfinder.account;

public class Account {

	private final String username;

	private final String password;

	private final String firstName;

	private final String lastName;
	
	private final String address;

	public Account(String username, String password, String firstName, String lastName,String address) {
		this.username = username;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.address = address;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}
	
	public String getAddress() {
		return address;
	}
}
