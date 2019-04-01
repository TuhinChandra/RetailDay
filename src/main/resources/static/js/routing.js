var app = angular.module("myApp", ["ngRoute", "monospaced.qrcode"]);
app.config(function($routeProvider) {
    $routeProvider
    .when("/", {
        templateUrl : "views/welcome.html",
        controller : "WelcomeController"
    })
    .when("/exit", {
        templateUrl : "views/exit.html"
    })
    .when("/partcipation", {
        templateUrl : "views/partcipation.html",
        controller : "ParticipationController"
    })
    .when("/login", {
        templateUrl : "views/login.html",
        controller : "LoginController"
    })
    .when("/adminlogin", {
        templateUrl : "views/adminlogin.html",
        controller : "AdminLoginController"
    })
    .when("/adminpanel", {
        templateUrl : "views/adminpanel.html",
        controller : "AdminPanelController"
    })
    	.when("/volunteerpanel", {
        templateUrl : "views/volunteerpanel.html",
        controller : "volunteerPanelController"
    })
	.when("/volunteerLogin", {
        templateUrl : "views/volunteerLogin.html",
        controller : "volunteerLoginController"
    })
    .when("/changePwd", {
        templateUrl : "views/changePwd.html",
        controller : "ChangePwdController"
    })
    .when("/forgetPwd", {
        templateUrl : "views/forgetPwd.html",
        controller : "ForgetPwdController"
    })
    .when("/register", {
        templateUrl : "views/register.html",
        controller : "RegisterController"
    })
    .when("/registrationdetails", {
        templateUrl : "views/registration_details.html",
        controller : "UpdateProfileController"
    })
    .when("/thankyou", {
        templateUrl : "views/thankyou.html",
        controller : "ThankYouController"
    })
    .when("/landing", {
        templateUrl : "views/landing_gray.html",
        controller : "LandingController"
    })
    .when("/attendee", {
        templateUrl : "views/attendee.html",
        controller : "AttendeeController"
    })
    .when("/event", {
        templateUrl : "views/event.html",
        controller : "EventController"
    })
    .when("/feedback", {
        templateUrl : "views/feedback.html",
        controller : "FeedbackController"
    })
    .when("/flight", {
        templateUrl : "views/flight.html",
        controller : "FlightController"
    })
    .when("/help", {
        templateUrl : "views/help.html",
        controller : "HelpController"
    })
    .when("/info", {
        templateUrl : "views/info.html",
        controller : "InfoController"
    })
    .when("/gifts", {
        templateUrl : "views/gifts.html",
        controller : "GiftController"
    })
    .when("/map", {
        templateUrl : "views/map.html",
        controller : "MapController"
    })
    .when("/liveupdate", {
        templateUrl : "views/liveupdate.html",
        controller : "LiveupdateController"
    })
    .when("/quiz", {
        templateUrl : "views/quiz.html",
        controller : "QuizController"
    })
    .when("/crossword", {
        templateUrl : "views/crossword.html",
        controller : "CrosswordController"
    })
    .when("/profile", {
        templateUrl : "views/profile.html",
        controller : "ProfileController"
    })
    .when("/gallery", {
        templateUrl : "views/gallery.html",
        controller : "GalleryController"
    })
    .when("/gallery_event", {
        templateUrl : "views/gallery_event.html",
        controller : "GalleryEventController"
    })
    .when("/gallery_kolkata", {
        templateUrl : "views/gallery_kolkata.html",
        controller : "GalleryKolkataController"
    })
    .when("/gallery_vedic_village", {
        templateUrl : "views/gallery_vedic_village.html",
        controller : "GalleryVedicVillageController"
    });
});

app.directive('valueMatches', ['$parse', function ($parse) {
	return {
	  require: 'ngModel',
	    link: function (scope, elm, attrs, ngModel) {
	      var originalModel = $parse(attrs.valueMatches),
	          secondModel = $parse(attrs.ngModel);
	      // Watch for changes to this input
	      scope.$watch(attrs.ngModel, function (newValue) {
	        ngModel.$setValidity(attrs.name, newValue === originalModel(scope));
	      });
	      // Watch for changes to the value-matches model's value
	      scope.$watch(attrs.valueMatches, function (newValue) {
	        ngModel.$setValidity(attrs.name, newValue === secondModel(scope));
	      });
	    }
	  };
	}]);