app.controller('ParticipationController', function($scope, $http, $window, $rootScope) {
    console.log("ParticipationController");
    $scope.usrNameVal = "";
    $rootScope.userDetails = {};
    $scope.errorMsg = "";
    $scope.doParticipate = function(usrName) {
        console.log(usrName + " ___ ");
        
        var url = "getEmployee/" + usrName;
        $http.get(url).then(function(response) {
            console.log(response);
            $rootScope.userDetails = response.data;
            $rootScope.isLoaded = true;
            
            if($rootScope.userDetails.status == "INVALID" && $rootScope.userDetails.confirmParticipation != "REJECTED"){
            	$scope.errorMsg = "Invalid user";
            }else if($rootScope.userDetails.confirmParticipation == "YES"){
            	$window.location.href = "#!login";
            }else if($rootScope.userDetails.confirmParticipation == "REJECTED"){
            	$scope.errorMsg = "User account has been temporarily deactivated";
            } else {
            	//Show dialog
            	$(document).ready(function() {
            		$('#modal_confirm').modal('show');
            		
            		$('#modal_confirm_yes').click(function(){
            			confirmInterest();
            	    });

                    $('#modal_confirm_no').click(function(){
                    	rejectParticipation();
              		});
                    //start Isometrix
                    $('#modal_close').click(function(){
                    	$('#modal_confirm').modal('hide');
                    });
            		//end
            	});
            }
            
        });
    };

    function confirmInterest(){
    	var url = "confirmParticipation/" + $rootScope.userDetails.employeeID;
    	$http.get(url).then(function(response) {
    		console.log(response);
        	$window.location.href = "#!login";
        });
    }
    
    function rejectParticipation(){
    	var url = "rejectParticipation/" + $rootScope.userDetails.employeeID;
    	$http.get(url).then(function(response) {
    		console.log(response);
    		$window.location.href = "#!exit";
        });
    }
    
});

app.controller('LoginController', function($scope, $http, $window, $rootScope) {
    console.log("LoginController");
    
    $scope.resetMsg="";
    if($rootScope.isLoaded){
    	if($rootScope.userDetails){
    		$scope.usrNameVal = $rootScope.userDetails.employeeID;    	
    	}else{
    		$scope.usrNameVal = "";
    	}
    	
    	$scope.usrNamePwd = "";
    	$scope.errorMsg = "";
    	$rootScope.usrName = "";
    	$rootScope.password = "";
    	
    	$rootScope.isLoaded = false;
    	$rootScope.profilePic = "";
    	//$rootScope.profilePic = "data:image/jpeg;base64,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";
    	
    	$scope.doAuthenticate = function(usrName, usrPwd) {
    		$scope.resetMsg="";
    		console.log(usrName + " ___ ");
    		$rootScope.password = usrPwd;
    		var url = "authenticateEmployee/" + usrName + "/" + usrPwd;
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
    				$window.location.href = "#!registrationdetails";
    			}else if(response.data.status == "INVALID"){
    				
    				// start Isometrix
    				if(response.data.countOfInvalidLoginAttempt == 1){
    					$scope.errorMsg="Incorrect password!!";
    				}
    				else if(response.data.countOfInvalidLoginAttempt == 2){
    					$scope.errorMsg="Incorrect password. Your account will be locked in next unsuccessful attempt !!";
    				}else if(response.data.countOfInvalidLoginAttempt >=3){
    					//$scope.errorMsg="Your account has been locked !!";
        
    	    			var url = "resetPassword/" + $scope.usrNameVal;
    	    			$http({
    	    				method: 'POST',
    	    				url: url,
    	    				headers: {'Content-Type': 'application/x-www-form-urlencoded'},
    	    			}).then(function(response) {
    	    				console.log(response);
    	    				$scope.errorMsg = "";
    	    				if(response.data.status == "INVALID"){
    	    					$scope.errorMsg = "Please enter a valid username";
    	    				}else{
    	    					$scope.resetMsg="Your account has been locked !!.Temparory password has been sent to your mail";
    	    				}
    	    			});
    	    		
    					
    					}
    				// $scope.errorMsg = "Invalid username and password.";
    				
    				//end 
    			}
    			
    		});
    	};
    	
    	
    	$scope.doResetPassword = function(){
    		console.log("doResetPassword ___ ");
    		$scope.errorMsg = "";
    		if($scope.usrNameVal > 0){
    			var url = "resetPassword/" + $scope.usrNameVal;
    			$http({
    				method: 'POST',
    				url: url,
    				headers: {'Content-Type': 'application/x-www-form-urlencoded'},
    			}).then(function(response) {
    				console.log(response);
    				$scope.errorMsg = "";
    				if(response.data.status == "INVALID"){
    					$scope.errorMsg = "Please enter a valid username";
    				}else{
    					$scope.resetMsg="Temparory password has been sent to your mail";
    				}
    			});
    		}else{
    			$scope.errorMsg = "Enter username to reset password.";
    		}
    	}
    
    }
    else{
           $window.location.href = "#!partcipation";
   }

});

