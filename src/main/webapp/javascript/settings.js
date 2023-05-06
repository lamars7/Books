let userId;
let userName;
let email;

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


   $('#submit-user-changes').click(function (e) {
      var email = $('#email').val();
      var old_password = $('#old-password').val()
      var new_password = $('#password').val()

      console.log(email, old_password, new_password)


      $.ajax({
         type: "PUT",
         url: "ChangePassword",
         dataType: 'json',
         data: JSON.stringify({
            userId: userId,
            new_password: new_password,
            old_password: old_password
         }),
         success: function (response) {
            // Redirect to home page

            alert(response.responseText)

         },
         error: function (error) {
            alert(error.responseText)

         }
      });
   });

});