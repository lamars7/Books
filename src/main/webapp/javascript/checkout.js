$(document).ready(function () {

   $.ajax({
      url: 'https://restcountries.com/v3.1/all',
      success: function (data) {
         // Loop through the countries and add them to the dropdown list
         var select = $('#country');
         $.each(data, function (i, country) {
            select.append($('<option>').val(country.name.common).text(country.name.common));
         });
      },
      error: function (e) {
         // Show error message
         console.log(e)
         $('.overlay-content').html('<h2>Error</h2><p>An error occurred while processing your request. Please try again later.</p>');
      }
   });
   $('#basket-checkout').click(function () {
      $('#checkout-overlay').fadeIn();
   });

   $('.close-overlay').click(function () {
      $('.overlay').fadeOut();
   });

   let isValid = false;

   $('#checkout-form').submit(function (e) {
      e.preventDefault();
      console.log(isValid)

      // Get form data
      var name = $('#name').val();
      var email = $('#email').val();
      var phone = $('#phone').val();
      var address1 = $('#address-line-1').val();
      var address2 = $('#address-line-2').val();
      var city = $('#city').val();
      var postcode = $('#postcode').val();
      var country = $('#country').val();
      var cardNumber = $('#card-number').val();
      var cardHolder = $('#card-holder').val();

      var cvv = $('#cvv').val();

      // Validate form data


      if (cardNumber === '' || cardNumber.length !== 16) {
         $('#card-number').addClass('invalid');
         isValid = false;
         $('#card-number-error').text("Invalid Card Number");
      } else {
         // Check if credit card is valid using Binlist API


         $.ajax({
            url: 'https://lookup.binlist.net/' + cardNumber,
            method: 'GET',
            data: {
               cardNumber: cardNumber
            },
            success: function () {
               isValid = true;
               $('#card-number-error').text('');

            },
            error: function (e) {
               isValid = false;
               // Show error message
               console.log(e)
               console.log("not valid")
               //$('.overlay-content').html('<h2>Error</h2><p>An error occurred while processing your request. Please try again later.</p>');
               $('#card-number').addClass('invalid');
               $('#card-number-error').text('Invalid credit card number.');
               isValid = false;
            }
         });

      }
      if (cardHolder === '') {
         $('#card-holder').addClass('invalid');
         $('#card-holder-error').text('Please enter a valid card holder name');

         isValid = false;
      }

      if (cvv === '' || cvv.length < 3 || cvv.length > 4) {
         $('#cvv').addClass('invalid');
         $('#cvv-error').text('Invalid CVV')
         isValid = false;
      } else {
         $('#cvv').removeClass('invalid')
         $('#cvv-error').text('');
      }

      setTimeout(function () { // Timeout to load after checking card validation
         if (isValid) {
            // Submit form data
            $.ajax({
               type: 'POST',
               url: 'BookAPI',
               data: {
                  action: 'ConfirmPurchase'
               },
               success: function (response) {
                  console.log(response);
                  $('.overlay-content').html('<h2>Thank you for your purchase!</h2><p>Your order has been confirmed and will be shipped to the following address:</p><ul><li>' + name + '</li><li>' + email + '</li><li>' + phone + '</li><li>' + address1 + '</li><li>' + address2 + '</li><li>' + city + '</li><li>' + postcode + '</li><li>' + country + '</li></ul>');
				  setTimeout(function() {
				    location.reload();
				  }, 5000);               		
               },
               error: function (e) {
                  // Show error message
                  console.log(e);
                  $('.overlay-content').html('<h2>Error</h2><p>An error occurred while processing your request. Please try again later.</p>');
               }
            });
         }
      }, 750);


   });

   $('#card-number').on('input', function () {
      var cardNumber = $(this).val().replace(/[^\d]/g, ''); // Remove non-numeric characters
      cardNumber = cardNumber.substring(0, 16); // Limit to 16 digits

      $(this).val(cardNumber);
   });

   $('#phone').on('input', function () {
      var phoneNumber = $(this).val().replace(/[^\d]/g, ''); // Remove non-numeric characters
      $(this).val(phoneNumber);
   })


});