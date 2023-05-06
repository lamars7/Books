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

import com.google.gson.Gson;
import com.google.gson.JsonObject;


@WebServlet("/ChangeUsername")
public class ChangeUsername extends HttpServlet {


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

    String newUsername = json.get("new_username").getAsString();
    System.out.println(newUsername);
    int id = json.get("userId").getAsInt();

    System.out.println(id);
    
    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;

    try {
      // Register JDBC driver
      Class.forName("com.mysql.jdbc.Driver");

      // Open a connection
      conn = DriverManager.getConnection(DB_URL, USER, PASS);

      // Check if id exists in database
      String checkIdSql = "SELECT * FROM users WHERE id=?";
      PreparedStatement checkIdStmt = conn.prepareStatement(checkIdSql);
      checkIdStmt.setInt(1, id);
      rs = checkIdStmt.executeQuery();

      if (rs.next()) { // Update user's username
        
        String updateUsernameSql = "UPDATE users SET name=? WHERE id=?";
        PreparedStatement updateUsernameStmt = conn.prepareStatement(updateUsernameSql);
        updateUsernameStmt.setString(1, newUsername);
        updateUsernameStmt.setInt(2, id);
        updateUsernameStmt.executeUpdate();

        // Send response indicating success
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write("Username updated successfully.");
      } else {
        // Id not found in database
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write("Incorrect Id");
        response.sendRedirect("login.html"); // Should have an Id if logged in, if not send them to login
      }

    } catch (SQLException se) {// Handle errors for JDBC
      se.printStackTrace();
      response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      response.getWriter().write("Database error - please try again later.");
    } catch (Exception e) { // Handle errors for Class.forName - Line 54
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
      } 
    }
  }

}