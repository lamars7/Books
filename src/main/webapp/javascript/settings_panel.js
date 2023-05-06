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
         console.log(response)
      },
      error: function (error) {
         console.log(error);
      }
   });
}

$(document).ready(function () {

   checkLoginStatus()
   $('#change-username-btn').click(function () {
      window.location.href = 'change_username.html';
   });

   $('#change-password-btn').click(function () {
      window.location.href = 'change_password.html';
   });

   $('#view-purchase-history-btn').click(function () {
      window.location.href = 'purchase_history.html';
   });
});