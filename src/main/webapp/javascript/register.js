$(document).ready(function() {
  $('#register-form').submit(function(e) {
    e.preventDefault(); // Prevent form submission

    // Make AJAX request to server
    $.ajax({
      type: 'POST',
      url: 'Register',
      data: $('#register-form').serialize(),
      success: function(response) {
        // Display success message for 2-3 seconds
        $('#error-message').css('color', 'green');
        $('#error-message').text('You have successfully registered! Please wait a moment.');
        setTimeout(function() {
          window.location.href = 'login.html'; // Redirect to login page
        }, 3000);
      },
      error: function(error) {
		  $('#error-message').css('color', 'red');
		  if(error.status === 409) {
			  $('#error-message').text('You already have an account with this email, please log in.');
			  return
		  }
		  
		  $('#error-message').text('Registration failed. Please try again.');
		  
         // Display error message
      }
    });
  });
});
