package sample.dao;

import java.util.List;

import sample.exceptions.DBAccessException;
import sample.model.Company;
import sample.model.Contact;
import sample.model.Job;
import sample.model.JobApplication;
import sample.model.Skill;
import sample.model.UserCredentials;
import sample.model.UserRole;

public interface IAppDirDAO {
	
	// Methods for Database access go here
	
	public UserCredentials getUserInfo(UserCredentials user) throws DBAccessException;
	
	public List<JobApplication> getApplicationsForUser(UserCredentials user) throws DBAccessException;
	
	public List<Company> getCompanies(Company com)throws DBAccessException;
	
	public void addCompany(Company com) throws DBAccessException;
	
	public void addContact(Contact con, Company com) throws DBAccessException;
	
	public void addUser(UserCredentials user) throws DBAccessException;
	
	public void addJobApplication(JobApplication ja) throws DBAccessException;
	
	public void addSkill(Skill sk) throws DBAccessException;
	
	public List<UserCredentials> getAllUsersAndRoles() throws DBAccessException;
	
	public List<UserRole> getRoles() throws DBAccessException; 
	
	public void addRolesToUser(String username, List<String> roles) throws DBAccessException;
	
	public void removeRolesFromUser(String username, List<String> roles) throws DBAccessException;
	
	public void getCompanyForUser(UserCredentials user) throws DBAccessException;
	
	public void addJobForCompany(Job job, Company company) throws DBAccessException;
	
	public List<Job> getJobsForCompany(Company company) throws DBAccessException;
	
	public Job getJobById(int id) throws DBAccessException;

}
