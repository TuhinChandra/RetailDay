var app = angular.module("myApp", ["ngRoute", "monospaced.qrcode"]);
app.config(function($routeProvider) {
    $routeProvider
    .when("/", {
        templateUrl : "views/welcome.html",
        controller : "WelcomeController"
    })
    .when("/login", {
        templateUrl : "views/login.html",
        controller : "LoginController"
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
    .when("/landing", {
        templateUrl : "views/landing.html",
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