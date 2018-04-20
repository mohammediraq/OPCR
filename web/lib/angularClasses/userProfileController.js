


angulerRouterApp.controller('ng0userProfile', function ($scope, $http) {


    var userEmail = sessionStorage.getItem('email');

    $http.get('API_getUserProfileByMail?userEmail=' + userEmail).then(function (response) {

        $scope.userProfileObj = response.data[0];
        console.log($scope.userProfileObj);



        $scope.userID = $scope.userProfileObj.usr_id;
        $scope.userName = $scope.userProfileObj.usr_first_name + " " + $scope.userProfileObj.usr_last_name;
        $scope.userEmail = $scope.userProfileObj.usr_email;
        $scope.userMobile = $scope.userProfileObj.usr_mobile;
        $scope.userCity = $scope.userProfileObj.usr_city;
        $scope.userGender = $scope.userProfileObj.usr_gender;
        $scope.userEducation = $scope.userProfileObj.usr_mainclass;
        $scope.userMajor = $scope.userProfileObj.usr_class;
        $scope.userFOS = $scope.userProfileObj.usr_subclass;
        $scope.userSkills = $scope.userProfileObj.usr_skills;
        sessionStorage.setItem("id",$scope.userID);
        sessionStorage.setItem("subclass",$scope.userFOS);
        $scope.uv = sessionStorage.getItem('id');


    });


// onload function.
    $scope.onload = function () {
        
        var sessionAuth = sessionStorage.getItem("Auth");

        if (sessionAuth !== "Success" )
        {
            console.log(sessionAuth);
            window.location.replace("login.html");
        }
        
     

    };
    $scope.onload();
});

