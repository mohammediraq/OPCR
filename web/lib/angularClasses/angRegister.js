/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



angulerRouterApp.controller('regctrl', function ($scope, $http) {

// get all counties to dropdown list
    $scope.bindAllCounties = function () {
        $http.get("API_getAllRegionsCounties").then(function (response) {
//        assign data to this variable
            console.log(response.data);
            $scope.countiesArray = response.data;
        });
    };




    $scope.bindClassesSubClasses = function () {
        $http.get("/Ontology/API_getedubackground").then(function (response) {
//        assign data to this variable
            console.log(response.data);
            $scope.eduArray = response.data;
        });
    };

    $scope.bindAllLanguages = function () {
        $http.get("API_getalllangs").then(function (response) {
//        assign data to this variable
            console.log(response.data);
            $scope.languagesArray = response.data;
        });
    };

    $scope.bindAlleduLevels = function () {
        $http.get("API_getCourselvl").then(function (response) {
//        assign data to this variable
            console.log(response.data);
            $scope.eduLevelArray = response.data;
        });
    };

//    

//validate user mail
    $scope.validateUserMail = function (m) {
        $scope.disableButton = false;
        $http.get("API_getUserEmail?userEmail=" + m).then(function (response) {

//            
            $scope.emailExistense = response.data[0].found;
            if ($scope.emailExistense == 1)
            {
                $scope.validationClass = "danger";
                $scope.validationMsg = "is already registered";
                $scope.disableButton = true;

            } else
            {
                $scope.validationClass = "success";
                $scope.validationMsg = "is a valid address";
            }



        });
    };


// insert profile 
    $scope.insertUserProfile = function (ufn, uln, umn, uad, ucun, uem, umo, uedb, ucq, ulang, uski, fos, cln, scln) {
        $scope.disableButton = false;
        $scope.showResponsePanel = false;

        $http.get('/Ontology/API_insertNewUser?ufn=' + ufn + '&uln=' + uln + '&umn=' + umn + '&uad=' + uad + '&ucun=' + ucun + '&uem=' + uem + '&umo=' + umo + '&uedb=' + uedb + '&ucq=' + ucq + '&ulang=' + ulang + '&uski=' + uski + '').then(function (response) {

            $scope.registrationStatus = response.data;
            console.log($scope.registrationStatus[0].addingUserResponse);
        });

        if (fos !== "undefined" && cln !== "undefined" && scln !== "undefined")
        {
            $http.get("/Ontology/API_InsClassConf?careerName=" + fos + "&className=" + cln + "&subClassName=" + scln).then(function (response) {

            });
        }
     
        $scope.showResponsePanel = true;
        $scope.disableButton = true;
    };


//    

    $scope.onloadFunction = function () {
        $scope.bindClassesSubClasses();
        $scope.bindAllCounties();
        $scope.bindAllLanguages();
        $scope.bindAlleduLevels();
    };


    $scope.onloadFunction();

});

