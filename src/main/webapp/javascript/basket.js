let userId;
let userName;

function checkLoginStatus() {
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
         $("#user-name").text(`Hi, ${userName}`);
         console.log(response)
      },
      error: function (error) {
         console.log(error);
      }
   });
}

var totalCost = 0;
$(document).ready(function () {
   checkLoginStatus()
   $.ajax({
      url: "GetBasketItems",
      type: "GET",
      dataType: "json",
      success: function (data) {
         console.log(data);
         // Loop through the items and add them to the list
         var items = data.items;
         for (var i = 0; i < items.length; i++) {


            var title = items[i].title;
            var price = parseFloat(items[i].price);
            var image = items[i].image;
            var id = items[i].id;
            totalCost += price;
            console.log(totalCost);
            $("#basket-list").append(`
			  	<li id="book-${id}">
			    <img src="bookcovers/${image}" alt="${title} cover" />
			    <div class="basket-item-details">
			      <h3>${title}</h3>
			      <p>${price} GBP</p>
			    </div>
			    <button onClick=removeBook(${id},${price}) class="remove-item">Remove</button>
			  </li>
			`);

         }
         $("#total-price").text(`Total Price: ${totalCost.toFixed(2)} GBP`)
      },
      error: function (error) {
         console.log(error);
      }
   });


});


$('a[href="#logout"]').click(function (e) {

   logout();
});


function removeBook(bookId, price) {
   console.log(bookId, ",", price)
   totalCost -= price.toFixed(2)
   $("#total-price").text(`Total Price: ${totalCost.toFixed(2)} GBP`)
   $("#book-" + bookId).remove();
   $.ajax({
      url: 'BookAPI',
      type: 'POST',
      data: {
         action: 'RemoveBookFromBasket',
         bookId: bookId,
      },
      success: function (response) {
         console.log("Worked")
         console.log(response)
      },
      error: function (error) {
         alert("Failed to remove book from basket")
      }
   })
}

function logout() {
   // Code to log the user out goes here

   $.ajax({
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