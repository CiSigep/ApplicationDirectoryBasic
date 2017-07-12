package sample.model;

import java.util.ArrayList;
import java.util.List;

public class UserCredentials {
	
	private int id;
	private String username;
	private List<UserRole> roles;
	private Company company;
	
	public UserCredentials(String username){
		this.username = username;
		this.roles = new ArrayList<UserRole>();
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public List<UserRole> getRoles() {
		return roles;
	}
	
	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public void addUserRole(UserRole role){
		roles.add(role);
	}
	
	public boolean hasUserRoleName(String role){
		for(UserRole r : roles)
		{
			if(r.getRole().equals(role))
				return true;
		}
		return false;
	}
	

}
