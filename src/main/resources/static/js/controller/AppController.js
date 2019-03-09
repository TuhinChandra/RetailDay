app.controller('LoginController', function($scope, $http, $window, $rootScope) {
    console.log("LoginController");
    $scope.usrNameVal = "";
    $scope.usrNamePwd = "";
    $scope.errorMsg = "";
    $rootScope.usrName = "";
    $rootScope.password = "";

    $rootScope.userDetails = {};
    $rootScope.isLoaded = false;

    $scope.doAuthenticate = function(usrName, usrPwd) {
        console.log(usrName + " ___ ");
        $rootScope.password = usrPwd;
        var url = "/Novia2019/authenticateEmployee/" + usrName + "/" + usrPwd;
        //var url = 'changepwd.json';
        $scope.errorMsg = "";
        $http.get(url).then(function(response) {
            console.log(response);
            $rootScope.userDetails = response.data;
            $rootScope.isLoaded = true;

            if(response.data.status == "CHANGE_DEFAULT_PASSWORD"){
                $window.location.href = "#!changePwd";
            }else if(response.data.status == "INCOMPLETE"){
                $window.location.href = "#!register";
            }else if(response.data.status == "COMPLETE"){
                $window.location.href = "#!landing";
            }else if(response.data.status == "INVALID"){
                $scope.errorMsg = "Invalid username and password.";
            }
        });
    };

    $scope.doResetPassword = function(usrName){
        console.log("doResetPassword ___ ");
        if(usrName.length > 0){
            var url = "/Novia2019/resetPassword/" + usrName;
            $http({
                method: 'POST',
                url: url,
                headers: {'Content-Type': 'application/x-www-form-urlencoded'},
            }).then(function(response) {
                console.log(response);
                $scope.errorMsg = "";
                alert("Password reset please use default password.")
            });
        }else{
            $scope.errorMsg = "Enter username to reset password.";
        }
    }
});

app.controller('ChangePwdController', function($scope, $http, $window, $rootScope) {
    if($rootScope.isLoaded){
        console.log("ChangePwdController");
        $scope.passwordSecond = "";
        $scope.doChangePwd = function() {
            console.log("ChangePwdController ___ " + $scope.passwordSecond);

            var url = "/Novia2019/changePassword/" + $rootScope.userDetails.employeeID + "/novia2019/" + $scope.passwordSecond;
            $http({
                method: 'POST',
                url: url,
                headers: {'Content-Type': 'application/x-www-form-urlencoded'},
            }).then(function(response) {
                console.log(response);
                if(response.data.status == "INCOMPLETE"){
                    $window.location.href = "#!register";
                }
            });

        };
    }else{
        $window.location.href = "#!login";
    }
});

app.controller('RegisterController', function($scope, $http, $window, $rootScope) {
    $http.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded;charset=utf-8';
    if($rootScope.isLoaded){
        console.log("RegisterController _ " + $rootScope.userDetails);
        
        $scope.foodPreference = "Veg";
        $scope.shirtSize = "XL";
        $scope.cityTour = "Tour 1"
    
        var input = document.querySelector('input[type=file]');
        input.addEventListener('change', function() {
            var file = input.files[0];
            drawImage(file);
        });

        input.onclick = function() {
            this.value = null;
        };

        $scope.imageData = ""
        function drawImage(file) {
            var reader = new FileReader();
            reader.onload = function(e) {
                dataURL = e.target.result;
                $scope.imageData = dataURL;
                let img=document.getElementById("profilePic");
                img.src=dataURL;

                orignal_image = new Image();
                orignal_image.onload = function() {
                    
                };
        
                orignal_image.src = dataURL;
            }
            reader.readAsDataURL(file);
        }

        $scope.doRegisterUsr = function() {
            console.log("Image Data " + $scope.imageData);
            var url = "/Novia2019/completeEmployeeProfile/" + $rootScope.userDetails.employeeID;
            $http({
                method: 'POST',
                url: url,
                headers: {'Content-Type': 'application/x-www-form-urlencoded'},
                data: $.param({
                    firstName: $rootScope.userDetails.firstName,
                    lastName: $rootScope.userDetails.lastName,
                    emailID: $rootScope.userDetails.emailID,
                    foodPreference: $rootScope.userDetails.foodPreference,
                    mobile: $rootScope.userDetails.mobile,
                    photoFile: $scope.imageData,
                    gender: $rootScope.userDetails.gender,
                    cityTour: $rootScope.userDetails.cityTour,
                    shirtSize: $rootScope.userDetails.shirtSize})
            }).then(function(response) {
                console.log(response);
                if(response.data.status == "INCOMPLETE"){
                    $window.location.href = "#!register";
                }else if(response.data.status == "COMPLETE"){
                    $window.location.href = "#!landing";
                }
            });
        };
    }else{
        $window.location.href = "#!login";
    }
    
});

