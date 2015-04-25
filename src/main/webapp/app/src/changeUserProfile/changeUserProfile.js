/**
 * Created by paulzhang on 14/04/15.
 */
angular.module('app.changeUserProfile',['ui.router']).config(function($stateProvider) {
    $stateProvider.state('changeUserProfile',{
        url: '/changeUserProfile',
        controller: 'changeUserProfileCtrl',
        templateUrl: 'changeUserProfile/changeUserProfile.tpl.html',
        data: { pageTitle: 'Change User Profile' }
    });
}).controller('changeUserProfileCtrl', function ($scope,$filter,$state,userResource,userLoginStatus) {

    userResource.getOneUser(userLoginStatus.getUserId(), function (data) {
        $scope.user = data;
    });

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

    $scope.changePassword = false;

    $scope.submit = function() {

        if($scope.changeUserProfileForm.$valid) {
            if($scope.icon != undefined || $scope.icon != null) {
                userResource.updateUserIcon($scope.userId,$scope.icon,function() {
                    alert("update image success.");
                }, function() {
                    alert("update image fail");
                });
            }
            userResource.updateUser($scope.user,function(data) {
                alert("success" + data.userId);
                userLoginStatus.login(data.userId);
                $state.go('home');
            }, function() {
                alert("failure");
            });
        }
    }
});