package sample.servlet;

import java.io.IOException;
import java.security.Principal;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import sample.auth.DummyAuthenticatorService;
import sample.auth.IAuthenticatorService;
import sample.dao.IAppDirDAO;
//import sample.dao.JDBCAppDirDAO;
import sample.dao.JNDIAppDirDAO;
import sample.exceptions.DBAccessException;
import sample.model.UserCredentials;
import sample.model.UserRole;
import weblogic.security.Security;
import weblogic.security.principal.WLSUserImpl;
import weblogic.servlet.security.ServletAuthentication;

public class LoginServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private IAuthenticatorService authenticator = new DummyAuthenticatorService();
	private IAppDirDAO DAO = new JNDIAppDirDAO();
	
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		String username = req.getParameter("user");
		String password = req.getParameter("pass");
		
		try{
			req.login(username, password);
		}
		catch(ServletException s)
		{
			forwardToLoginError(req, resp);
			return;
		}
		
		if(!req.isUserInRole("AppUser"))
		{
			forwardToLoginError(req, resp);
			req.logout();
			return;
		}
		
		
		UserCredentials user = new UserCredentials(req.getUserPrincipal().getName());
		
		// Do database query to get user information
		try
		{
			user = DAO.getUserInfo(user);
			
			if(user.hasUserRoleName("comp_user"))
				DAO.getCompanyForUser(user);
		}
		catch(DBAccessException d){
            req.setAttribute("Error", "An error has occured in retrieving user credentials. Try again later.");
			
            d.printStackTrace();
            
			req.getRequestDispatcher("/resources/Page/loginPage.jsp").forward(req, resp);
			req.logout();
			return;
		}
		
		//TODO: Figure out how to add roles from database and get rid of this hack.
		if(user.hasUserRoleName("role_admin"))
			Security.getCurrentSubject().getPrincipals().add(new WLSUserImpl("ROLEADMIN"));
		if(user.hasUserRoleName("comp_user"))
			Security.getCurrentSubject().getPrincipals().add(new WLSUserImpl("COMPANYUSER"));
		
		
		ServletAuthentication.generateNewSessionID(req);
		
		req.getSession().setAttribute("UserCred", user);
		
		req.getRequestDispatcher("/resources/Page/index.jsp").forward(req, resp);
	}
	
	private void forwardToLoginError(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		req.setAttribute("Error", "Username or Password is incorrect.");
		
		req.getRequestDispatcher("/resources/Page/loginPage.jsp").forward(req, resp);
	}

}