app.controller('ChangePwdController', function($scope, $http, $window, $rootScope) {
    if($rootScope.isLoaded){
    	$scope.errorMsg = "";
        console.log("ChangePwdController");
        $scope.passwordSecond = "";
        $scope.passwordFirst = "";
        $scope.doChangePwd = function() {
            console.log("ChangePwdController ___ " + $scope.passwordSecond);

            var url = "changePassword/" + $rootScope.userDetails.employeeID + "/novia2019/" + $scope.passwordSecond;
            $http({
                method: 'POST',
                url: url,
                headers: {'Content-Type': 'application/x-www-form-urlencoded'},
            }).then(function(response) {
                console.log(response);
                if(response.data.status == "INCOMPLETE"){
                    $window.location.href = "#!register";
                }else if(response.data.status == "COMPLETE"){
                	$rootScope.userDetails = response.data;
                	$window.location.href = "#!registrationdetails";
                }else if(response.data.status == "INVALID"){
                	$scope.errorMsg = "Your new password should not be same as the default password";
                }
            });

        };
    }else{
        $window.location.href = "#!partcipation";
    }
});

app.controller('RegisterController', function($scope, $http, $window, $rootScope) {
    $http.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded;charset=utf-8';
    if($rootScope.isLoaded){
        console.log("RegisterController _ " + $rootScope.userDetails);
        
        $scope.foodPreference = "NON_VEG";
        $scope.mnum = "";
        $scope.gift = "Bhakti";
        $scope.accomodation = "Yes";
    
        var input = document.querySelector('input[type=file]');
        input.addEventListener('change', function() {
            var file = input.files[0];
            drawImage(file);
        });

        input.onclick = function() {
            this.value = null;
        };

        $scope.imageData = "data:image/png;base64,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";
        
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

        $scope.foodSelect = function(strVal){
        	$scope.foodPreference = strVal;
        }
        
        $scope.giftSelect = function(strVal){
        	$scope.gift = strVal
        }
        
        $scope.accomodationSelect = function(strVal){
        	$scope.accomodation = strVal
        }
        $scope.toggleSelection = function toggleSelection(event) {
        	$scope.chkselct = event.target.checked;
          };
          
        
        $scope.doRegisterUsr = function() {    	
			if($scope.chkselct){
				var address = $scope.address_local;
			}else{
				var address = "NO_PICKUP";
			}
            console.log("Image Data " + $scope.imageData);
            var url = "completeEmployeeProfile/" + $rootScope.userDetails.employeeID;
            $http({
                method: 'POST',
                url: url,
                headers: {'Content-Type': 'application/x-www-form-urlencoded'},
                data: $.param({
                    foodPreference: $scope.foodPreference,
                    mobile: $scope.mnum,
                    gift: $scope.gift,
                    accomodationNeeded: $scope.accomodation,
                    photoFile:$scope.imageData,
                    pickupAddress:address})
            }).then(function(response) {
                console.log(response);
                if(response.data.status == "INCOMPLETE"){
                    $window.location.href = "#!register";
                }else if(response.data.status == "COMPLETE"){
                	$rootScope.userDetails = response.data;
                    $window.location.href = "#!thankyou";
                }
            });
        };
        
        $('#modal_confirm_yes').click(function(){
			confirmInterest();
	    });

        $('#modal_confirm_no').click(function(){
        	rejectParticipation();
  		});

        
        function confirmInterest(){
        	var url = "confirmParticipation/" + $rootScope.userDetails.employeeID;
            $http({
                method: 'POST',
                url: url,
                headers: {'Content-Type': 'application/x-www-form-urlencoded'}
            }).then(function(response) {
                console.log(response);
            });
        }
        
        function rejectParticipation(){
        	var url = "rejectParticipation/" + $rootScope.userDetails.employeeID;
            $http({
                method: 'POST',
                url: url,
                headers: {'Content-Type': 'application/x-www-form-urlencoded'}
            }).then(function(response) {
                console.log(response);
                $window.location.href = "#!";
            });
        }
        
    }else{
        $window.location.href = "#!partcipation";
    }
    
});


