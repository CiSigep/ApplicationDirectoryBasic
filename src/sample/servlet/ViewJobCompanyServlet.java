package sample.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import sample.dao.IAppDirDAO;
import sample.dao.JNDIAppDirDAO;
import sample.exceptions.DBAccessException;
import sample.model.Job;

public class ViewJobCompanyServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private IAppDirDAO dao = new JNDIAppDirDAO();
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			Job job = dao.getJobById(Integer.parseInt(req.getParameter("jobid")));
			
			req.setAttribute("Job", job);
			
		} catch (DBAccessException e) {
			throw new ServletException(e.getMessage());
		}
		
		
		req.getRequestDispatcher("/resources/Page/companyJob.jsp").forward(req, resp);
	}
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doPost(req, resp);
	}
	
	

}
