package controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * Servlet implementation class GetBasketItems
 */
@WebServlet("/GetBasketItems")
public class GetBasketItems extends HttpServlet {


    private static final long serialVersionUID = -3394892466544659402L;
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://mudfoot.doc.stu.mmu.ac.uk:6306/safilama";

    // Database credentials
    static final String USER = "safilama";
    static final String PASS = "fingsteR2";

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login.html");
            return;
        }

        int userId = (int) session.getAttribute("userId");
        System.out.println(userId);

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            // Register JDBC driver
            Class.forName(JDBC_DRIVER);

            // Open a connection
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            // Execute SQL query to retrieve user's items from basket
            stmt = conn.createStatement();
            String sql = "SELECT books.title, books.price, books.image, books.id " +
                    "FROM user_basket " +
                    "JOIN books ON user_basket.book_id = books.id " +
                    "WHERE user_id = " + userId;

            rs = stmt.executeQuery(sql);

            // Build the JSON response with the items
            List<JsonObject> items = new ArrayList<JsonObject>();
            while (rs.next()) {
                JsonObject item = new JsonObject();
                String title = rs.getString("title");
                String price = rs.getString("price").replace("£", "");
                String image = rs.getString("image");
                int id = rs.getInt("id");
                item.addProperty("title", title);
                item.addProperty("price", price);
                item.addProperty("image", image);
                item.addProperty("id", id);
                items.add(item);
            }

            JsonObject json = new JsonObject();
            JsonArray jsonArray = new Gson().toJsonTree(items).getAsJsonArray();
            json.add("items", jsonArray);


            // Set response content type
            response.setContentType("application/json");

            // Send the JSON response
            PrintWriter out = response.getWriter();
            out.print(json.toString());
            out.flush();

        } catch (SQLException se) {
            // Handle errors for JDBC
            se.printStackTrace();
        } catch (Exception e) {
            // Handle errors for Class.forName
            e.printStackTrace();
        } finally {
            // Close resources
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }
}
