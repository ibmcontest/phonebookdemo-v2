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

(function () {
    'use strict';

    describe('phonebook', function () {

        var scope, createCtrl, $httpBackend;
        var PHONEBOOK_API = 'api/phonebook';

        beforeEach(function () {
                module('phonebook');

            inject(function ($rootScope, $controller, _$httpBackend_) {
                scope = $rootScope.$new();
                $httpBackend = _$httpBackend_;
                
                $httpBackend.when('GET', PHONEBOOK_API)
                	.respond({"entries": [ { 
                		"id":1,
                		"title":"X",
                		"firstName":"X",
                		"lastName":"X",
                		"phoneNumber":"X"
                	}]});

                createCtrl = function () {
                	// The controller always start with a GET all
                	$httpBackend.expectGET(PHONEBOOK_API);
                	
                    return $controller('PhonebookList', {$scope: scope});
                };
            });
        });

        afterEach(function () {
        	$httpBackend.flush();
        	
        	$httpBackend.verifyNoOutstandingExpectation();
            $httpBackend.verifyNoOutstandingRequest();
        });

        it('just a dummy test', function () {
        	var ctrl = createCtrl();
            expect(true).toEqual(true);
        });

    });

})();