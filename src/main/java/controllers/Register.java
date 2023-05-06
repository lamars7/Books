package controllers;

import java.io.*;
import java.sql.*;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import org.mindrot.jbcrypt.BCrypt;

@WebServlet("/Register")
public class Register extends HttpServlet {
  
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
// JDBC driver and database URL
  static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
  static final String DB_URL = "jdbc:mysql://mudfoot.doc.stu.mmu.ac.uk:6306/safilama";

  // Database credentials
  static final String USER = "safilama";
  static final String PASS = "fingsteR2";

  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	  // Get user information from form
	  String name = request.getParameter("name");
	  String email = request.getParameter("email");
	  String password = request.getParameter("password");
	  
	  // Hash the password using BCrypt
	  String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

	  // Initialize JDBC objects
	  Connection conn = null;
	  PreparedStatement stmt = null;
	  ResultSet rs = null;
	  
	  try {
	    // Register JDBC driver
	    Class.forName("com.mysql.jdbc.Driver");

	    // Open a connection
	    conn = DriverManager.getConnection(DB_URL, USER, PASS);

	    // Check if email already exists in database
	    String checkEmailSql = "SELECT * FROM users WHERE email=?";
	    PreparedStatement checkEmailStmt = conn.prepareStatement(checkEmailSql);
	    checkEmailStmt.setString(1, email);
	    rs = checkEmailStmt.executeQuery();

	    if (rs.next()) {
	      // Email already exists in database
	      response.setStatus(HttpServletResponse.SC_CONFLICT);
	      response.getWriter().write("Email already exists.");
	    } else {
	      // Execute SQL query to insert user into database
	      String sql = "INSERT INTO users (name, email, password) VALUES (?, ?, ?)";
	      stmt = conn.prepareStatement(sql);
	      stmt.setString(1, name);
	      stmt.setString(2, email);
	      stmt.setString(3, hashedPassword);
	      stmt.executeUpdate();
	
	      // Redirect to login page
	      response.setStatus(HttpServletResponse.SC_OK);
	      response.getWriter().write("Success");
	    }

	  } catch (SQLException se) {
	    // Handle errors for JDBC
	    se.printStackTrace();
	    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	    response.getWriter().write("Database error - try again later");
	  } catch (Exception e) {
	    // Handle errors for Class.forName
	    e.printStackTrace();
	    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	    response.getWriter().write("Unknown error - please try again later");
	  } finally {
	    // Finally block used to close resources
	    try {
	      if (stmt != null) stmt.close();
	      if (rs != null) rs.close();
	    } catch (SQLException se2) {
	    } 
	    try {
	      if (conn != null) conn.close();
	    } catch (SQLException se) {
	      se.printStackTrace();
	    }
	  } 
	}

}
