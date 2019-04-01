angular
		.module('kfApp', ['cgBusy','ngSanitize'])
		.controller(
				'kfController',
				function($scope, $http) {

					var URL = '';

					$scope.showStartQuiz = true;
					$scope.showQuestion = false;
					$scope.showResultButton = false;
					$scope.result = false;
					$scope.question = 1;
					$scope.startQuiz = function() {
						$scope.question++;
						if ($scope.question > 6) {
							$scope.promise = $http
									.get(URL + '/clearCurrentQuestion/')
									.then(
											function mySuccess(response) {
												$scope.showQuestion = false;
												$scope.showStartQuiz = false;
												$scope.showResultButton = false;
												$scope.result = false;
												$scope.quizEnd=true;
												$scope.$digest();
											},
											function myError(response) {
												window
														.alert('Oops! Some error has occured!');
												console.log(response);
												return;
											});
							return;
						}
						$scope.promise = $http
								.get(
										URL + '/setCurrentQuestion/'
												+ $scope.question)
								.then(
										function mySuccess(response) {
											$scope.promise = $http
													.get(
															URL
																	+ '/getCurrentQuestionAdmin/')
													.then(
															function mySuccess(
																	response) {
																console
																		.log(response.data);
																$scope.currentQuestionData = response.data;
																$scope.showQuestion = true;
																$scope.showStartQuiz = false;
																$scope.showResultButton = true;
																$scope.result = false;
																$scope
																		.$digest();
															},
															function myError(
																	response) {
																window
																		.alert('Oops! Some error has occured!');
																console
																		.log(response);
																return;
															});
										},
										function myError(response) {
											window
													.alert('Oops! Some error has occured!');
											console.log(response);
											return;
										});
					}

					$scope.showResult = function() {
						$scope.promise = $http
								.get(URL + '/getResult/' + $scope.question)
								.then(
										function mySuccess(response) {
											console.log(response.data);
											$scope.resultData = response.data;
											$scope.showQuestion = false;
											$scope.showResultButton = false;
											$scope.result = true;
											$scope.$digest();
										},
										function myError(response) {
											window
													.alert('Oops! Some error has occured!');
											console.log(response);
											return;
										});
					}
				});