app.controller('UpdateProfileController', function($scope, $http, $window, $rootScope) {
    $http.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded;charset=utf-8';
    console.log("UpdateProfileController _ " + $rootScope.userDetails);             
    
    if($rootScope.isLoaded){
    	$scope.successMsg ="";
    	
    	$scope.flightUpdate = $rootScope.userDetails.flightUpdateNeeded;
    	$scope.register = !($rootScope.userDetails.flightUpdateNeeded);
    	$scope.flight = $rootScope.userDetails.flightUpdateNeeded;
    	$scope.chkselct = $rootScope.userDetails.localTransportNeeded;
    	$scope.address_local = $rootScope.userDetails.pickupAddress;
    	
    	$scope.isregister = function () {
    		$scope.register = true;
    		$scope.reg = "active";
    		$scope.flight = false;
    	};
    	$scope.isflight = function () {
    		$scope.register = false;
    		$scope.flight = true;
    	};
    	
		$scope.foodSelect = function(strVal){
        	$scope.foodPreference = strVal;
        }
        
        $scope.giftSelect = function(strVal){
        	$scope.gift = strVal
        }
        
        $scope.accomodationSelect = function(strVal){
        	$scope.accomodation = strVal
        }
        //start Isometrix 
	 var input = document.querySelector('input[type=file]');
        input.addEventListener('change', function() {
            var file = input.files[0];
            drawImage(file);
        });

        input.onclick = function() {
            this.value = null;
        };

     //   $scope.imageData = "data:image/png;base64,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";
        
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

	//end
    	var flightInfos = $rootScope.userDetails.flightInfos;
        if(flightInfos != null){
        	for (i = 0; i < flightInfos.length; i++) {
        		var flightInfo=flightInfos[i];
        		
        		if(flightInfo.flightType=="INBOUND"){
        			$scope.inbound_airline_name=flightInfo.airlineName;
        			$scope.flight_arrival_no=flightInfo.flightNumber;
        			$scope.flight_arrival_to=flightInfo.location;
        			$scope.arrival_date=flightInfo.flightDate;
        			$scope.arrival_time=flightInfo.flightTime;
        			
        		}else if(flightInfo.flightType=="OUTBOUND"){
        			$scope.outbound_airline_name=flightInfo.airlineName;
        			$scope.flight_depart_no=flightInfo.flightNumber;
        			$scope.flight_depart_to=flightInfo.location;
        			$scope.depart_date=flightInfo.flightDate;
        			$scope.depart_time=flightInfo.flightTime;
        		}
        		
        	}
        }
        $scope.toggleSelection = function toggleSelection(event) {
        	$scope.chkselct = event.target.checked;
          };
        
        $scope.updateProfile = function() {
            console.log("Image Data " + $scope.imageData);
            if($scope.accomodation =="Yes"){
            	var address = "NO_PICKUP";
            }else if($scope.accomodation =="No"){
            if($scope.chkselct){
				var address = $scope.address_local;
			}else{
				var address = "NO_PICKUP";
			}
            }
            
            var url = "completeEmployeeProfile/" + $rootScope.userDetails.employeeID;
            $http({
                method: 'POST',
                url: url,
                headers: {'Content-Type': 'application/x-www-form-urlencoded'},
                data: $.param({
                    foodPreference: $scope.foodPreference,
                    mobile: $scope.mnum,
                    gift: $scope.gift,
                    accomodationNeeded: $scope.accomodation,
                    pickupAddress:address,
                    photoFile:$scope.imageData})
            }).then(function(response) {
                console.log(response);
				if(response.data.status == "COMPLETE"){
                	$rootScope.userDetails = response.data;
                	$scope.successMsg = "Your details updated successfully.";
                   // $window.location.href = "#!registrationdetails";
                	
                	$(document).ready(function(){
                		  //$('.toast').toast('show');
                		  $('#modal_data_update').modal('show');
                		  $('#data_save_ok').click(function(){
                		  	$window.location.href = "#!registrationdetails";
                		  });
                		});
                	if(!$scope.chkselct){
                		$scope.address_local="";
                	}
                }
            });
        };
        
        $scope.doflight_depart = function() {
            console.log("doflight_depart");
			var url = "addFlightInfo/" + $rootScope.userDetails.employeeID;
			
            $http({
                method: 'POST',
                url: url,
                headers: {'Content-Type': 'application/x-www-form-urlencoded'},
                data: $.param({
                	airlineName: $scope.outbound_airline_name,
                	flightNumber: $scope.flight_depart_no,
                	location: $scope.flight_depart_to,
                	flightType: 'OUTBOUND',
                	flightDate: $scope.depart_date,
                	flightTime: $scope.depart_time
                    })
            }).then(function(response) {
                console.log(response);
                $(document).ready(function() {
            		$('#modal_flight').modal('show');
            		$('#modal_ok').click(function(){
						$scope.flight_depart_no="" ;
						$scope.flight_depart_to="" ;
						$scope.depart_date="" ;
						$scope.depart_time="" ;
						$window.location.href = "#!registrationdetails";
            	    });
                });
            });
        }
        
        $scope.doflight_arrival = function() {
            console.log("doflight_arrival");
			var url = "addFlightInfo/" + $rootScope.userDetails.employeeID;
			
            $http({
                method: 'POST',
                url: url,
                headers: {'Content-Type': 'application/x-www-form-urlencoded'},
                data: $.param({
					airlineName: $scope.inbound_airline_name,
                	flightNumber: $scope.flight_arrival_no,
                	location: $scope.flight_arrival_to,
                	flightType: 'INBOUND',
                	flightDate: $scope.arrival_date,
                	flightTime: $scope.arrival_time
                    })
            }).then(function(response) {
                console.log(response);
                $(document).ready(function() {
            		$('#modal_flight').modal('show');
            		$('#modal_ok').click(function(){
            			$scope.inbound_airline_name="" ;
						$scope.flight_arrival_no="" ;
						$scope.flight_arrival_to="" ;
						$scope.arrival_date="" ;
						$scope.arrival_time="" ;
						$window.location.href = "#!registrationdetails";
            	    });
                });
            });
        }
       
    }else{
        $window.location.href = "#!partcipation";
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
        $window.location.href = "#!partcipation";
    }
});

