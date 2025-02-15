package com.fc.project;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@WebServlet(urlPatterns = "/Join")
public class signup extends HttpServlet {
	private static final long serialVersionUID = 1L;


	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String uname = request.getParameter("username");
		String uemail = request.getParameter("email");
		String upwd = request.getParameter("password");
		String reUpwd = request.getParameter("re_pass");
		String umobile = request.getParameter("contact");

		System.out.println("Email: " + uemail);
		System.out.println("Name: " + uname);
		System.out.println("Contact: "+umobile);
		RequestDispatcher dispatcher = null;
		Connection connection = null;
		if(uname==null || uname.equals("")) {
			request.setAttribute("status","invalidName" );
	    	dispatcher= request.getRequestDispatcher("login.jsp");
	    	dispatcher.forward(request, response);
	       }
		if(uemail==null || uemail.equals("")) {
			request.setAttribute("status","invalidEmail" );
	    	dispatcher= request.getRequestDispatcher("registration.jsp");
	    	dispatcher.forward(request, response);
	       }
		if(upwd==null || upwd.equals("")) {
			request.setAttribute("status","invalidPassword" );
	    	dispatcher= request.getRequestDispatcher("registration.jsp");
	    	dispatcher.forward(request, response);
	    	
	       }else if(!upwd.equals(reUpwd)) {
	    	   request.setAttribute("status","invalidConfirmPassword" );
		    	dispatcher= request.getRequestDispatcher("registration.jsp");
		    	dispatcher.forward(request, response);
	    	   	}
		
		if(umobile==null || umobile.equals("")) {
			request.setAttribute("status","invalidMobileNumber" );
	    	dispatcher= request.getRequestDispatcher("login.jsp");
	    	dispatcher.forward(request, response);
	       }
		else if (umobile.length()>10) {
			request.setAttribute("status","invalidMobileNumberLength" );
	    	dispatcher= request.getRequestDispatcher("registration.jsp");
	    	dispatcher.forward(request, response);
		}
	
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");

			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/java_project", "root", "Shiv@m21");

			PreparedStatement pst = connection
					.prepareStatement("insert into details(uname,upwd,uemail,umobile) values (?,?,?,?)");

			pst.setString(1, uname);
			pst.setString(2, upwd);
			pst.setString(3, uemail);
			pst.setString(4, umobile);
			int rowCount = pst.executeUpdate();
			dispatcher = request.getRequestDispatcher("registration.jsp");
			if (rowCount > 0) {
				request.setAttribute("status", "success");
			} else {
				request.setAttribute("status", "failed");
			}
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("status", "failed");
			dispatcher = request.getRequestDispatcher("error.jsp");

		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		if (dispatcher != null) {
			dispatcher.forward(request, response);
		}
	}
}