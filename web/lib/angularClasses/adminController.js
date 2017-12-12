/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

angulerRouterApp.controller('angulerAdmin', function ($scope, $http) {

// call get all classes API 
    $scope.getAllClasses = function () {
        $http.get('/Ontology/API_getAllClasses').then(function (response) {
            $scope.classesArray = response.data;




        });
    };
//    

//insert new class conf 

    $scope.insertClass = function (cn, cl, scl) {
        $http.get("/Ontology/API_InsClassConf?careerName=" + cn + "&className=" + cl + "&subClassName=" + scl).then(function (response) {

        });
        $scope.disableButton = true;
    };

    $scope.newAction = function () {
        $scope.disableButton = false;

    };
//
    $scope.getAllInterests = function () {
        $http.get('/Ontology/API_getAllInterests').then(function (response) {
            $scope.InterestsArray = response.data;

        });
    };
//Add new Interest
    $scope.addNewInterest = function (n) {
        $http.get('/Ontology/API_InsAreaOfInterest?an=' + n).then(function (response) {

        });
        $scope.disableButton = true;
    };
//    get all weights 
    $scope.getAllWeights = function () {
        $http.get('/Ontology/API_getItemWeight').then(function (response) {
            $scope.weightsArray = response.data;

        });
    };

//    validate item weight.

    $scope.InsertItemWeight = function (it, wv) {
        $scope.currentWeightsArray = "";
        $scope.niwr = false;
        $http.get('/Ontology/API_insItemWeight?it=' + it + '&pr=' + wv + '').then(function (response) {
            $scope.currentWeightsArray = response.data;
            console.log($scope.currentWeightsArray);
        });

        $scope.getAllWeights();
        $scope.niwr = true;

    };


// insert new item
    $scope.updateItemWeight = function (rec, it, wv) {

        $scope.updateWeightsArray = "";
        $http.get('/Ontology/API_updateItemWeight?rec=' + rec + '&it=' + it + '&pr=' + wv + '').then(function (response) {
            $scope.updateWeightsArray = response.data;
            console.log($scope.updateWeightsArray);

        });
        $scope.getAllWeights();
    };


//    get all languages 
    $scope.getAllLanguages = function () {
        $http.get('/Ontology/API_getalllangs').then(function (response) {
            $scope.langsArray = response.data;

        });
    };

//    add language 
    $scope.insertLang = function (l) {
        $http.get('/Ontology/API_insLanguage?ln=' + l).then(function (response) {


        });
        $scope.disableButton = true;
    };

//   get courses levels
    $scope.getAllLevels = function () {
        $http.get('/Ontology/API_getCourselvl').then(function (response) {
            $scope.levelsArray = response.data;

        });
    };

    $scope.insertCourseLevel = function (ln, lo) {
        $http.get('/Ontology/API_insCourselvl?lv=' + ln + '&lo=' + lo + '').then(function (response) {
            console.log(response.data);
            console.log(response.status);
        });
        $scope.disableButton = true;
    };



    $scope.showUpdatePanel = function (rn) {
        $scope.showEditPanel = false;
        var rowIndex = rn + 1;
        var itemName_el = $("body > div > div > section > div > table > tbody > tr:nth-child(" + rowIndex + ") > td:nth-child(2)");
        var itemWeight_el = $("body > div > div > section > div > table > tbody > tr:nth-child(" + rowIndex + ") > td:nth-child(3)");
        $scope.rind = rowIndex;
        $scope.itn = itemName_el.text();
        $scope.iwe = itemWeight_el.text();
        $scope.showEditPanel = true;



    };


    $scope.deleteClass = function (rec)
    {
        var rowIndex = rec + 1;
        $http.get('/Ontology/API_removeRecord?t=conf_classes&r=' + rec).then(function (response) {
            $scope.getAllClasses();
        });
         $http.get('/Ontology/API_removeRecord?t=conf_subclasses&r=' + rec).then(function (response) {
            $scope.getAllClasses();
        });


    };

    $scope.deleteInterest = function (rec)
    {
        var rowIndex = rec + 1;
        $http.get('/Ontology/API_removeRecord?t=conf_interest_areas&r=' + rec).then(function (response) {
            $scope.getAllInterests();
        });

    };

    $scope.deleteWeight = function (rec)
    {
        var rowIndex = rec + 1;
        $http.get('/Ontology/API_removeRecord?t=usr_item_weight&r=' + rec).then(function (response) {
            $scope.getAllWeights();
        });


    };

    $scope.deleteLanguage = function (rec)
    {
        var rowIndex = rec + 1;
        $http.get('/Ontology/API_removeRecord?t=conf_languages&r=' + rec).then(function (response) {
        });
        $scope.getAllLanguages();
    };

    $scope.deleteCourseLevel = function (rec)
    {
        var rowIndex = rec + 1;
        $http.get('/Ontology/API_removeRecord?t=conf_course_lvl&r=' + rec).then(function (response) {
        $scope.getAllLevels();
        
        });
        
        
    };


});