app.controller('AttendeeController', function($scope, $window, $rootScope) {
    if($rootScope.isLoaded){
        console.log("AttendeeController" + $rootScope.userDetails);
    }else{
        $window.location.href = "#!partcipation";
    }
});

app.controller('CrosswordController', function($scope, $window, $rootScope) {
    if($rootScope.isLoaded){
        console.log("CrosswordController" + $rootScope.userDetails);
    }else{
        $window.location.href = "#!partcipation";
    }
});

app.controller('FeedbackController', function($scope, $window, $rootScope) {
    if($rootScope.isLoaded){
        console.log("FeedbackController" + $rootScope.userDetails);
    }else{
        $window.location.href = "#!partcipation";
    }
});

app.controller('FlightController', function($scope, $window, $rootScope) {
    if($rootScope.isLoaded){
        console.log("FlightController" + $rootScope.userDetails);
    }else{
        $window.location.href = "#!partcipation";
    }
});

app.controller('ForgetPwdController', function($scope) {
    console.log("ForgetPwdController");
});

app.controller('GalleryEventController', function($scope, $window, $rootScope) {
    if($rootScope.isLoaded){
        console.log("GalleryEventController" + $rootScope.userDetails);
    }else{
        $window.location.href = "#!partcipation";
    }
});

app.controller('GalleryKolkataController', function($scope, $window, $rootScope) {
    if($rootScope.isLoaded){
        console.log("GalleryKolkataController" + $rootScope.userDetails);
    }else{
        $window.location.href = "#!partcipation";
    }
});

