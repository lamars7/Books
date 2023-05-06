package controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.mindrot.jbcrypt.BCrypt;

@WebServlet("/Login")
public class Login extends HttpServlet {

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

  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    // Get user information from form
    String email = request.getParameter("email");
    String password = request.getParameter("password");

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
      String checkEmailSql = "SELECT * FROM users WHERE email=?";
      PreparedStatement checkEmailStmt = conn.prepareStatement(checkEmailSql);
      checkEmailStmt.setString(1, email);
      rs = checkEmailStmt.executeQuery();

      if (rs.next()) {
        // Verify password
        String hashedPassword = rs.getString("password");
        if (BCrypt.checkpw(password, hashedPassword)) {
          // Password is correct, create a token for the user
          String token = UUID.randomUUID().toString();
          int userId = rs.getInt("id");
          String userName = rs.getString("name");
          
          System.out.println(userName);
          
          // Store token and user id in the session object
          HttpSession session = request.getSession();
          session.setAttribute("token", token);
          session.setAttribute("userId", userId);
          session.setAttribute("userName", userName);
          session.setAttribute("email", email);

          // Redirect to dashboard
          response.setStatus(HttpServletResponse.SC_OK);
          response.sendRedirect("index.html");
        } else {
          // Password is incorrect
          response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
          response.getWriter().write("Incorrect password, please try again");
        }
      } else {
        // Email not found in database
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write("No account found with that email");
      }

    } catch (SQLException se) {
      // Handle errors for JDBC
      se.printStackTrace();
      response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      response.getWriter().write("Server Error - please try again later.");
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
