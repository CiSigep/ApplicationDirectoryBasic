package sample.model;

public class UserRole {
	
	int id;
	private String role;
	
	public UserRole(int id, String role){
		this.id = id;
		this.role = role;
	}

	public int getId() {
		return id;
	}

	public String getRole() {
		return role;
	}

}
