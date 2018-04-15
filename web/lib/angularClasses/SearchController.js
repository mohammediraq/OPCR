/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


angulerRouterApp.controller('Search_Controller', function ($scope, $http) {



//search function 
    $scope.getCourseByProfile = function (ng0searchKey, ng0minNSS, ng0Search_CourseFees) {


//    =>ng0courseMinFees,ng0courseMaxFees,ng0minNSS,ng0user_major,ng0user_region,ng0user_subclass,ng0userIdentification,ng0searchKey		
        $scope.showProgress = true;
//    get user props 

        $http.get('API_getUserByID?userId=' + sessionStorage.getItem('id') + '').then(function (userObject) {
            $scope.rateMessage = "click to rate";
            //search for courses with user props;
//userMajor = usr_education_background
//userSubclass 	 =usr_subclass
//userSearchkey = ng0searchKey	
//userRegion 	=  usr_region
//minimumNSS 	=	ng0minNSS
//minFees 	=	ng0Search_CourseFees
//maxFees 	= null ( not used )


///API_courseSearch?methodName=searchCourse&key=Computer%20Graph&usrid=AHMSH0001&sc=Computer%20Graphics&ur=Jersey&um=Computer%20Programming&nss=55&coursemin=8000&coursemx=10000
            $http.get('API_courseSearch?methodName=searchCourse&key=' + ng0searchKey.replace(" ", "%20") + '&usrid=' + userObject.data[0].usr_id + '&sc=' + userObject.data[0].usr_subclass.replace(" ", "%20") + '&ur=' + userObject.data[0].usr_region.replace(' ', '%20') + '&um=' + userObject.data[0].usr_education_background + '&nss=' + ng0minNSS + '&coursemin=2000&coursemx=' + ng0Search_CourseFees + '').then(function (courseResponse) {
//            courseResponse

                if (courseResponse.status === 200) {
                    $scope.showProgress = false;
                    $scope.ng0show_resultstable = true;
                    $scope.recommendedCourses = courseResponse.data;
                    console.log(courseResponse.data);

                    $http.get('API_listAllJobs?sc=' + sessionStorage.getItem("subclass").replace(" ", "%20") + '').then(function (Jobsresponse) {
                        if (Jobsresponse.status === 200)
                        {
                            $scope.JobsObject = Jobsresponse.data;
                            console.log($scope.JobsObject);
                            console.log('API_listAllJobs?sc=' + sessionStorage.getItem("subclass").replace(" ", "%20") + '');
                        }
                    });

                    console.log("response while calling get courses by id api.." + "==> " + courseResponse.status);

                } else {
                    console.log("response while calling get courses by id api.." + "==> " + courseResponse.status);
                }
            });

        });


    };
//   <span class="fa fa-star checked"></span>


    $scope.myFunction = function (id)
    {
        var popup = document.getElementById("myPopup" + id + "");
        console.log("myPopup" + id + "");
        popup.classList.toggle("show");
    };
    
    $scope.rateCourse = function (id, rate)
    {
        $http.get('API_courseSearch?methodName=scoreCourse&usrid=' + sessionStorage.getItem('id') + '&courseid=' + id + '&courseScore=' + rate + '').then(function (response) {
            if (response.status === 200)
            {

            } else
            {

            }
        })

    };

// onload function.
    $scope.onload = function () {

        var sessionAuth = sessionStorage.getItem("Auth");

        if (sessionAuth !== "Success" )
        {
            console.log(sessionAuth);
            window.location.replace("login.html");
        }


        $scope.ng0show_resultstable = false;
        $scope.showProgress = false;
    };
    $scope.onload();
});