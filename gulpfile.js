/*jslint node: true*/
'use strict';

var pkg = require('./package.json');
var gulp = require('gulp-help')(require('gulp'), {
  hideEmpty: true,
  hideDepsMessage: true
});
var $ = require('gulp-load-plugins')();
var karma = require('karma');
var path = require('path');

/*
 ****************************************************
 * A place to define variables to various resources
 ****************************************************
 */
var APP_ROOT = 'WebContent';  // root of webpage artifacts
var APP = {
  allHtml: path.join(APP_ROOT, '*.html'), // location of all htmls
  indexHtml: path.join(APP_ROOT, 'index.html') // start page of application
};

var TEST_ROOT = 'test';  // root of test artifacts
var TEST = {
  karmaConf: path.join(TEST_ROOT, 'karma.conf.js'), // name of Karma configuration file
  specfiles: [  										                // Karma unit test files
    path.join(TEST_ROOT, 'specs/**/*spec.js')
  ]
};

/*
 ****************************************************
 * Start of build tasks definition
 ****************************************************
 */
function karmaStartServer (options, done) {
   options.configFile = path.resolve(TEST.karmaConf);
    	
   var server = new karma.Server(options, done);
   server.start();
}

gulp.task('karma:unit', 'Run unit test suite once.', function (done) {
  karmaStartServer ({
    singleRun: true,
    autoWatch: false
  }, done);
}, {
  aliases: ['test']
});