app.controller('GalleryVedicVillageController', function($scope, $window, $rootScope) {
    if($rootScope.isLoaded){
        console.log("GalleryVedicVillageController" + $rootScope.userDetails);
    }else{
        $window.location.href = "#!partcipation";
    }
});

app.controller('GalleryController', function($scope, $window, $rootScope) {
    if($rootScope.isLoaded){
        console.log("GalleryController" + $rootScope.userDetails);
    }else{
        $window.location.href = "#!partcipation";
    }
});

app.controller('HelpController', function($scope, $window, $rootScope) {
    if($rootScope.isLoaded){
        console.log("HelpController" + $rootScope.userDetails);
    }else{
        $window.location.href = "#!partcipation";
    }
});

app.controller('InfoController', function($scope, $window, $rootScope) {
    if($rootScope.isLoaded){
        console.log("InfoController" + $rootScope.userDetails);
    }else{
        $window.location.href = "#!partcipation";
    }
});

app.controller('LandingController', function($scope, $window, $rootScope) {
    if($rootScope.isLoaded){
        console.log("LandingController" + $rootScope.userDetails);
        $scope.qrcode = "";
    }else{
        $window.location.href = "#!partcipation";
    }
});

app.controller('LiveupdateController', function($scope, $window, $rootScope) {
    if($rootScope.isLoaded){
        console.log("LiveupdateController" + $rootScope.userDetails);
    }else{
        $window.location.href = "#!partcipation";
    }
});

app.controller('GiftController', function($scope, $http, $window, $rootScope) {
    if($rootScope.isLoaded){
        console.log("GiftController" + $rootScope.userDetails);
        
        $scope.gift1 = "Hindi";
        $scope.gift2 = "Category1";
        
        $scope.doUpdateGift = function() {
            var url = "updateEmployeeInfo/" + $rootScope.userDetails.employeeID;
            $http({
                method: 'POST',
                url: url,
                headers: {'Content-Type': 'application/x-www-form-urlencoded'},
                data: $.param({
                	gift1:$scope.gift1,
                	gift2:$scope.gift2
                })
            }).then(function(response) {
                console.log(response);
                $window.location.href = "#!landing";
            });
        };
        
    }else{
        $window.location.href = "#!partcipation";
    }
});

app.controller('ProfileController', function($scope, $window, $rootScope) {
    if($rootScope.isLoaded){
        console.log("ProfileController" + $rootScope.userDetails);
    }else{
        $window.location.href = "#!partcipation";
    }
});

