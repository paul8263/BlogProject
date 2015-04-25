/**
 * Created by paulzhang on 28/03/15.
 */
angular.module('app.register',['ui.router']).config(function($stateProvider) {
    $stateProvider.state('register',{
        url: '/register',
        controller: 'registerCtrl',
        templateUrl: 'register/register.tpl.html',
        data: { pageTitle: 'Register' }
    });
}).controller('registerCtrl',function($scope,$state,userResource,userLoginStatus,sessionService) {

    $scope.user = {};
    $scope.user.gender = "MALE";
    $scope.user.birthday = "";

    $scope.open = function($event) {
        $event.preventDefault();
        $event.stopPropagation();

        $scope.opened = true;
    };
    $scope.today = function() {
        $scope.user.birthday = new Date();
    };
    $scope.format = 'dd-MM-yyyy';
    $scope.dateOptions = {
        formatYear: 'yy',
        startingDay: 1
    };


    $scope.submit = function () {

        if($scope.registerForm.$valid) {

            $scope.user.icon = $scope.icon;

            userResource.addUser($scope.user,function(data) {
                $scope.$parent.isLoggedIn = true;
                $scope.$parent.userId = data.userId;

                sessionService.login($scope.user.username,$scope.user.password, function() {
                    alert("security login success");
                }, function() {
                    alert("security login fail");
                });

                userLoginStatus.login(data.userId);

                $state.go('home');
            }, function() {
                alert('Failure');
            });

        }


    };
});