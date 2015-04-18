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
    $scope.user.birthday = "1970-01-01";

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
}).directive('confirmPassword', function () {
    return {
        restrict: 'A',
        require: 'ngModel',
        link: function (scope, element, attr, ctrl) {
            element.on('blur',function() {
                scope.$apply(function () {
                    if(scope.user.password == scope.user.confirmPassword) {
                        ctrl.$setValidity('confirmValid', true);
                    } else {
                        ctrl.$setValidity('confirmValid', false);
                    }
                });

            });

        }
    };
});