app.controller('QuizController', function($scope, $window, $rootScope) {
	var stompClient = null;
	var socket = null;
	var employeeID = null;
	 $(document).ready(function() {
		 
		 var currentURL = new URL(window.location);
		 employeeID = url_query("employeeId");
		 socket = new SockJS('quizWS');
		 stompClient = Stomp.over(socket);
		 stompClient.connect({}, onConnected, onError);
		 
		 
		 function onConnected() {
				stompClient.subscribe('/user/topic/getCurrentQuestion', onMessageReceivedQuiz);
				stompClient.subscribe('/topic/broadcastCurrentQuestion', onMessageReceivedQuiz);
				var d = new Date();
				var n = d.getTime();
				stompClient.send("/app/getCurrentQuestion", {},
						'{"currentMili":' + n + '}');
			}
			
			function onError(e) {
				console.log("error:" + e);
				if (stompClient !== null) {
			        stompClient.disconnect();
			        console.log("Disconnected");
			    }
			}
			
			
			function onMessageReceivedQuiz(payload) {
				var data=JSON.parse(payload.body);
				
				var template,
				contentHtml;
				
				if(data.current) {
					template = $('#ui-template-quiz').html();                    
				}else{
					template = $('#ui-template-quiz-inactive').html();
				}
				contentHtml = Mustache.to_html(template, data);
				$('#question-container').html(contentHtml);
			}
			
			function url_query( query ) {
			    query = query.replace(/[\[]/,"\\\[").replace(/[\]]/,"\\\]");
			    var expr = "[\\?&]"+query+"=([^&#]*)";
			    var regex = new RegExp( expr );
			    var results = regex.exec( window.location.href );
			    if ( results !== null ) {
			        return results[1];
			    } else {
			        return false;
			    }
			}
			$(document).on("click", '.js-submit-answer', function(e) { 
				e.preventDefault();
				
				var radioValueOfAnsweredQuestion = $("input[name='q_answer']:checked").val();
		        var questionID=$('#questionID').text().trim();
		        var emailId=$('#account_email').text().trim();

				var url='saveResponse/'+questionID+'/'+employeeID+'/'+radioValueOfAnsweredQuestion;
				console.log(url);

				$.ajax({
					url: url,
					processData: false,
					contentType: false,
					type: 'get',
					success: function (res) {
						console.log(res);
						$('.js-submit-answer').addClass('disabled');
					},
					error: function(error) {
						console.log(error);
					}
				})
				
			});
 	});
	 
	 
	 
});

app.controller('WelcomeController', function() {
    
});

app.controller('ThankYouController', function($window, $rootScope) {
	 //start Isometrix
	$("#backtoRegisterDetails").click(function(){
		$window.location.href = "#!registrationdetails";
	});
	//end
	if($rootScope.isLoaded){
        console.log("ThankYouController" + $rootScope.userDetails);
    }else{
        $window.location.href = "#!partcipation";
    }
    
});

app.controller('AdminPanelController', function($scope, $window, $http, $rootScope) {
	$scope.successMsg ="";
	console.log("AdminPanelController" + $rootScope.userDetails);
	$scope.genderlist = ["MALE", "FEMALE", "OTHERS"];
	$scope.gender = "FEMALE";
	
if($rootScope.isLoaded){
      $scope.fileData = "";
        var input = document.querySelector('input[type=file]');
        input.addEventListener('change', function() {
            var file = input.files[0];
            var fd = new FormData();
            fd.append('file', file);
            $scope.fileData = fd;
        });

        $scope.doUpload = function() {
            console.log("Image Data ");
            var url = "upload/";
            $http({
                method: 'POST',
                url: url,
                headers: {'Content-Type': undefined},
                data: $scope.fileData
            }).then(
        		function(response) {
        			$scope.errorMsg="";
            	$scope.successMsg ="User details uploaded successfully";
        	    },
        	    function(errorResponse) {
        	    	$scope.successMsg ="";
        	    	$scope.errorMsg = errorResponse.data.message;
            });
        };
	
		$scope.doAddUser = function() {      
			var empid = $scope.empid;
			var firstName = $scope.firstName;
			var lastName = $scope.lastName;
			var emailid = $scope.emailid;
			var shortname = $scope.shortname;
			var gender = $scope.gender;
			var url = "createEmployee/" + empid + "/" + emailid + "/" + firstName + "/" + lastName+ "/" + gender;	  
            $http({
                method: 'POST',
                url: url,
                headers: {'Content-Type': 'application/x-www-form-urlencoded'},
                data: $.param({
					empid: $scope.empid,
                    firstName: $scope.firstName,
                    lastName: $scope.lastName,
                    emailid: $scope.emailid,
                    shortname:$scope.shortname,
                    gender:$scope.gender
                })
            }).then(function(response) {
                console.log(response);
                $(document).ready(function() {
            		$('#modal_Add').modal('show');
            		$('#modal_ok').click(function(){
            			 $window.location.href = "#!adminpanel";
            	    });
            	});
            });
		   
        };
        
    }else{
        $window.location.href = "#!adminlogin";
    }
    
});

