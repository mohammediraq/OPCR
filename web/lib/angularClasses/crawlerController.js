


angulerRouterApp.controller('ng0crawlerController', function ($scope, $http) {

    $scope.jobs_getAllJobs = function(k,sc)
    {
        $scope.message ="Working on your request,it might take a while, you will be notified once it's finished... ";
        $http.get('API_getJobs?k='+k+'&sc='+sc+'').then(function(rs){
            $scope.response  = rs.data[0].Status;
            console.log($scope.response);
            if(rs.status === 200)
            {
            $scope.message ="Jobs were added successfully.";
            }
            else
            {
                $scope.message ="something is wrong, please contact your administrator \n response: "+rs.status+"";
            }
        });
    };

    $scope.getUCAScourses = function (k, ro, m, s) {
        $scope.notification = "Working on your request,it might take a while, you will be notified once it's finished...";
        $http.get('API_getCourses?searchKey=' + k + '&rootClass=' + ro + '&mainClass=' + m + '&subClass=' + s+ '').then(function (r) {
            $scope.responseObj = r.data;
            console.log( $scope.responseObj);
            $scope.notification = "Courses were added successfully.";
        });
    };



    $scope.setroot = function (r)
    {
        $scope.ng0Root = r;
        $scope.allClassesObj = "";
        console.log(r);
    }
    $scope.setmain = function (m)
    {
        $scope.ng0Main = m;
        $scope.allMainObj = "";
        console.log(m);
    }
    $scope.setsub = function (s)
    {
        $scope.ng0Sub = s;
        $scope.allSubObj = "";
        console.log(s);
    }

//    onchange root
    $scope.onchangeRoot = function ()
    {
        $scope.allClassesObj = "";

        $http.get('API_getAllClasses').then(function (r) {

            $scope.allClassesObj = r.data;
        });
    };

    $scope.onchangeMain = function ()
    {
        $scope.allMainObj = "";

        $http.get('API_getAllClasses').then(function (m) {

            $scope.allMainObj = m.data;
        });
    };

    $scope.onchangeSub = function ()
    {
        $scope.allSubObj = "";

        $http.get('API_getAllClasses').then(function (s) {

            $scope.allSubObj = s.data;
        });
    };



// onload function.
    $scope.onload = function () {
        var sessionAuth = sessionStorage.getItem("Auth");

        if (sessionAuth !== "Success" || sessionAuth !== "Admin")
        {
            console.log(sessionAuth);
            window.location.replace("login.html");
        }

    };
    $scope.onload();
});