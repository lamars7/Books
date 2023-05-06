$(document).ready(function () {
   $("#login-form").submit(function (event) {
      event.preventDefault();

      // Get user information from form
      var email = $("#email").val();
      var password = $("#password").val();

      // Send AJAX request to server
      $.ajax({
         type: "POST",
         url: "Login",
         data: {
            email: email,
            password: password
         },
         success: function () {
            // Redirect to home page

            window.location.href = "index.html";
         },
         error: function (error) {
            console.log(error)
            // Display error message

            if (error.status === 401) {
               $("#error-message").text(error.responseText);
               return
            }

            $("#error-message").text("An Error has occurred");

         }
      });
   });
});