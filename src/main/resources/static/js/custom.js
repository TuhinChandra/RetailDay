jQuery(document).ready(function($) {
	/************************** Global Variable Section Start **************************/
	var connectionURL='';
	var mobileDetect = new MobileDetect(window.navigator.userAgent);
	var stompClient = null;
	var socket = null;
	var delay=3000;
	var userImage=null;
	/************************** Global Variable Section End **************************/
	//For Phone Gap mobile app, url is required.
	if(mobileDetect.mobile()){
		//connectionURL='http://kfday.herokuapp.com';
	}
	console.log("connectionURL="+connectionURL);
	
	if(localStorage.isloggedin === 'true'){
		hideSignInShowLogOff();
		
		var employee = { 
				  "name"  :  localStorage.loginname, 
				  "emailID"   :  localStorage.loginemail, 
				  "photo"      :  localStorage.photo 
				}
		
		var template1 = $('#ui-template-account-details').html();
		var template2 = $('#ui-template-sign-out-and-account-photo').html();    
		var template3 = $('#ui-template-edit-profile-menu').html();
		$('#user_details').html(Mustache.to_html(template1, employee));
		$('#logoff').html(Mustache.to_html(template2, employee));
		$('#editProfile').html(Mustache.to_html(template3, employee));
	}else{
		showSignInHideLogOff();
	}
	
	$(document).on("click", '.close-modal', function(e) { 
		if (stompClient !== null) {
	        stompClient.disconnect();
	        console.log("Disconnected after modal close");
	    }
	});
	
	/************************** Functions Start **************************/
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
	
	function onConnectedEvent() {
		stompClient.subscribe('/user/topic/getCurrentEvent', onMessageReceivedEvent);
		stompClient.subscribe('/topic/broadcastCurrentEvent', onMessageReceivedEvent);
		stompClient.send("/app/getEventStatus", {}, '{"currentMili":' + new Date().getTime() + '}');
	}
	
	function onErrorEvent(error) {
		console.log('onErrorEvent()');
		console.log(error);
		if (stompClient !== null) {
	        stompClient.disconnect();
	        console.log("Disconnected");
	    }
	}
	
	function onMessageReceivedEvent(payload) {
		
		var data=JSON.parse(payload.body);
		console.log("onMessageReceivedEvent.data="+data);
		if(data.length > 0){
			for(var i = 0; i < data.length; i++) {
				console.log("eventID="+data[i].eventID);
				var template = $('#ui-template-vote-event').html();    
				var currentEvent= $('.event-container').find('.js-event-tab-'+data[i].eventID);
				var element = currentEvent.find('.event-vote');
				if(element.length > 0){
					element.html(Mustache.to_html(template, data[i]));
					element.click();
				}
	        }
			
		}else{
			console.log("No event is running at this moment!!");
			var previousEvent= $('.event-container').find('.event-vote');
			if(previousEvent.length > 0){
				previousEvent.html(null);
			}
		}
	}
	
	function validateLoginAndOpenModal(modalToOpen){
		if(localStorage.isloggedin === 'true'){
			$.LoadingOverlay("show");
			setTimeout(function(){
				$.LoadingOverlay("hide");
			}, delay);
			$(modalToOpen).modal();
			return true;
		}else{
			$('.js-signin-start').click();
			return false;
		}
		
	}
	function loginWithEmailAndPassword(email, password){
		var url=connectionURL+'/getEmployee/'+email+'/'+password;
		console.log("Login URL:"+url);
		
		$.LoadingOverlay("show");
		$.ajax({
			url: url,
			processData: false,
			contentType: false,  
			type: 'get',
			success: function (employee) {
				console.log(employee);
				
				if(employee){
					$('.close-modal').click();
					var template1 = $('#ui-template-account-details').html();
					var template2 = $('#ui-template-sign-out-and-account-photo').html();
					var template3 = $('#ui-template-edit-profile-menu').html();
					$('#user_details').html(Mustache.to_html(template1, employee));
					$('#logoff').html(Mustache.to_html(template2, employee));
					$('#editProfile').html(Mustache.to_html(template3, employee));
					
					localStorage.clear();
					
					localStorage.loginemail = email;
					localStorage.loginpwd = password;
					localStorage.loginname=employee.name;
					localStorage.photo=employee.photo;
					if ($('#remember').is(':checked')) {
						localStorage.rememberme = $('#remember').val();
					}

					localStorage.isloggedin = true;
					hideSignInShowLogOff();
					
				}else{
					$('#error-panel').html("Ooops!! Login credential is invalid");
				}
			},
			error: function(error) {
				console.log(error);
				$('#error-panel').html("Ooops!! Looks like there is some technical problem!!");
			},
			complete: function (jqXHR, status) {
				setTimeout(function(){
				    $.LoadingOverlay("hide");
				}, delay);
			}
			
		})
	}
	function hideSignInShowLogOff() {
		$("#signIn").hide();
		$("#logoff").show();
		$("#editProfile").show();
	}
	
	function showSignInHideLogOff(){
		$("#signIn").show();
		$("#logoff").hide();
		$("#editProfile").hide();
	}
 
    function doBounce(element, times, distance, speed) {
    	for(var i = 0; i < times; i++) {
          element.animate({fontSize: '+='+distance}, speed)
            .animate({fontSize: '-='+distance}, speed);
        }        
    }
	/************************** Functions End **************************/
	
	/************************** Quiz Section Start **************************/


	$('.js-open-quiz').on('click', function (e) {
		
		e.preventDefault();
		if(validateLoginAndOpenModal('#quiz-modal')){
			socket = new SockJS(connectionURL+'/quizWS');
			stompClient = Stomp.over(socket);
			stompClient.connect({}, onConnected, onError);
		}
		

	});


	$(document).on("click", '.js-submit-answer', function(e) { 
		e.preventDefault();
		
		var radioValueOfAnsweredQuestion = $("input[name='q_answer']:checked").val();
        var questionID=$('#questionID').text().trim();
        var emailId=$('#account_email').text().trim();

		var url=connectionURL+'/saveResponse/'+questionID+'/'+emailId+'/'+radioValueOfAnsweredQuestion;
		console.log(url);

		$.LoadingOverlay("show");
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
			},
			complete: function (jqXHR, status) {
				setTimeout(function(){
				    $.LoadingOverlay("hide");
				}, delay);
			}
		})
		
	});

	$(document).on("click", '.js-prev-question', function(e) { 
		$('.close-modal').click();
	});
	
	/************************** Quiz Section End **************************/

	/************************** Event Section Start **************************/
	
	$('.js-open-events').on('click', function(e){
		e.preventDefault();
		if(validateLoginAndOpenModal('#event-modal')){
			$.ajax({
				url: connectionURL+'/getAllEvents',
				processData: false,
				contentType: false,
				type: 'get',
				success: function (events_array) {
					console.log(events_array);
					var view     = {events: events_array};
					var template = $('#ui-template-all-event').html();
					$('#event-modal').html(Mustache.to_html(template, view));
					/*stompClient = Stomp.over(new SockJS(connectionURL+'/eventMobileWS'));
					stompClient.connect({},onConnectedEvent,onErrorEvent);*/
				},
				error: function(error) {
					console.log(error);
				},
				complete: function (jqXHR, status) {
					setTimeout(function(){
					    $.LoadingOverlay("hide");
					}, delay);
				}
			})
		}
		
	});
	
	$(document).on("click", '.js-send-event-feedback', function(e) { 
		
		e.preventDefault();
        var $element = $(this).closest('.event-vote').find('.js-send-event-feedback'),
            $elementToanimate = $(this).find('i');
        $element.removeClass('active');
        doBounce($elementToanimate, 1, '10px', 400);
        $(this).toggleClass('active');
		
		var data=$(this).data();
		var feedbackPostURL=connectionURL+'/saveEventResponseWithComment/'+localStorage.loginemail+'/'+data.eventid+'/'+data.vote+'/'+'No Comment';
		console.log("feedbackPostURL="+feedbackPostURL);
		$.ajax({
			url: feedbackPostURL,
			processData: false,
			contentType: false,
			type: 'get',
			success: function (res) {
				console.log("success="+res);
			},
			error: function(error) {
				console.log("error="+error);
			},
			complete: function (jqXHR, status) {
			}
		})
	});
	
	/************************** Event Section End **************************/
	
	/************************** Contest Section Start **************************/
	
	$('#contest').on('click', function(e){
		
		e.preventDefault();

		if(validateLoginAndOpenModal('#portfolio-modal')){
			$.ajax({
				url: connectionURL+'/getAllImages',
				processData: false,
				contentType: false,
				type: 'get',
				success: function (images_array) {
					console.log(images_array);
					var view     = {images: images_array};
					var template = $('#ui-template-photo-contest-images').html();
					$('.photo-contest-images-container').html(Mustache.to_html(template, view));
				},
				error: function(error) {
					console.log(error);
				},
				complete: function (jqXHR, status) {
					setTimeout(function(){
					    $.LoadingOverlay("hide");
					}, delay);
				}
			})
		}
		
	});
	
	$(document).on("click", '.js-send-photo-feedback', function(e) { 
		
		e.preventDefault();
        var $element = $(this).closest('.event-vote').find('.js-send-photo-feedback'),
            $elementToanimate = $(this).find('i');
        $element.removeClass('active');
        doBounce($elementToanimate, 1, '10px', 400);
        $(this).toggleClass('active');
		
		var data=$(this).data();
		var feedbackPostURL=connectionURL+'/savePhotoContestResponse/'+localStorage.loginemail+'/'+data.imageid+'/'+data.vote+'/'+'No Comment';
		console.log("feedbackPostURL="+feedbackPostURL);
		$.ajax({
			url: feedbackPostURL,
			processData: false,
			contentType: false,
			type: 'get',
			success: function (res) {
				console.log("success="+res);
			},
			error: function(error) {
				console.log("error="+error);
			},
			complete: function (jqXHR, status) {
			}
		})
	});
	/************************** Contest Section End **************************/
	
	
	/************************** Login/Registration Section Start **************************/
	
	$('.js-signin-start').on('click', function() {
        if (localStorage.rememberme && localStorage.rememberme != '') {
            $('#remember').attr('checked', 'checked');
            $('#login-email').val(localStorage.loginemail);
            $('#login-password').val(localStorage.loginpwd);
        } else {
            $('#remember').removeAttr('checked');
            $('#login-email').val('');
            $('#login-password').val('');
        }
    });
	
	$('form[id="login-form"]').validate({
		rules: {    
			password : {
				minlength : 6
			}
		},
		messages: {    
			password: {
				minlength: 'Password must be at least 6 characters'
			}
		}
	});
	
	$('#login-form').on('submit', function(e){

		e.preventDefault();

		if($('#login-form').valid()) {

			var data = {
					name: $('#login-email').val(),
					password: $('#login-password').val()
			}

			console.log(data);
			loginWithEmailAndPassword(data.name,data.password);
		}
	});
	
	$('#logoff').on('click', function(e){

		e.preventDefault();

		$.LoadingOverlay("show");
		setTimeout(function(){
			$.LoadingOverlay("hide");
		}, delay);		
		localStorage.isloggedin = false;
		$('#user_details').html(null);
		
		showSignInHideLogOff();
		
		location.reload(true);
		
	});
	
	$('#editProfile').on('click', function(e){
		
		e.preventDefault();

		if(validateLoginAndOpenModal('#edit-profile-modal')){
			var employee = { 
					  "name"  	 :  localStorage.loginname, 
					  "emailID"  :  localStorage.loginemail, 
					  "password" :  localStorage.loginpwd 
					}
			var template = $('#ui-template-edit-my-profile').html();
			$('.edit-profile-container').html(Mustache.to_html(template, employee));
			setTimeout(function(){
				$.LoadingOverlay("hide");
			}, delay);
			
			$('#register-form').validate({
				rules : {
					password : {
						minlength : 6
					},
					confirm_password : {
						minlength : 6,
						equalTo : "#reg-password"
					}
				}
			});
		}
		
	});
	
	//$('input[name="account-file"]').change(function(e) {
	$(document).on("change", 'input[name=account-file]', function(e) { 
	    var file = e.target.files[0];
	    $.canvasResize(file, {
	        width: 300,
	        height: 0,
	        crop: false,
	        quality: 80,
	        callback: function(data, width, height) {
	        	userImage=data;
	        }
	    });
	});
	
	function b64toBlob(b64Data, contentType, sliceSize) {
        contentType = contentType || '';
        sliceSize = sliceSize || 512;

        var byteCharacters = atob(b64Data);
        var byteArrays = [];

        for (var offset = 0; offset < byteCharacters.length; offset += sliceSize) {
            var slice = byteCharacters.slice(offset, offset + sliceSize);

            var byteNumbers = new Array(slice.length);
            for (var i = 0; i < slice.length; i++) {
                byteNumbers[i] = slice.charCodeAt(i);
            }

            var byteArray = new Uint8Array(byteNumbers);

            byteArrays.push(byteArray);
        }

      var blob = new Blob(byteArrays, {type: contentType});
      return blob;
    }
	
	$(document).on("submit", '#register-form', function(e) { 

		e.preventDefault();

		if($('#register-form').valid()) {

			var data = {
					name: $('#reg-name').val(),
					email: $('#reg-email').val(),
					password: $('#reg-password').val(),
					confirm_password :$('reg-confirm-password').val(),
					foodpref: $('input[name="options"]:checked').val(),
					image: $('input[name="account-file"]').get(0).files[0]
			}
			
			var imageToUpload;
			
			if(null==userImage){
				imageToUpload=data.image;
			}else{
				var block = userImage.split(";");
				// Get the content type
				var contentType = block[0].split(":")[1];// In this case "image/gif"
				// get the real base64 content of the file
				var realData = block[1].split(",")[1];// In this case "iVBORw0KGg...."
				
				// Convert to blob
				imageToUpload = b64toBlob(realData, contentType);
				
			}
			if(imageToUpload && imageToUpload.size > 0){
				var imageSizeinKB=imageToUpload.size/1024;
				if(imageSizeinKB > 512){
			       $('#error-panel-edit-profile').html("File is too big!!. Please try with different photo");
			       return;
			    };
			}
			
			var fd = new FormData();
			fd.append('photoFile', imageToUpload);

			console.log(data);
			var url=connectionURL+'/registerEmployee/'+data.name+'/'+data.email+'/'+data.password;
			console.log(url);
			
			$.LoadingOverlay("show");
			$.ajax({
				url: url,     
				data: fd,
				processData: false,
				contentType: false,   
				enctype: 'multipart/form-data', 
				type: 'post',
				success: function (employee) {
					console.log(employee);
					if(employee){
						$('.close-modal').click();
						
						var template1 = $('#ui-template-account-details').html();
						var template2 = $('#ui-template-sign-out-and-account-photo').html();
						var template3 = $('#ui-template-edit-profile-menu').html();
						$('#user_details').html(Mustache.to_html(template1, employee));
						$('#logoff').html(Mustache.to_html(template2, employee));
						$('#editProfile').html(Mustache.to_html(template3, employee));
						
						//clear local storage
						localStorage.clear();
						//save into local storage
						localStorage.loginemail = employee.emailID;
						localStorage.loginpwd = data.password;
						localStorage.loginname=employee.name;
						localStorage.photo=employee.photo;
						localStorage.isloggedin = true;
						localStorage.rememberme = true;
						
						hideSignInShowLogOff();
					}else{
						$('#error-panel-edit-profile').html("Ooops!! Registration failed. Re-check your input data!!");
					}
				},
				error: function(error) {
					console.log(error);
					$('#error-panel-edit-profile').html("Ooops!! Registration failed. Re-check your input data!!");
					if(error.responseJSON && error.responseJSON.message){
						$('#error-panel-edit-profile').html(error.responseJSON.message);
					}
				},
				complete: function (jqXHR, status) {
					setTimeout(function(){
					    $.LoadingOverlay("hide");
					}, delay);
				}
			})

		}
	});
	/************************** Login/Registration Section End **************************/
});
//'https://api.myjson.com/bins/ux73o', 'https://api.myjson.com/bins/ncltw'
