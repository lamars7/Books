// get search box and overlay elements
const searchBox = document.getElementById("search-box");
const overlay = document.getElementById("overlay");


// get search icon element and add click event listener
const searchIcon = document.getElementById("search-icon");

if (searchIcon) {
   searchIcon.addEventListener("click", function () {
      // show search box and overlay
      searchBox.style.display = "block";
      overlay.style.display = "block";
   });
}
const closeSearchBox = document.getElementById("close-search-box");

if (closeSearchBox) {
   closeSearchBox.addEventListener("click", function () {
      // hide search box and overlay
      searchBox.style.display = "none";
      overlay.style.display = "none";
   });

}

// get menu and overlay elements
const menu = document.getElementById("menu");
const closeMenu = document.getElementById("close-menu");
const menuBtn = document.getElementById("menu-icon");


menuBtn.addEventListener("click", () => {
   menu.style.display = "block";
   overlay.style.display = "block";
});

closeMenu.addEventListener("click", () => {
   menu.style.display = "none";
   overlay.style.display = "none";
});


// get menu icon element and add click event listener
const menuIcon = document.getElementById("menu-icon");
menuIcon.addEventListener("click", function () {
   // show menu and overlay
   menu.style.display = "block";
   overlay.style.display = "block";
});

// add click event listener to overlay to close menu and search box
overlay.addEventListener("click", function () {
   // hide menu and overlay
   menu.style.display = "none";
   overlay.style.display = "none";

   // hide search box
   if (searchBox) {
      searchBox.style.display = "none";
   }

});

$('a[href="#products"]').click(function (e) {
   console.log("test")
   window.location.href = '/Books/index.html'
});

$('a[href="#about"]').click(function (e) {
   console.log("test")
   window.location.href = '/Books/about.html'
});

$('a[href="#settings"]').click(function (e) {

   window.location.href = '/Books/settings_panel.html'
});

$('a[href="#contact-us"]').click(function (e) {
   console.log("test")
   window.location.href = '/Books/support.html'
});

$('a[href="#logout"]').click(function (e) {
   logout();
});

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

if ($('#basket')) {
   $('a[href="#basket"]').click(function (e) {
      window.location.href = '/Books/basket.html';
   });
}