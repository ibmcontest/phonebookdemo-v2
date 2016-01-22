/**
 * Copyright 2015 IBM Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

angular.module('phonebook', [])

.controller('PhonebookList', function($scope, $http, $location, $window) {

	var id = 0;
	$scope.entry = {title : "", firstName : "", lastName : "", phoneNumber : "", email: ""};

	$http.get('api/v2/phonebook').success(function(data) {
		$scope.entries = data.entries;
	});

	$scope.loadEntry = function() {

		if (id) {
			$http.get('api/v2/phonebook/' + id).success(function(data) {
				$scope.entry = data;
			});
		} else {
			$scope.entry = {title : "", firstName : "", lastName : "", phoneNumber : "", email : ""};
		}

	};

	$scope.setId = function(_id) {
		console.log("id");
		id = _id;
	};

	$scope.setFavorite = function(setting) {
		console.log("set");
		if (id) {
			console.log("id set to"+id);
			$http.post('api/v2/phonebook/favorites/' +id+'?setting='+setting).then(function(data) {
				$window.location.reload();
			});
		} else {
			console.log("id not set");
		}
	};

	$scope.remove = function() {

		$http['delete']('api/v2/phonebook/' + id).then(function(data) {
			$window.location.reload();
		});

	};

	$scope.submit = function() {
		if (id) {
			$http.put('api/v2/phonebook/' + id, {
				title : $scope.entry.title,
				firstName : $scope.entry.firstName,
				lastName : $scope.entry.lastName,
				phoneNumber : $scope.entry.phoneNumber,
				email : $scope.entry.email
			}).then(function(data) {
				$window.location.reload();
			});
		} else {
			$http.post('api/v2/phonebook', {
				title : $scope.entry.title,
				firstName : $scope.entry.firstName,
				lastName : $scope.entry.lastName,
				phoneNumber : $scope.entry.phoneNumber,
				email : $scope.entry.email
			}).then(function(data) {
				$window.location.reload();
			});
		}
	};

});
