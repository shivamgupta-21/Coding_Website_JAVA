package com.fc.project;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Servlet implementation class login
 */
@WebServlet("/login")
public class login extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
   
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	String uemail = request.getParameter("username");
	String upwd= request.getParameter("password");
	
	System.out.println("Email: " + uemail);
	System.out.println("Name: " + upwd);

	HttpSession session= request.getSession();
	RequestDispatcher dispatcher= null;
	
	if(uemail==null || uemail.equals("")) {
		request.setAttribute("status","invalidEmail" );
    	dispatcher= request.getRequestDispatcher("login.jsp");
    	dispatcher.forward(request, response);
       }

	if(upwd==null || upwd.equals("")) {
		request.setAttribute("status","invalidPassword" );
    	dispatcher= request.getRequestDispatcher("login.jsp");
    	dispatcher.forward(request, response);
       }
		

	try {
		Class.forName("com.mysql.cj.jdbc.Driver");

    	Connection connection  = DriverManager.getConnection("jdbc:mysql://localhost:3306/java_project", "root", "Shiv@m21");

        PreparedStatement pst = connection.prepareStatement("select * from details where uemail=? and upwd=?");
        pst.setString(1, uemail);
        pst.setString(2, upwd);
        
       ResultSet rs = pst.executeQuery();
       if(rs.next()) {
    	session.setAttribute("name", rs.getString("uname"));
    	dispatcher= request.getRequestDispatcher("index.jsp");
       }else {
    	   request.setAttribute("status", "failed");
    	   dispatcher= request.getRequestDispatcher("login.jsp");
       }
       dispatcher.forward(request, response);
       
	} catch (Exception e) {
		// TODO: handle exception
	}
	}

}
