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
  $scope.authkey = $location.search().key;
  var id = 0;
  $scope.auth = {
    valid: false
  };
  $scope.entry = {
    title: "",
    firstName: "",
    lastName: "",
    phoneNumber: "",
    email: ""
  };
  loadEntries();

  function loadEntries() {
    $http.get('api/v2/phonebook?Authorization=' + $scope.authkey).success(function(data) {
      $scope.auth.valid = true;
      $scope.entries = data.entries;
    }).error(function(data, status) {
      $scope.auth.valid = false;
    });
  }

  $scope.loadKey = function() {
    loadEntries();
    $scope.attemptedLoad = true;
  };
  $scope.createKey = function() {
    $http.post('api/v2/user', {}).then(function(data) {
      $scope.authkey = data.data.userkey;
      loadEntries();
    });
  };

  $scope.loadEntry = function() {
    if (id) {
      $http.get('api/v2/phonebook/' + id + "?Authorization=" + $scope.authkey).success(function(data) {
        $scope.entry = data;
      });
    } else {
      $scope.entry = {
        title: "",
        firstName: "",
        lastName: "",
        phoneNumber: "",
        email: ""
      };
    }

  };

  $scope.setId = function(_id) {
    id = _id;
  };

  $scope.setFavorite = function(setting) {
    if (id) {
      $http.post('api/v2/phonebook/favorites/' + id + '?Authorization=' + $scope.authkey + '&setting=' + setting).then(function(data) {
        loadEntries();
      });
    } else {}
  };

  $scope.remove = function() {

    $http['delete']('api/v2/phonebook/' + id + "?Authorization=" + $scope.authkey).then(function(data) {
      loadEntries();
    });

  };

  $scope.submit = function() {
    if (id) {
      $http.put('api/v2/phonebook/' + id + "?Authorization=" + $scope.authkey, {
        title: $scope.entry.title,
        firstName: $scope.entry.firstName,
        lastName: $scope.entry.lastName,
        phoneNumber: $scope.entry.phoneNumber,
        email: $scope.entry.email
      }).then(function(data) {
        loadEntries();
      });
    } else {
      $http.post('api/v2/phonebook?Authorization=' + $scope.authkey, {
        title: $scope.entry.title,
        firstName: $scope.entry.firstName,
        lastName: $scope.entry.lastName,
        phoneNumber: $scope.entry.phoneNumber,
        email: $scope.entry.email
      }).then(function(data) {
        loadEntries();
      });
    }
  };

});
