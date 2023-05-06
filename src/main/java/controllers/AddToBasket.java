package controllers;

import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;


@WebServlet("/AddToBasket")
public class AddToBasket extends HttpServlet {

  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
  static final String DB_URL = "jdbc:mysql://mudfoot.doc.stu.mmu.ac.uk:6306/safilama";

  // Database credentials
  static final String USER = "safilama";
  static final String PASS = "fingsteR2";

  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	  
	int userId = Integer.parseInt(request.getParameter("userId"));
    int bookId = Integer.parseInt(request.getParameter("bookId"));
    


    Connection conn = null;
    PreparedStatement stmt = null;
    try {
      Class.forName(JDBC_DRIVER);
      conn = DriverManager.getConnection(DB_URL, USER, PASS);

      String sql = "INSERT INTO user_basket (user_id, book_id) VALUES (?, ?)";
      stmt = conn.prepareStatement(sql);
      stmt.setInt(1, userId);
      stmt.setInt(2, bookId);
      
      int rowsAffected = stmt.executeUpdate();

      if (rowsAffected == 1) {
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write("Item added to cart successfully.");
      } else {
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        response.getWriter().write("Failed to add item to cart.");
      }
    } catch (SQLException se) {
      se.printStackTrace();
      response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      response.getWriter().write("Failed to add item to cart.");
    } catch (Exception e) {
      e.printStackTrace();
      response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      response.getWriter().write("Failed to add item to cart.");
    } finally {
      try {
        if (stmt != null) stmt.close();
      } catch (SQLException se2) {}
      try {
        if (conn != null) conn.close();
      } catch (SQLException se) {
        se.printStackTrace();
      }
    }
  }
}