app.controller('EventController', function($scope, $window, $rootScope) {
    if($rootScope.isLoaded){
        console.log("EventController");
        $(function() {
            $( "#event_feedback1" ).click(function() {
                $('#modal_long_feedback').modal('show')
            });

            $( "#event_feedback2" ).click(function() {
                $('#modal_long_feedback').modal('show')
            });

            $( "#event_feedback3" ).click(function() {
                $('#modal_long_feedback').modal('show')
            });

            $( "#event_feedback4" ).click(function() {
                $('#modal_long_feedback').modal('show')
            });

            $( "#event_feedback5" ).click(function() {
                $('#modal_long_feedback').modal('show')
            });
        });
    }else{
        $window.location.href = "#!login";
    }
});

app.controller('AttendeeController', function($scope, $window, $rootScope) {
    if($rootScope.isLoaded){
        console.log("AttendeeController" + $rootScope.userDetails);
    }else{
        $window.location.href = "#!login";
    }
});

app.controller('CrosswordController', function($scope, $window, $rootScope) {
    if($rootScope.isLoaded){
        console.log("CrosswordController" + $rootScope.userDetails);
    }else{
        $window.location.href = "#!login";
    }
});

app.controller('FeedbackController', function($scope, $window, $rootScope) {
    if($rootScope.isLoaded){
        console.log("FeedbackController" + $rootScope.userDetails);
    }else{
        $window.location.href = "#!login";
    }
});

app.controller('FlightController', function($scope, $window, $rootScope) {
    if($rootScope.isLoaded){
        console.log("FlightController" + $rootScope.userDetails);
    }else{
        $window.location.href = "#!login";
    }
});

app.controller('ForgetPwdController', function($scope) {
    console.log("ForgetPwdController");
});

app.controller('GalleryEventController', function($scope, $window, $rootScope) {
    if($rootScope.isLoaded){
        console.log("GalleryEventController" + $rootScope.userDetails);
    }else{
        $window.location.href = "#!login";
    }
});

app.controller('GalleryKolkataController', function($scope, $window, $rootScope) {
    if($rootScope.isLoaded){
        console.log("GalleryKolkataController" + $rootScope.userDetails);
    }else{
        $window.location.href = "#!login";
    }
});

app.controller('GalleryVedicVillageController', function($scope, $window, $rootScope) {
    if($rootScope.isLoaded){
        console.log("GalleryVedicVillageController" + $rootScope.userDetails);
    }else{
        $window.location.href = "#!login";
    }
});

app.controller('GalleryController', function($scope, $window, $rootScope) {
    if($rootScope.isLoaded){
        console.log("GalleryController" + $rootScope.userDetails);
    }else{
        $window.location.href = "#!login";
    }
});

app.controller('HelpController', function($scope, $window, $rootScope) {
    if($rootScope.isLoaded){
        console.log("HelpController" + $rootScope.userDetails);
    }else{
        $window.location.href = "#!login";
    }
});

app.controller('InfoController', function($scope, $window, $rootScope) {
    if($rootScope.isLoaded){
        console.log("InfoController" + $rootScope.userDetails);
    }else{
        $window.location.href = "#!login";
    }
});

app.controller('LandingController', function($scope, $window, $rootScope) {
    if($rootScope.isLoaded){
        console.log("LandingController" + $rootScope.userDetails);
        $scope.qrcode = "ABCsssss";
    }else{
        $window.location.href = "#!login";
    }
});

app.controller('LiveupdateController', function($scope, $window, $rootScope) {
    if($rootScope.isLoaded){
        console.log("LiveupdateController" + $rootScope.userDetails);
    }else{
        $window.location.href = "#!login";
    }
});

app.controller('MapController', function($scope, $window, $rootScope) {
    if($rootScope.isLoaded){
        console.log("MapController" + $rootScope.userDetails);
    }else{
        $window.location.href = "#!login";
    }
});

app.controller('ProfileController', function($scope, $window, $rootScope) {
    if($rootScope.isLoaded){
        console.log("ProfileController" + $rootScope.userDetails);
    }else{
        $window.location.href = "#!login";
    }
});

app.controller('QuizController', function($scope, $window, $rootScope) {
    if($rootScope.isLoaded){
        console.log("QuizController" + $rootScope.userDetails);
    }else{
        $window.location.href = "#!login";
    }
});

app.controller('WelcomeController', function() {
    
});