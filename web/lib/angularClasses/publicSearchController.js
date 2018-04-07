/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


angulerRouterApp.controller('PublicSearch_Controller', function ($scope, $http) {


   $scope.onload = function () {
            $http.get('API_allCoursesPublic').then(function (courseResponse) {
//            courseResponse


                $scope.showProgress = false;
                $scope.ng0show_resultstable = true;
                $scope.allCourses = courseResponse.data;
                console.log(courseResponse.data);



                console.log("there;s a problem while calling get courses by id api.." + "==> " + courseResponse.status);

            });

        

// onload function.
 
     
        
        
        $scope.ng0show_resultstable = false;
        $scope.showProgress = false;
    };
    $scope.onload();
});