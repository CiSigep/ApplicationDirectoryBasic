package sample.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import sample.dao.IAppDirDAO;
import sample.dao.JNDIAppDirDAO;
import sample.exceptions.DBAccessException;
import sample.model.Company;
import sample.model.UserCredentials;

public class CompanyServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private IAppDirDAO dao = new JNDIAppDirDAO();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doGet(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String name = req.getParameter("name");
		String city = req.getParameter("city");
		char[] stateCode = req.getParameter("state").toCharArray();
		
		Company com = new Company(0, name, city, stateCode, null);
		com.setUser((UserCredentials) req.getSession().getAttribute("UserCred"));
		
		try {
			dao.addCompany(com);
		} catch (DBAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	
	
	
}
