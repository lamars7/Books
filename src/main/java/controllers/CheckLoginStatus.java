package controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.JsonObject;


@WebServlet("/CheckLoginStatus")
public class CheckLoginStatus extends HttpServlet {
  
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

  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    // Initialize JDBC objects
    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    
    try {
      // Register JDBC driver
      Class.forName("com.mysql.jdbc.Driver");

      // Open a connection
      conn = DriverManager.getConnection(DB_URL, USER, PASS);

      // Check if user is logged in
      HttpSession session = request.getSession(false);
      if (session != null && session.getAttribute("userId") != null) {
        // User is logged in
    	  int userId = (int) session.getAttribute("userId");
    	  String userName = (String) session.getAttribute("userName");
    	  String token = (String) session.getAttribute("token");
    	    JsonObject jsonResponse = new JsonObject();
    	    jsonResponse.addProperty("token", token);
    	    jsonResponse.addProperty("loggedIn", true);
    	    jsonResponse.addProperty("userId", userId);
    	    jsonResponse.addProperty("userName", userName);
    	    response.setContentType("application/json");
    	    response.setCharacterEncoding("UTF-8");
    	    response.getWriter().write(jsonResponse.toString());
      } else {
        // User is not logged in
    	    JsonObject jsonResponse = new JsonObject();
    	    jsonResponse.addProperty("loggedIn", false);
    	    response.setContentType("application/json");
    	    response.setCharacterEncoding("UTF-8");
    	    response.getWriter().write(jsonResponse.toString());
      }

    } catch (SQLException se) {
      // Handle errors for JDBC
      se.printStackTrace();
      response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      response.getWriter().write("Database error.");
    } catch (Exception e) {
      // Handle errors for Class.forName
      e.printStackTrace();
      response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      response.getWriter().write("Unknown error.");
    } finally {
      // Finally block used to close resources
      try {
        if (stmt != null) stmt.close();
        if (rs != null) rs.close();
      } catch (SQLException se2) {
      } // nothing we can do
      try {
        if (conn != null) conn.close();
      } catch (SQLException se) {
        se.printStackTrace();
      } // end finally try
    } // end try
  }

}

