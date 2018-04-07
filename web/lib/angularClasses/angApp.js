/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


var angulerRouterApp = angular.module('angulerRouter', ["ngRoute","ngSanitize"]);

angulerRouterApp.config(function ($routeProvider) {
    $routeProvider

            .when("/showClasses", {
                templateUrl: "classesConf.html"
            })
            .when("/modifyClasses", {
                templateUrl: "classesMod.html"
            })
            .when("/viewInterests", {
                templateUrl: "interestsView.html"
            })
            .when("/addInterests", {

                templateUrl: "interestsMod.html"
            })
              .when("/viewWeights", {

                templateUrl: "weightsView.html"
            })
             .when("/viewlangs", {

                templateUrl: "languagesView.html"
            })
                 .when("/addlangs", {

                templateUrl: "langMod.html"
            })
                    .when("/viewCourseLevels",{
                        templateUrl:"coursesLevelConf.html"
            })
            
            .when ("/viewCourseWeights",{
                    templateUrl:"courseItemWeight.html"
            })
                  .when("/editCourseLevels",{
                        templateUrl:"coursesLevelsMod.html"
            })
                  .when("/viewConfig",{
                        templateUrl:"viewConfig.html"
            })
         
});

var angularFilterApp = angular.module('angularFilter', ['angular.filter']);

