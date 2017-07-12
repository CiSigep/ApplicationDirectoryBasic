package sample.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import sample.dao.IAppDirDAO;
import sample.dao.JNDIAppDirDAO;
import sample.exceptions.DBAccessException;
import sample.model.UserCredentials;
import sample.model.UserRole;

public class RoleServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7421938155495129385L;
	private IAppDirDAO dao = new JNDIAppDirDAO();
	
	
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		try {
			List<UserRole> roles = dao.getRoles();
			List<UserCredentials> users = dao.getAllUsersAndRoles();
			
			StringBuffer buf = new StringBuffer("{ \"roles\":[");
			for(int i = 0; i < roles.size(); i++){
				buf.append("{ \"roleid\": " + roles.get(i).getId() +", \"rolename\": \"" + roles.get(i).getRole() + "\" }");
				if(i < roles.size() - 1)
					buf.append(", ");
			}
			buf.append("], \"users\":[");
			for(int i = 0; i < users.size(); i++){
				buf.append(" { \"userid\": " + users.get(i).getId() + ", \"username\": \"" +users.get(i).getUsername() + "\"" );
				if(users.get(i).getRoles().size() > 0){
					buf.append(", \"userroles\": [");
					for(int j = 0; j < users.get(i).getRoles().size(); j++)	{
						buf.append("{ \"roleid\": " + users.get(i).getRoles().get(j).getId() +", \"rolename\": \"" +users.get(i).getRoles().get(j).getRole() + "\" }");
						if(j < users.get(i).getRoles().size() - 1)
							buf.append(", ");
						else
							buf.append("]");
					}
				}
				buf.append("}");
				if(i < users.size() - 1)
					buf.append(", ");
				else
					buf.append("]}");
			}
			
			res.addHeader("Content-Type", "application/json");
			res.getWriter().write(buf.toString());
			
			
		} catch (DBAccessException e) {
			e.printStackTrace();
		}
	}


	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String user = req.getParameter("user");
		String rolesAdded = req.getParameter("add");
		String rolesRemoved = req.getParameter("remove");
		
		
		if(rolesAdded != null){
			List<String> rolesToAdd = Arrays.asList(rolesAdded.split(","));
			
			try {
				dao.addRolesToUser(user, rolesToAdd);
			} catch (DBAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(rolesRemoved != null){
			List<String> rolesToRemove = Arrays.asList(rolesRemoved.split(","));
			
			try {
				dao.removeRolesFromUser(user, rolesToRemove);
			} catch (DBAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if(rolesAdded == null && rolesRemoved == null){
			resp.getWriter().write("No changes were made");
			return;
		}
		
		
		resp.getWriter().write("Changes complete");
	}
	
	
	

}
