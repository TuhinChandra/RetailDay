angular
		.module('kfApp', [ 'cgBusy', 'ngSanitize' ])
		// .config(
		// function($sceDelegateProvider) {
		// $sceDelegateProvider.resourceUrlWhitelist([ 'self',
		// 'https://kfplc-my.sharepoint.com/**' ]);
		// })
		.controller(
				'kfController',
				function($scope, $http, $sce) {

					var URL = '';
					$scope.randomColors = [];
					for (var i = 0; i < 30; i++)
						$scope.randomColors.push(Math
								.floor((Math.random() * 5) + 1));

					var socket = new SockJS('/eventWS');
					stompClient = Stomp.over(socket);
					stompClient.connect({}, onConnected, onError);

					$scope.showEventList = true;
					$scope.showComment = false;
					$scope.currentEvent = 0;
					$scope.comments = [];

					$scope.promise = $http.get(URL + '/getAllEvents').then(
							function mySuccess(response) {
								$scope.events = response.data;
								console.log($scope.events);
							}, function mySuccess(response) {
								window.alert('Oops! Some error has occured!');
								console.log(response);
								return;
							});

					$scope.startEvent = function(event) {
						$scope.promise = $http
								.get(
										URL + '/changeEventState/'
												+ event.eventID + '/RUNNING')
								.then(
										function mySuccess(response) {
											console.log('setCurrentEvent: '
													+ event.eventID);
											$scope.currentEvent = event;
											$scope.trustAsResourceUrl = $sce
													.trustAsResourceUrl($scope.currentEvent.pptPath);
											$scope.showEventList = false;
											$scope.showComment = true;
											$scope.comments = [];
											$scope.$digest();
										},
										function mySuccess(response) {
											window
													.alert('Oops! Some error has occured!');
											console.log(response);
											return;
										});
					}

					function onConnected() {
						console.log('onConnected()');
						stompClient.subscribe('/topic/broadcastLatestComments',
								onMessageReceivedComment);
					}

					function onError(error) {
						console.log('onError()');
						console.log(error);
						stompClient.connect({}, onConnected, onError);
					}

					function onMessageReceivedComment(payload) {
						$scope.latestComments = JSON.parse(payload.body);
						console.log($scope.latestComments);
						$scope.$digest();
					}

					$scope.endEvent = function(event) {

						console.log('completeEvent: ' + event.eventID);
						$scope.promise = $http
						.get(URL + '/getAllEvents')
						.then(
								function mySuccess(response) {
									$scope.showEventList = true;
									$scope.showComment = false;
									$scope.events = response.data;
									console.log($scope.events);
									$scope.$digest()
								},
								function mySuccess(response) {
									window
									.alert('Oops! Some error has occured!');
									console.log(response);
									return;
								});

					}

				});