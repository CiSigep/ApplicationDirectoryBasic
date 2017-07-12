package sample.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.NamingException;

import sample.exceptions.DBAccessException;
import sample.model.Company;
import sample.model.Contact;
import sample.model.JobApplication;
import sample.model.Skill;
import sample.model.UserCredentials;
import sample.model.UserRole;

public class JDBCAppDirDAO implements IAppDirDAO {

	@Override
	public UserCredentials getUserInfo(UserCredentials user) throws DBAccessException {
		UserCredentials userInfo = null;
		try {
			Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "applier", "applier");
			PreparedStatement ps = conn.prepareStatement("SELECT userid, username, urid, rolename"
					+ " FROM appusers, rolejunction, userrole"
					+ " WHERE username=? and userid = rolejunction.rjuserid and rolejunction.rjroleid = urid");
			
			ps.setString(1, user.getUsername());
			
			ResultSet rs = ps.executeQuery();
			
			if(rs.next())
			{
				userInfo = new UserCredentials(rs.getString("username"));
				userInfo.setId(rs.getInt("userid"));
				
				do {
					userInfo.addUserRole(new UserRole(rs.getInt("urid"), rs.getString("rolename")));
				} while(rs.next());
			}
			else
				throw new DBAccessException("User does not exist");
			conn.close();
		}
		catch(SQLException s){
			throw new DBAccessException(s.getMessage());
		}
		
		
		return userInfo;
	}

	@Override
	public List<JobApplication> getApplicationsForUser(UserCredentials user) throws DBAccessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Company> getCompanies(Company com) throws DBAccessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addCompany(Company com) throws DBAccessException {
		// TODO Auto-generated method stub

	}

	@Override
	public void addContact(Contact con, Company com) throws DBAccessException {
		// TODO Auto-generated method stub

	}

	@Override
	public void addUser(UserCredentials user) throws DBAccessException {
		// TODO Auto-generated method stub

	}

	@Override
	public void addJobApplication(JobApplication ja) throws DBAccessException {
		// TODO Auto-generated method stub

	}

	@Override
	public void addSkill(Skill sk) throws DBAccessException {
		// TODO Auto-generated method stub

	}

	@Override
	public List<UserCredentials> getAllUsersAndRoles() throws DBAccessException {
		Map<Integer, UserCredentials> userMap = new HashMap<Integer, UserCredentials>();
		List<UserCredentials> users = new ArrayList<UserCredentials>();
		try {
			Connection conn = DriverManager.getConnection("jdbc:oracle:@localhost:1521:xe", "applier", "applier");
			PreparedStatement pre = conn.prepareStatement("Select * from appusers order by userid");
			
			ResultSet rs = pre.executeQuery();
			while(rs.next()){
				UserCredentials user = new UserCredentials(rs.getString("username"));
				user.setId(rs.getInt("userid"));
				userMap.put(user.getId(), user);
			}
			
			pre.close();
			rs.close();
			
			pre = conn.prepareStatement("SELECT userid, urid, rolename"
					+ " FROM appusers, rolejunction, userrole"
					+ " WHERE userid = rolejunction.rjuserid and rolejunction.rjroleid = urid"
					+ " ORDER BY user, urid");
			
			rs = pre.executeQuery();
			
			while(rs.next()){
				userMap.get(rs.getInt("userid")).addUserRole(new UserRole(rs.getInt("urid"), rs.getString("rolename")));
			}
			
			
			conn.close();
			users.addAll(userMap.values());
			
		}
		catch(SQLException s){
			throw new DBAccessException(s.getMessage());
		}
		
		return users;
	}

	@Override
	public List<UserRole> getRoles() throws DBAccessException {
		List<UserRole> roles = new ArrayList<UserRole>();
		try {
			Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "applier", "applier");
			PreparedStatement pre = conn.prepareStatement("Select * from userrole");
			ResultSet rs = pre.executeQuery();
			
			while(rs.next())
				roles.add(new UserRole(rs.getInt("urid"), rs.getString("rolename")));
			
			conn.close();
		}
		catch (SQLException s) {
			throw new DBAccessException(s.getMessage());
		}
		
		return roles;
	}

	@Override
	public void addRolesToUser(String username, List<String> roles) throws DBAccessException {
		try {
			Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "applier", "applier");
			StringBuffer buf = new StringBuffer("insert into rolejunction (RJUSERID, RJROLEID)"
					+ " select userid, urid from appusers cross join USERROLE where username = ? and (rolename in (");
			
			if(roles.size() == 1) {
				buf.append("?))");
			}
			else if(roles.size() > 1) {
				for(int i = 0; i < roles.size(); i++) {
					buf.append("?");
					if(i < roles.size() - 1)
						buf.append(",");
					else
						buf.append("))");
				}
			}
			
			PreparedStatement pre = conn.prepareStatement(buf.toString());
			
			pre.setString(1, username);
			
			int j = 0;
			for(String role : roles){
				pre.setString(2 + j, role);
				j++;
			}
			
			pre.executeUpdate();
			
			conn.close();
		}
		catch (SQLException s) {
			throw new DBAccessException(s.getMessage());
		}
		
	}

	@Override
	public void removeRolesFromUser(String username, List<String> roles) throws DBAccessException {
		try {
			Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "applier", "applier");
			StringBuffer buf = new StringBuffer("delete from rolejunction"
					+ " where RJUSERID in (select userid from appusers where username=?)"
					+ " and RJROLEID in (select urid from userrole where rolename in (");
			if(roles.size() == 1) {
				buf.append("?))");
			}
			else if(roles.size() > 1) {
				for(int i = 0; i < roles.size(); i++) {
					buf.append("?");
					if(i < roles.size() - 1)
						buf.append(",");
					else
						buf.append("))");
				}
			}
			
			PreparedStatement pre = conn.prepareStatement(buf.toString());
			
			pre.setString(1, username);
			
			int j = 0;
			for(String role : roles){
				pre.setString(2 + j, role);
				j++;
			}
			
			pre.executeUpdate();
			
			conn.close();
		}
		catch (SQLException s) {
			throw new DBAccessException(s.getMessage());
		}
		
	}

	@Override
	public void getCompanyForUser(UserCredentials user) throws DBAccessException {
		
		try{
			Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "applier", "applier");
			PreparedStatement ps = conn.prepareStatement("select c.comid, name, city, statecode"
	                + " from company c, companyuserjunction cuj"
	                + " where cuj.USERID = ? and cuj.COMID = c.COMID");
	
			ps.setInt(1, user.getId());
			
			ResultSet rs = ps.executeQuery();
			Company com = null;
			if(rs.next())
				com = new Company(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4).toCharArray(), null);
			
			user.setCompany(com);
			
			conn.close();
		}
		catch (SQLException s) {
			throw new DBAccessException(s.getMessage());
		}
	}


}
