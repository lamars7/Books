package database;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.*;

import models.Book;

public class BookDao {
	
	Book oneBook = null;
	Connection conn = null;
    Statement stmt = null;
	String user = "safilama";
    String password = "fingsteR2";
    // Note none default port used, 6306 not 3306
    String url = "jdbc:mysql://mudfoot.doc.stu.mmu.ac.uk:6306/"+user;

	public BookDao() {}

	
	private void openConnection(){
		// loading jdbc driver for mysql
		try{
		    Class.forName("com.mysql.jdbc.Driver");
		} catch(Exception e) { System.out.println(e); }

		// connecting to database
		try{
			// connection string for demos database, username demos, password demos
 			conn = DriverManager.getConnection(url, user, password);
		    stmt = conn.createStatement();
		} catch(SQLException se) { System.out.println(se); }	   
    }
	private void closeConnection(){
		try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private Book getNextBook(ResultSet rs){
		Book thisBook=null;
		
		try {
			thisBook = new Book(
					rs.getInt("id"),
					rs.getString("title"),
					rs.getDouble("price"),
					rs.getString("author"),
					rs.getInt("year"),
					rs.getString("description"),
					rs.getString("image"),
					rs.getString("genre"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return thisBook;		
	}
	
	
	
	
	
   public ArrayList<Book> getAllBooks(){
	   
		ArrayList<Book> allBooks = new ArrayList<Book>();
		openConnection();
		
	    // Create select statement and execute it
		try{
		    String selectSQL = "select * from books";
		    ResultSet rs1 = stmt.executeQuery(selectSQL);
	    // Retrieve the results
		    while(rs1.next()){
		    	oneBook = getNextBook(rs1);
		    	allBooks.add(oneBook);
		   }

		    stmt.close();
		    closeConnection();
		} catch(SQLException se) { System.out.println(se); }

	   return allBooks;
   }
   
   public ArrayList<Book> getBookByTitle(String title){
	   
		ArrayList<Book> allBooks = new ArrayList<Book>();
		openConnection();
		
	    // Create select statement and execute it
		try{
		    String selectSQL = "SELECT * FROM books WHERE title LIKE '%"+title+"%'";
		    ResultSet rs1 = stmt.executeQuery(selectSQL);
	    // Retrieve the results
		    while(rs1.next()){
		    	oneBook = getNextBook(rs1);
		    	allBooks.add(oneBook);
		   }

		    stmt.close();  
		    closeConnection();
		} catch(SQLException se) { System.out.println(se); }

	   return allBooks;
  }
   
   public ArrayList<Book> getPurchaseHistory(int id) {
	    ArrayList<Book> allBooks = new ArrayList<>();
	    openConnection();

	    try {
	        String selectSQL = "SELECT books.*, purchase_history.purchase_date " +
	                           "FROM purchase_history " +
	                           "JOIN books ON purchase_history.book_id = books.id " +
	                           "WHERE purchase_history.user_id = " + id + " " +
	                           "ORDER BY purchase_history.purchase_date DESC";
	        ResultSet rs = stmt.executeQuery(selectSQL);

	        while (rs.next()) {
	            Book oneBook = getNextBook(rs);
	            
	            allBooks.add(oneBook);
	        }

	        stmt.close();
	        closeConnection();
	    } catch (SQLException se) {
	        System.out.println(se);
	    }

	    return allBooks;
	}

   
   public ArrayList<Book> getBookByGenre(String genre){
	   
		ArrayList<Book> allBooks = new ArrayList<Book>();
		openConnection();
		
	    // Create select statement and execute it
		try{
		    String selectSQL = "SELECT * FROM books WHERE genre LIKE '%"+genre+"%'";
		    ResultSet rs1 = stmt.executeQuery(selectSQL);
	    // Retrieve the results
		    while(rs1.next()){
		    	oneBook = getNextBook(rs1);
		    	allBooks.add(oneBook);
		   }

		    stmt.close();  
		    closeConnection();
		} catch(SQLException se) { System.out.println(se); }

	   return allBooks;
  }
   
   public ArrayList<Book> getBookByAuthor(String author){
	   
		ArrayList<Book> allBooks = new ArrayList<Book>();
		openConnection();
		
	    // Create select statement and execute it
		try{
		    String selectSQL = "SELECT * FROM books WHERE author LIKE '%"+author+"%'";
		    ResultSet rs1 = stmt.executeQuery(selectSQL);
	    // Retrieve the results
		    while(rs1.next()){
		    	oneBook = getNextBook(rs1);
		    	allBooks.add(oneBook);
		   }

		    stmt.close();  
		    closeConnection();
		} catch(SQLException se) { System.out.println(se); }

	   return allBooks;
  }
   
   public ArrayList<Book> getBookById(int id){
	   
		ArrayList<Book> allBooks = new ArrayList<Book>();
		openConnection();
		
	    // Create select statement and execute it
		try{
		    String selectSQL = "SELECT * FROM books WHERE id = "+id+"";
		    ResultSet rs1 = stmt.executeQuery(selectSQL);
	    // Retrieve the results
		    while(rs1.next()){
		    	oneBook = getNextBook(rs1);
		    	allBooks.add(oneBook);
		   }

		    stmt.close();  
		    closeConnection();
		} catch(SQLException se) { System.out.println(se); }

	   return allBooks;
  }
   

   
 
   
   public boolean deleteBook(int bookId, int userId) throws SQLException {

		boolean b = false;
		try {
			String selectSQL = "DELETE FROM user_basket WHERE user_id = "+userId+" and book_id = "+bookId+" LIMIT 1";

			openConnection();
			b = stmt.execute(selectSQL);
			closeConnection();
			b = true;
		} catch (SQLException s) {
			throw new SQLException("Book from basket not deleted");
		}
		return b;
	}
   
   public boolean confirmPurchase(int id) throws SQLException {

		boolean b = false;
		try {
			String selectSQL = "INSERT INTO purchase_history (user_id, book_id) SELECT user_id, book_id FROM user_basket WHERE user_id = "+id;

			openConnection();
			b = stmt.execute(selectSQL);
			closeConnection();
			b = true;
			deleteBasket(id);
		} catch (SQLException s) {
			throw new SQLException("Book from basket not deleted");
		}
		return b;
	}
   
   public boolean deleteBasket(int id) throws SQLException {

		boolean b = false;
		try {
			String selectSQL = "DELETE FROM user_basket WHERE user_id = "+id;
			
			System.out.println(selectSQL);
			openConnection();
			b = stmt.execute(selectSQL);
			closeConnection();
			b = true;
		} catch (SQLException s) {
			throw new SQLException("Book from basket deleted");
		}
		return b;
	}

   
}
