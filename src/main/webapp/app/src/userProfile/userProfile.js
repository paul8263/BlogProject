/**
 * Created by paulzhang on 14/04/15.
 */
angular.module('app.userProfile',['ui.router']).config(function($stateProvider) {
    $stateProvider.state('userProfile',{
        url: '/userProfile',
        templateUrl: 'userProfile/userProfile.tpl.html',
        controller: 'userProfileCtrl',
        data : { pageTitle: 'User Profile'}
    });
}).controller('userProfileCtrl',function($scope,userLoginStatus,userResource,basePath) {
    $scope.user = {};
    $scope.userIconPath = "";
    userResource.getOneUser(userLoginStatus.getUserId(),function(data) {
        //alert("success");
        $scope.user = data;
        $scope.userIconPath = basePath + "user/" + $scope.user.userId + "/icon";
    }, function () {
        alert("Failure");
    });



});
