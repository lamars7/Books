let userId;
let userName;
let email;

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
         email = response.email
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


   $('#submit-user-changes').click(function (e) { // Function for when they click "Save changes"
      var new_username = $('#new-username').val();
      $.ajax({
         type: "PUT",
         url: "ChangeUsername",
         dataType: 'json',
         data: JSON.stringify({
            userId: userId,
            new_username: new_username
         }),
         success: function (response) {

            alert(`${response.responseText}, please sign out and back in to see changes!`)

         },
         error: function (error) {
            alert(error.responseText)

         }
      });
   });

});