app.controller('AdminLoginController', function($scope, $http, $window, $rootScope) {
	console.log("AdminLoginController");
	
	$scope.loginMsg = ""
		
	$scope.doAuthenticate = function(usrName, usrPwd) {
        var url = "authenticateEmployee/" + usrName + "/" + usrPwd;
        //var url = 'changepwd.json';
        $scope.errorMsg = "";
        $http.get(url).then(function(response) {
            console.log(response);

            if(response.data.role == "ADMIN" || response.data.role == "SUPERADMIN"){
            	$rootScope.isLoaded = true;
                $window.location.href = "#!adminpanel";
            }else{
            	$scope.loginMsg = "Please login as Admin."
            }
        });
    };
    
});
app.controller('volunteerLoginController', function($scope, $window, $http, $rootScope) {
	
	$scope.doAuthenticatevolunteerLogin = function(username, password) {
    		$scope.resetMsg="";
    		console.log(username + " ___ ");
    		var url = "authenticateEmployee/" + username + "/" + password;
    		$scope.errorMsg = "";
    		$http.get(url).then(function(response) {
    			console.log(response);
    			$rootScope.userDetails = response.data;
    			$rootScope.isLoaded = true;
    			
    			if(response.data.status!="INVALID" && response.data.role == "VOLUNTEER"){
    				$window.location.href = "#!volunteerpanel";
    			}else{
    				$scope.errorMsg = "Please login with Volunteer credential";
    			}
    		});
    	};
		/*if($rootScope.isLoaded){		
			}else{
			$window.location.href = "#!partcipation";
			}*/
	
});
app.controller('volunteerPanelController', function($scope, $window, $http, $rootScope) {
	
		$scope.employee_details = false;
		$scope.checkedIn = true;
		
		$scope.checkInMsg="";
		$scope.checkInStatus="";
		$scope.errorMsg="";
		if($rootScope.isLoaded){		
			
			$scope.close = function(){
				$scope.employee_details=false;
				$scope.emailID="";
				$scope.firstName="";
				$scope.lastName="";
				$scope.foodPreference="";
				$scope.mobile="";
				$scope.registrationNo="";
				$scope.checkInMsg="";
				$scope.checkInStatus="";
			}
			$scope.checkInMsg="";
			$scope.doCheckIn = function(usrNameVal){
				var url = "checkIn/" + usrNameVal;
		        $http.get(url).then(function(response) {
		            console.log(response);
		            $rootScope.userDetails = response.data;
		            $rootScope.isLoaded = true;
		            var employee=$rootScope.userDetails;
		            if(employee.checkedIn){
		            	$scope.checkInStatus="CHECK In Successful";
		            	$scope.checkInMsg="User checked in successfully";
		            	$scope.checkedIn = false;	            	
		            }else{
		            	$scope.checkInStatus="Not Yet Checked In";
		            	$scope.checkInMsg="There is some error in check in. Please contact technical team";
		            	$scope.checkedIn = true;
		            }
		        });
	    	}
			$scope.doFindEmployee = function(usrNameVal) {
				
				var url = "getEmployee/" + usrNameVal;
		        $http.get(url).then(function(response) {
		            console.log(response);
		            $rootScope.userDetails = response.data;
		            $rootScope.isLoaded = true;
		            var employee=$rootScope.userDetails;
		            if((employee.role!="ADMIN" || employee.role!="VOLUNTEER") && employee.registrationNo > 0){
	            		$scope.employee_details=true;
	            		$scope.employeeID=employee.employeeID;
	            		$scope.emailID=employee.emailID;
	            		$scope.firstName=employee.firstName;
	            		$scope.lastName=employee.lastName;
	            		$scope.foodPreference=employee.foodPreference;
	            		$scope.mobile=employee.mobile;
	            		$scope.registrationNo=employee.registrationNo;
	            		if(employee.checkedIn){
	            			$scope.checkInStatus="Already Checked In";
	            			$scope.checkedIn = false;
	            		}else{
	            			$scope.checkInStatus="Not Yet Checked In";
	            			$scope.checkedIn=true;
	            		}
		            }else{
		            	$scope.employee_details=false;
		            	$scope.errorMsg= "Employee Not Found";
		            }
		        });
	    	};
		}else{
			$window.location.href = "#!volunteerLogin";
		}
});