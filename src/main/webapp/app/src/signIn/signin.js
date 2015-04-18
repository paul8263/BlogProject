/**
 * Created by paulzhang on 30/03/15.
 */
angular.module('app.signIn', ['ui.router']).config(function ($stateProvider) {
    $stateProvider.state( 'signin', {
        url: '/signin',
        controller: 'signInCtrl',
        templateUrl: 'signIn/signIn.tpl.html',
        data:{ pageTitle: 'Sign In' }
    });
}).controller('signInCtrl', function ($scope,$state, sessionService,userLoginStatus, userResource) {
    $scope.user = {};
    $scope.submit = function () {
        sessionService.login($scope.user.username,$scope.user.password,function() {
            userResource.getOneUserByUsername($scope.user.username,function(data) {
                userLoginStatus.login(data.userId);

                $scope.$parent.isLoggedIn = true;
                $scope.$parent.userId = data.userId;

                alert("Hello " + $scope.user.username);
                alert("Hello " + data.userId);

                $state.go("home");

            }, function() {
                alert("error finding user");
            });
        }, function() {
            alert("login fail");
        });
    };
});