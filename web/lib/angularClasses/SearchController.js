/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


angulerRouterApp.controller('Search_Controller', function ($scope, $http) {


//search function 
    $scope.getCourseByProfile = function (ng0userIdentification, ng0searchKey, ng0minNSS, ng0Search_CourseFees) {

//    =>ng0courseMinFees,ng0courseMaxFees,ng0minNSS,ng0user_major,ng0user_region,ng0user_subclass,ng0userIdentification,ng0searchKey		
        $scope.showProgress = true;
//    get user props 

        $http.get('API_getUserByID?userId=' + ng0userIdentification + '').then(function (userObject) {
          
                //search for courses with user props;
//userMajor = usr_education_background
//userSubclass 	 =usr_subclass
//userSearchkey = ng0searchKey	
//userRegion 	=  usr_region
//minimumNSS 	=	ng0minNSS
//minFees 	=	ng0Search_CourseFees
//maxFees 	= null ( not used )


///API_courseSearch?methodName=searchCourse&key=Computer%20Graph&usrid=AHMSH0001&sc=Computer%20Graphics&ur=Jersey&um=Computer%20Programming&nss=55&coursemin=8000&coursemx=10000
                $http.get('API_courseSearch?methodName=searchCourse&key=' + ng0searchKey.replace(" ", "%20") + '&usrid=' + userObject.data[0].usr_id + '&sc=' + userObject.data[0].usr_subclass.replace(" ", "%20") + '&ur=' + userObject.data[0].usr_region.replace(' ', '%20') + '&um=' + userObject.data[0].usr_education_background + '&nss=' + ng0minNSS + '&coursemin=8000&coursemx=10000').then(function (courseResponse) {
//            courseResponse
                  
                    
                        $scope.showProgress = false;
                        $scope.ng0show_resultstable = true;
                        $scope.recommendedCourses = courseResponse.data;
                        console.log(courseResponse.data);

              
                    
                        console.log("there;s a problem while calling get courses by id api.." + "==> " + courseResponse.status);
                    
                });

        });


    };

// onload function.
    $scope.onload = function () {
        $scope.ng0show_resultstable = false;
        $scope.showProgress = false;
    };
    $scope.onload();
});