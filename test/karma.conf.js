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

/*******************************************************************************
 * Karma configuration file for phonebook demo
 *******************************************************************************/
module.exports = function(config){

    config.set({
        /**
         * From where to look for files, starting with the location of this file.
         */
        basePath : '../',

        /**
         * This is the list of file patterns to load into the browser during testing.
         */
        files : [
            'WebContent/bower_components/jquery/dist/jquery.js',
            'WebContent/bower_components/bootstrap/dist/js/bootstrap.js',
            'WebContent/bower_components/angular/angular.js',

            'WebContent/phonebook.js',

            // Angular mocking dependencies
            'WebContent/bower_components/angular-mocks/angular-mocks.js',

            // Test related files
            'test/utils/**/*.js',
            'test/specs/**/*-spec.js'
        ],

        /**
         * Automatically runs tests when you save any test file
         */
        autoWatch : true,

        /**
         * Default reporter
         */
        reporters : ['mocha'],

        /**
         * Frameworks to use for unit testing
         */
        frameworks : [
          'jasmine'
        ],

        /**
         * Browser to use for running unit tests
         * values: Chrome, Firefox, Opera, Safari, PhantomJS etc.
         */
        browsers : ['PhantomJS'],

        /**
         * Plugins required to run karma
         */
        plugins : [
            'karma-jasmine',
            'karma-mocha-reporter',
            'karma-phantomjs-launcher'
        ],

        /**
         * On which port should the browser connect, on which port is the test runner
         * operating, and what is the URL path for the browser to use.
         */
        port : 9000,
        runnerPort : 9001,
        urlRoot : '/',

        // When true, enables coloured output to stdout
        colors: true

    });

};
