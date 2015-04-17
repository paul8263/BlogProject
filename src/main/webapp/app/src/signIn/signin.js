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
}).controller('signInCtrl', function ($scope) {
    $scope.user = {};
    $scope.submit = function () {
        alert($scope.user.username);
        alert($scope.user.password);
    }
});