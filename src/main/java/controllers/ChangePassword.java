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

import org.mindrot.jbcrypt.BCrypt;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * Servlet implementation class ChangePassword
 */
@WebServlet("/ChangePassword")
public class ChangePassword extends HttpServlet {

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

  protected void doPut(HttpServletRequest request, HttpServletResponse response)
	      throws ServletException, IOException {
	    // Get JSON data from request
	    JsonObject json = new Gson().fromJson(request.getReader(), JsonObject.class);
	    
	    String oldPassword = json.get("old_password").getAsString();
	    String newPassword = json.get("new_password").getAsString();
	    int id = json.get("userId").getAsInt();
	    
	    System.out.println(oldPassword);
	    System.out.println(newPassword);
	    // Initialize JDBC objects
	    Connection conn = null;
	    PreparedStatement stmt = null;
	    ResultSet rs = null;

	    try {
	      // Register JDBC driver
	      Class.forName("com.mysql.jdbc.Driver");

	      // Open a connection
	      conn = DriverManager.getConnection(DB_URL, USER, PASS);

	      // Check if email exists in database
	      String checkEmailSql = "SELECT * FROM users WHERE id=?";
	      PreparedStatement checkEmailStmt = conn.prepareStatement(checkEmailSql);
	      checkEmailStmt.setInt(1, id);
	      rs = checkEmailStmt.executeQuery();

	      if (rs.next()) {
	        // Verify old password
	        String hashedPassword = rs.getString("password");
	        if (BCrypt.checkpw(oldPassword, hashedPassword)) {
	          // Password is correct, update user's password
	          String updatePasswordSql = "UPDATE users SET password=? WHERE id=?";
	          PreparedStatement updatePasswordStmt = conn.prepareStatement(updatePasswordSql);
	          String hashedNewPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
	          updatePasswordStmt.setString(1, hashedNewPassword);
	          updatePasswordStmt.setInt(2, id);
	          updatePasswordStmt.executeUpdate();

	          // Send response indicating success
	          response.setStatus(HttpServletResponse.SC_OK);
	          response.getWriter().write("Password updated successfully.");
	        } else {
	          // Password is incorrect
	          response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	          response.getWriter().write("Incorrect Password");
	        }
	      } else {
	        // Email not found in database
	        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	        response.getWriter().write("Incorrect Email");
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
	        if (stmt != null)
	          stmt.close();
	        if (rs != null)
	          rs.close();
	      } catch (SQLException se2) {
	      } // nothing can be done here
	      try {
	        if (conn != null)
	          conn.close();
	      } catch (SQLException se) {
	        se.printStackTrace();
	      } // end finally try
	    } // end try
	  }

}
