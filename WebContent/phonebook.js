
var app = angular.module('phonebook', ['ngRoute']);


app.config(['$routeProvider', function ($routeProvider, $locationProvider) {
	$routeProvider
	.when('/', {
		templateUrl: 'list.html',
		controller: 'PhonebookList as phonebookList'
	})
	.when('/edit/:entryID', {
		templateUrl: 'edit.html',
		controller: 'PhonebookEdit as phonebookEdit'
	})
	.when('/add', {
		templateUrl: 'edit.html',
		controller: 'PhonebookAdd as phonebookAdd'
	})
	.otherwise({ redirectTo: '/' });
}]);

app.controller('PhonebookList', function($scope, $http, $location) {
	
	console.log("list");
	$http.get('api/phonebook').success(function(data) {
		$scope.entries = data.entries;
	});
	
	$scope.remove = function (_id, _index) {
		$http['delete']('api/phonebook/'+ _id);
		 $scope.entries.splice(_index, 1);
	};

});

app.controller('PhonebookEdit', function($scope, $http, $location, $routeParams) {
	
	var id = $routeParams.entryID;
	$http.get('api/phonebook/'+id).success(function(data) {
		$scope.entry = data;
	});
	
	$scope.submit = function() {
		$http.put('api/phonebook/'+id, {title: $scope.entry.title, firstName: $scope.entry.firstName, lastName: $scope.entry.lastName, phoneNumber: $scope.entry.phoneNumber}).then(function(data) {
	          $location.path('#/');
	      });
	};
	
});

app.controller('PhonebookAdd', function($scope, $http, $location) {
	
	$scope.entry = {title: "", firstName: "", lastName: "", phoneNumber: ""};
	
	$scope.submit = function() {
		$http.post('api/phonebook', {title: $scope.entry.title, firstName: $scope.entry.firstName, lastName: $scope.entry.lastName, phoneNumber: $scope.entry.phoneNumber}).then(function(data) {
	          $location.path('#/');
	      });
	};
});
