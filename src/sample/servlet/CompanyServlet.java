package sample.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import sample.dao.IAppDirDAO;
import sample.dao.JNDIAppDirDAO;
import sample.exceptions.DBAccessException;
import sample.model.Company;
import sample.model.Contact;
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
		int comid = Integer.parseInt(req.getParameter("comId"));
		String name = req.getParameter("name");
		String city = req.getParameter("city");
		char[] stateCode = req.getParameter("state").toCharArray();
		int contid = Integer.parseInt(req.getParameter("contId"));
		String conName = req.getParameter("conName");
		String conPhone = req.getParameter("conPhone");
		String conMail = req.getParameter("conMail");
		
		Contact cont = new Contact(contid, conName, conPhone, conMail);
		
		Company com = new Company(comid, name, city, stateCode, cont);
		com.setUser((UserCredentials) req.getSession().getAttribute("UserCred"));
		
		try {
			dao.addCompany(com);
		} catch (DBAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		PrintWriter pw = resp.getWriter();
		
		// Company and Contact objects' ids get updated, so they can be passed back
		pw.write("{ \"com\": " +  com.getId() + ", \"cont\": " + com.getContact().getId() + " }");
	}
	
	
	
	
	
}
