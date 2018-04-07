


angulerRouterApp.controller('ng0loginController', function ($scope, $http) {





//Auth

    $scope.LoginAuth = function (u, p) {
        $http.get("API_loginAuth?userEmail=" + u + "&userPassword=" + p + "").then(function (response) {
            console.log(response);

            $scope.userAuth = response.data[0].Auth;

            if ($scope.userAuth === "Success")
            {
                //Auth Success
                //redirected to profile view

                sessionStorage.setItem("Auth", "Success");
                sessionStorage.setItem("email", u);
                window.location.replace('userProfile.html');


            }
            
             if ($scope.userAuth === "Success-ad")
            {
                sessionStorage.setItem("Auth", "Success");
               sessionStorage.setItem("AdminAuth", "Admin");
                sessionStorage.setItem("email", u);
                window.location.replace('userProfile.html');
 
            }
            
            else
            {
                //incorrect password
                $scope.showm = true;
                $scope.m = "Incorrect Password";

            }

        });


    };


//validate user mail
    $scope.validateUserMail = function (m) {
        $scope.disableButton = false;
        $http.get("API_getUserEmail?userEmail=" + m).then(function (response) {

//            
            $scope.emailExistense = response.data[0].found;

            if ($scope.emailExistense !== 0)
            {

                $scope.validationClass = "success";
                $scope.validationMsg = "Valid E-mail , Please Enter your password!";
                $scope.show_ValidationMsg = true;
                $scope.disableButton = false;

            } else
            {

                $scope.validationClass = "danger";
                $scope.validationMsg = "Email not registered , please register first,it's free!  ";
                $scope.show_ValidationMsg = true;
                $scope.disableButton = true;

            }



        });
    };
// onload function.
    $scope.onload = function () {
         //incorrect password
                $scope.showm = false;
        sessionStorage.setItem('id', '');
        sessionStorage.setItem("Auth", "");
        sessionStorage.setItem("AdminAuth", "");
        sessionStorage.setItem("email", "");
        sessionStorage.setItem("subclass", "");


    };
    $scope.onload();
});