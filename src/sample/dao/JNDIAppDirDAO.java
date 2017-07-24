package sample.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import sample.exceptions.DBAccessException;
import sample.model.Company;
import sample.model.Contact;
import sample.model.JobApplication;
import sample.model.Skill;
import sample.model.UserCredentials;
import sample.model.UserRole;

public class JNDIAppDirDAO implements IAppDirDAO {

	@Override
	public UserCredentials getUserInfo(UserCredentials user) throws DBAccessException {
		UserCredentials userInfo = null;
		try{
			InitialContext cont = new InitialContext();
			DataSource ds = (DataSource) cont.lookup("jdbc/applier");
			
			Connection conn = ds.getConnection();
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
		} catch (NamingException e) {
			throw new DBAccessException(e.getMessage());
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
		try {
			InitialContext cont = new InitialContext();
			DataSource ds = (DataSource) cont.lookup("jdbc/applier");
			
			Connection conn = ds.getConnection();
			
			PreparedStatement pr = null; 
			
			if(com.getId() != 0){
				// Company exists, update it
								
				pr = conn.prepareStatement("update company set name=?, city=?, statecode=? where comid=?");
				pr.setString(1, com.getName());
				pr.setString(2, com.getCity());
				pr.setString(3, new String(com.getStateCode()));
				pr.setInt(4, com.getId());
				
				pr.executeUpdate();
				pr.close();
				
				pr = conn.prepareStatement("update contact set fullname=?, phonenum=?, email=? where contactid=?");
				pr.setString(1, com.getContact().getName());
				pr.setString(2, com.getContact().getPhone());
				pr.setString(3, com.getContact().getEmail());
				pr.setInt(4, com.getContact().getId());
				
				pr.executeUpdate();
				pr.close();
				conn.commit();
				
				return;
			}
			// else
			// Company doesn't exist, create it
			
			ResultSet rs = null;
			
			pr = conn.prepareStatement("insert into company(name, city, statecode) values (?,?,?)");
			pr.setString(1, com.getName());
			pr.setString(2, com.getCity());
			pr.setString(3, new String(com.getStateCode()));
			
			pr.executeUpdate();
			
			// Assume company names are unique
			pr = conn.prepareStatement("Select comid from company where name=?");
			pr.setString(1, com.getName());
			rs = pr.executeQuery();
			
			rs.next();
			com.setId(rs.getInt("comid"));
			
			rs.close();
			
			pr = conn.prepareStatement("insert into companyuserjunction(comid, userid) values (?,?)");
			pr.setInt(1, com.getId());
			pr.setInt(2, com.getUser().getId());
			
			pr.executeUpdate();
			
			pr = conn.prepareStatement("insert into contact(fullname, phonenum, email, comid) values (?, ?, ?, ?)");
			pr.setString(1, com.getContact().getName());
			pr.setString(2, com.getContact().getPhone());
			pr.setString(3, com.getContact().getEmail());
			pr.setInt(4, com.getId());
			
			pr.executeUpdate();
			
			conn.commit();
			
			pr = conn.prepareStatement("select contactid from contact where comid = ?");
			pr.setInt(1, com.getId());
			
			rs = pr.executeQuery();
			rs.next();
			
			com.getContact().setId(rs.getInt(1));
			
		}
		catch (SQLException s) {
			throw new DBAccessException(s.getMessage());
		} catch (NamingException e) {
			throw new DBAccessException(e.getMessage());
		}
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
	public List<UserCredentials> getAllUsersAndRoles()  throws DBAccessException {
		Map<Integer, UserCredentials> userMap = new HashMap<Integer, UserCredentials>();
		List<UserCredentials> users = new ArrayList<UserCredentials>();
		try {
			InitialContext cont = new InitialContext();
			DataSource ds = (DataSource) cont.lookup("jdbc/applier");
			
			Connection conn = ds.getConnection();
			
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
		catch (SQLException s) {
			throw new DBAccessException(s.getMessage());
		} catch (NamingException e) {
			throw new DBAccessException(e.getMessage());
		}
		
		return users;
	}

	@Override
	public List<UserRole> getRoles() throws DBAccessException {
		List<UserRole> roles = new ArrayList<UserRole>();
		try {
			InitialContext cont = new InitialContext();
			DataSource ds = (DataSource) cont.lookup("jdbc/applier");
			
			Connection conn = ds.getConnection();
			PreparedStatement pre = conn.prepareStatement("Select * from userrole");
			ResultSet rs = pre.executeQuery();
			
			while(rs.next())
				roles.add(new UserRole(rs.getInt("urid"), rs.getString("rolename")));
			
			conn.close();
		}
		catch (SQLException s) {
			throw new DBAccessException(s.getMessage());
		} catch (NamingException e) {
			throw new DBAccessException(e.getMessage());
		}
		
		return roles;
	}

	@Override
	public void addRolesToUser(String username, List<String> roles) throws DBAccessException {
		try{
			InitialContext cont = new InitialContext();
			DataSource ds = (DataSource) cont.lookup("jdbc/applier");
			
			Connection conn = ds.getConnection();
			StringBuffer buf = new StringBuffer("insert into rolejunction (RJUSERID, RJROLEID)"
					+ " select userid, urid from appusers cross join USERROLE where username = ? and (rolename in(");
			
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
		} catch (NamingException e) {
			throw new DBAccessException(e.getMessage());
		}
		
	}

	@Override
	public void removeRolesFromUser(String username, List<String> roles) throws DBAccessException {
		try{
			InitialContext cont = new InitialContext();
			DataSource ds = (DataSource) cont.lookup("jdbc/applier");
			
			Connection conn = ds.getConnection();
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
		} catch (NamingException e) {
			throw new DBAccessException(e.getMessage());
		}
		
	}

	@Override
	public void getCompanyForUser(UserCredentials user) throws DBAccessException {
		try{
			InitialContext cont = new InitialContext();
			DataSource ds = (DataSource) cont.lookup("jdbc/applier");
			
			Connection conn = ds.getConnection();
			PreparedStatement ps = conn.prepareStatement("select c.comid, c.name, c.city, c.statecode, co.CONTACTID, co.FULLNAME, co.PHONENUM, co.EMAIL"
					                                  + " from company c left join companyuserjunction cuj on c.COMID = cuj.COMID left join contact co on c.COMID = co.COMID"
					                                  + " where cuj.USERID = ?");
			
			ps.setInt(1, user.getId());
			
			ResultSet rs = ps.executeQuery();
			Company com = null;
			if(rs.next()){
				Contact con = null;
				String name = rs.getString(6); // Verify we have a contact and add them.
				if(name != null)
					con = new Contact(rs.getInt(5), name, rs.getString(7), rs.getString(8));
				com = new Company(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4).toCharArray(), con);
			}
			
			user.setCompany(com);
			
			conn.close();
		}
		catch (SQLException s) {
			throw new DBAccessException(s.getMessage());
		} catch (NamingException e) {
			throw new DBAccessException(e.getMessage());
		}
		
	}

}
