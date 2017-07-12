package sample.model;

import java.util.List;

public class Company {
	private int id;
	private String name;
	private String city;
	private char[] stateCode;
	private UserCredentials user;
	private List<Contact> contacts;
	
	public Company(int id, String name, String city, char[] stateCode, List<Contact> contacts){
		this.id = id;
		this.name = name;
		this.city = city;
		this.stateCode = stateCode;
		this.contacts = contacts;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public String getCity() {
		return city;
	}

	public char[] getStateCode() {
		return stateCode;
	}

	public List<Contact> getContacts() {
		return contacts;
	}

	public UserCredentials getUser() {
		return user;
	}

	public void setUser(UserCredentials user) {
		this.user = user;
	}
	
	

}
