package sample.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DefaultServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		if(req.getUserPrincipal() == null)
			req.getRequestDispatcher("/resources/Page/loginPage.jsp").forward(req, resp);
		else
			req.getRequestDispatcher("/resources/Page/index.jsp").forward(req, resp);
	}
	
	
	

}
