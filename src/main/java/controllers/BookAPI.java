package controllers;

import com.google.gson.Gson;
import database.BookDao;
import models.Book;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;


@WebServlet("/BookAPI")
public class BookAPI extends HttpServlet {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // retrieve all books from the database
    	
    	String dataRequest = request.getParameter("action");
    	System.out.println(dataRequest);
    	BookDao dao = new BookDao();
    	if(dataRequest.equals("GetBooks")) {
    		
    		PrintWriter out = response.getWriter();
    		ArrayList<Book> allBooks = dao.getAllBooks();
    		Gson gson = new Gson();
    		String booksToJSON = gson.toJson(allBooks);
    		out.write(booksToJSON);
    	} else if(dataRequest.equals("GetBook")) {
    		
    		PrintWriter out = response.getWriter();
    		ArrayList<Book> allBooks = dao.getBookById(Integer.parseInt(request.getParameter("bookId")));
    		
    		Gson gson = new Gson();
    		String booksToJSON = gson.toJson(allBooks);
    		out.write(booksToJSON);
    	} else if(dataRequest.equals("Search")) {
    		PrintWriter out = response.getWriter();
    		String searchTerm = request.getParameter("searchTerm");
    		String searchBy = request.getParameter("searchBy");
    		System.out.println("SEARCHTERM = " + searchTerm);
    		System.out.println("searcHYBY = " +searchBy);
    		if (searchBy.equals("title")) {
    			ArrayList<Book> allBooks = dao.getBookByTitle(searchTerm);
        		Gson gson = new Gson();
        		String booksToJSON = gson.toJson(allBooks);
        		out.write(booksToJSON);
    		} else if (searchBy.equals("genre")) {
    			ArrayList<Book> allBooks = dao.getBookByGenre(searchTerm);
        		Gson gson = new Gson();
        		String booksToJSON = gson.toJson(allBooks);
        		out.write(booksToJSON);
    		} else if (searchBy.equals("author")) {
    			ArrayList<Book> allBooks = dao.getBookByAuthor(searchTerm);
        		Gson gson = new Gson();
        		String booksToJSON = gson.toJson(allBooks);
        		out.write(booksToJSON);
    		} 



    	} else if(dataRequest.equals("GetPurchaseHistory")) {
    		PrintWriter out = response.getWriter();
    		int id = Integer.valueOf(request.getParameter("userId"));
    		ArrayList<Book> purchaseHistory = dao.getPurchaseHistory(id);
    		Gson gson = new Gson();
    		String booksToJSON = gson.toJson(purchaseHistory);
    		out.write(booksToJSON);
    	}
    	

    }
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	String dataRequest = request.getParameter("action");

    	if(dataRequest.equals("RemoveBookFromBasket")) {
    		System.out.println("Wasd");
    		HttpSession session = request.getSession(false); // Set the "create" parameter to false to avoid creating a new session
    		if (session != null && session.getAttribute("userId") != null) {
    			
    			int userId = (int) session.getAttribute("userId"); // Retrieve userId from the session
    			System.out.println("UserId = " + userId);
    			int bookId = Integer.parseInt(request.getParameter("bookId")); // Get bookId parameter from request
    			
    			try {
    				BookDao dao = new BookDao();
    				dao.deleteBook(bookId, userId); // Call deleteBook method with bookId and userId parameters

    			} catch (SQLException e) {
    				System.out.println(e);
    				// Handle exception
    			}
    			
    		}
    	} else if(dataRequest.equals("ConfirmPurchase")) {
    		 HttpSession session = request.getSession(false);
    		    if (session != null && session.getAttribute("userId") != null) {
    		        int userId = (int) session.getAttribute("userId");
    		        try {
    		        	BookDao dao = new BookDao();
    		        	dao.confirmPurchase(userId);

    		        } catch (SQLException e) {
    		            System.out.println(e);
    		            // Handle exception
    		        }
    		    }
    	}

	}

	
	
}


