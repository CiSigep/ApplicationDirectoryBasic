package sample.model;

public class Company {
	private int id;
	private String name;
	private String city;
	private char[] stateCode;
	private UserCredentials user;
	private Contact contact;
	
	public Company(int id, String name, String city, char[] stateCode, Contact contact){
		this.id = id;
		this.name = name;
		this.city = city;
		this.stateCode = stateCode;
		this.contact = contact;
	}

	public int getId() {
		return id;
	}

	public Contact getContact() {
		return contact;
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


	public UserCredentials getUser() {
		return user;
	}

	public void setUser(UserCredentials user) {
		this.user = user;
	}
	
	

}
