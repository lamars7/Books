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
         $("#user-name").text(`Hello, ${userName}`);
         getPurchaseHistory()
         console.log(response)
      },
      error: function (error) {
         console.log(error);
      }
   });
}

function getPurchaseHistory() {
   $.ajax({
      url: "BookAPI",
      type: "GET",
      dataType: "json",
      data: {
         action: 'GetPurchaseHistory',
         userId: userId
      },
      success: function (data) {
         console.log();
         // Loop through the items and add them to the list

         for (var i = 0; i < data.length; i++) {
            console.log(data)

            var title = data[i].title;
            var price = parseFloat(data[i].price);
            var image = data[i].image;
            var id = data[i].id;


            $("#history-list").append(`
			  	<li id="book-${id}">
			    <img src="bookcovers/${image}" alt="${title} cover" />
			    <div class="history-item-details">
			      <h3>${title}</h3>
			      <p>${price} GBP</p>
			    </div>
			    
			  </li>
			`);

         }

      },
      error: function (error) {
         console.log(error)
      }
   });
}


$(document).ready(function () {
   checkLoginStatus()

});