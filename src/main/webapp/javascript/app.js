let userId;
let userName;

function checkLoginStatus() { // Check Log in status > Log out if they aren't logged in
   $.ajax({
      url: 'CheckLoginStatus',
      type: 'GET',

      dataType: 'json',
      success: function (response) {
         if (!response.loggedIn) {
            window.location.href = '/Books/login.html';
            return
         }

         userId = response.userId
         userName = response.userName
         $("#user-name").text(`Hello, ${userName}`);
         console.log(response)
      },
      error: function (error) {
         console.log(error);
      }
   });
}


$(document).ready(function () {
   checkLoginStatus()

   $.ajax({
      url: "BookAPI",

      method: "GET",
      data: {
         action: 'GetBooks'
      },
      success: function (response) {
         
         var books = JSON.parse(response); // parse the JSON data

         // loop through each book and create a new book template
         for (var i = 0; i < books.length; i++) {
            var book = books[i];

            // create a new book template and populate it with data
            var bookTemplate = $(`<div class="book-item">
			<img class="book-image">
			<h2 class="book-title"></h2>
			<p class="book-genre"></p>
			<p class="book-price"></p>
			
			<button onClick="addBookToBasket(${book.id})" class="basket-button">ADD TO BASKET</button>
			<button onClick="viewBook(${book.id})" class="view-button">VIEW</button>
		</div>`);
            bookTemplate.find('.book-image').attr('src', 'bookcovers/' + book.image);
            bookTemplate.find('.book-title').text(book.title);
            bookTemplate.find('.book-genre').text(book.genre);
            bookTemplate.find('.book-price').text(book.price.toFixed(2) + " GBP");
            bookTemplate.find('.purchase-button').text('PURCHASE');


            // append the new book template to the book grid
            $('.book-grid').append(bookTemplate);
         }
      },
      error: function (error) {
         console.log(error);
      }
   });

   $('#search-term').on('keydown', function (event) {
      if (event.keyCode === 13) { // Check if the Enter key was pressed
         event.preventDefault(); // Prevent the form from submitting normally
         search();
      }
   });

   $('a[href="#logout"]').click(function (e) {
      logout();
   });

   $('a[href="#basket"]').click(function (e) {
      window.location.href = '/Books/basket.html';
   });


});


function search() {
   $('.book-grid').empty()
   var searchTerm = $('#search-term').val(); // Get the value of the search term input
   var searchBy = $('#search-by').val(); // Get the value of the search by select
   console.log(searchBy)
   console.log(searchTerm)
   // Replace with your search URL

   $.ajax({
      url: 'BookAPI',
      type: 'GET', // Replace with the HTTP method you want to use
      data: {
         action: 'Search',
         searchTerm: searchTerm,
         searchBy: searchBy
      },
      success: function (response) {
         // Handle the response from the server
         console.log(response);
         var books = JSON.parse(response); // parse the JSON data

         // loop through each book and create a new book template
         for (var i = 0; i < books.length; i++) {
            var book = books[i];

            // create a new book template and populate it with data
            var bookTemplate = $(`<div class="book-item">
			<img class="book-image">
			<h2 class="book-title"></h2>
			<p class="book-genre"></p>
			<p class="book-price"></p>
			
			<button onClick="addBookToBasket(${book.id})" class="basket-button">ADD TO BASKET</button>
			<button onClick="viewBook(${book.id})" class="view-button">VIEW</button>
		</div>`);
            bookTemplate.find('.book-image').attr('src', 'bookcovers/' + book.image);
            bookTemplate.find('.book-title').text(book.title);
            bookTemplate.find('.book-genre').text(book.genre);
            bookTemplate.find('.book-price').text('$' + book.price.toFixed(2));
            bookTemplate.find('.purchase-button').text('PURCHASE');


            // append the new book template to the book grid
            $('.book-grid').append(bookTemplate);
         }
      },
      error: function (error) {
         // Handle any errors that occur
         console.error(error);
      }
   });
}

function logout() {


   $.ajax({ // Make a request to the Log out Servlet
      url: 'UserLogout',
      type: 'POST',

      success: function (response) {
         window.location.href = '/Books/login.html';
      },
      error: function (error) {
         console.log(error);
      }
   });
}


// Function to run when purchase button is clicked
function purchaseBook(bookId) {
   console.log("Book purchased", bookId);
}

function viewBook(id) {
   // Make a request to get the book information by id
   $.ajax({
      url: 'BookAPI',
      type: 'GET',
      data: {
         action: 'GetBook',
         bookId: id
      },
      success: function (response) {
         // Handle the response from the server

         var book = JSON.parse(response)[0]; // parse the JSON data and access the book object
         console.log(book)

         // Create a modal element to display the book information
         var modal = $('<div class="modal">');
         var modalContent = $('<div class="modal-content">');
         var bookTitle = $('<h2>').text(book.title);
         var bookImage = $('<img>').attr('src', 'bookcovers/' + book.image).css('max-height', '300px');
         var bookDescription = $('<p>').text(book.description);
         var bookAuthor = $('<p>').text(`Author: ${book.author}`);
         var addToCartButton = $('<button class="add-to-cart-button">').text('ADD TO BASKET').on('click', function () {
            addBookToBasket(id);
            closeModal();
         });
         var closeButton = $('<span class="close">×</span>').on('click', closeModal);

         // Add the book information to the modal content
         modalContent.append(closeButton, bookTitle, bookImage, bookDescription, bookAuthor, addToCartButton);
         modal.append(modalContent);

         // Display the modal
         modal.hide().appendTo('body').fadeIn();
      },
      error: function (error) {
         // Handle any errors that occur
         console.error(error);
      }
   });
}


function closeModal() {

   $('.modal').remove();
}


function addBookToBasket(bookId) {
   // Create the data object to send as part of the POST request
   console.log(bookId)
   console.log(userId)

   var modal = $("#notification-modal");
   var modalMessage = $("#modal-message");
   modalMessage.html("Added book to basket");
   modalMessage.css("color", "green");
   modal.fadeIn(500).delay(1000).fadeOut(500);


   // Make the POST request
   $.ajax({
      url: 'AddToBasket',
      type: 'POST',
      dataType: 'json',
      data: {
         bookId: bookId,
         userId: userId
      },

      success: function (response) {
         console.log(response.message);
      },
      error: function (error) {
         console.log(error);
      }
   });
}