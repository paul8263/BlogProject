/**
 * Created by paulzhang on 27/03/15.
 */
angular.module('app.home',['ui.router','textAngular']).config(function ($stateProvider) {
    $stateProvider.state( 'home', {
        url: '/home',
        controller: 'homeCtrl',
        templateUrl: 'home/home.tpl.html',
        data:{ pageTitle: 'Home' }
    });
}).controller('homeCtrl', function ($scope,$state,blogResource,userLoginStatus,basePath) {

    $scope.isLoggedIn = $scope.$parent.isLoggedIn;
    $scope.currentPage = 1;

    blogResource.getAllBlogs({page:0,size:10},function(data) {
        alert("success");
        $scope.blogList = data;
    },function() {
        alert("failure");
    });

    $scope.pageChange = function() {
        blogResource.getAllBlogs({page:$scope.currentPage-1,size:10},function(data) {
            alert("success");
            $scope.blogList = data;
        },function() {
            alert("failure");
        });
    };

    $scope.onlyViewMine = 0;

    $scope.onlyViewMineToggle = function () {
        if($scope.onlyViewMine == 1) {
            blogResource.getAllBlogsByUser(userLoginStatus.getUserId(),{page:$scope.currentPage-1,size:10},function(data) {
                alert("success");
                $scope.blogList = data;
                $scope.currentPage = 0;
            },function() {
                alert("failure");
            });
        } else {
            blogResource.getAllBlogs({page:$scope.currentPage-1,size:10},function(data) {
                alert("success");
                $scope.blogList = data;
                $scope.currentPage = 0;
            },function() {
                alert("failure");
            });
        }
    };

    $scope.viewBlog = function(ownerId,blogId) {
        $state.go('viewBlog',{userId:ownerId,blogId:blogId});
    };

    $scope.userIconPath = function(userId) {
        return basePath + "user/" + userId + "/icon";
    };

});