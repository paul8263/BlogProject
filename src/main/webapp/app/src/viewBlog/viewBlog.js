/**
 * Created by paulzhang on 16/04/15.
 */
angular.module('app.viewBlog',['ui.router','textAngular']).config(function($stateProvider) {
    $stateProvider.state('viewBlog', {
        url: '/viewBlog/:userId/:blogId',
        templateUrl: 'viewBlog/viewBlog.tpl.html',
        controller: 'viewBlogCtrl',
        data: {pageTitle: 'View Blog'}
    });
}).controller('viewBlogCtrl',function($scope,$state,$stateParams,blogResource,userLoginStatus,userBlogCommentResource) {
    $scope.comment = {};
    blogResource.getOneBlog($stateParams.userId,$stateParams.blogId,function(data) {
        alert("success");
        $scope.blog = data;
    }, function() {
        alert("failure");
    });

    $scope.modifiable = $stateParams.userId == userLoginStatus.getUserId();

    $scope.modify = function() {
        $state.go('createModifyBlog',{blogId: $scope.blog.blogId});
    };

    $scope.delete = function() {
        var result = confirm("Do you really want to delete?");
        if(result == true) {
            blogResource.deleteBlog(userLoginStatus.getUserId(),$scope.blog.blogId,function() {
                alert("success");
                $state.go('home');
            },function() {
                alert("failure");
            });
        }
    };
    userBlogCommentResource.getOneComment(userLoginStatus.getUserId(),$stateParams.blogId,function(data) {
        if(data == null || data == '') {
            $scope.commentable = true;
        } else {
            $scope.commentable = false;
        }
    }, function() {
        alert("failure");
        $scope.commentable = true;
    });

    userBlogCommentResource.getAllByBlog($stateParams.blogId,{page:0,size:5},function(data) {
        $scope.userBlogCommentList = data;
    }, function() {
        alert("Retrieve comment failure");
    });

    $scope.pageChange = function() {
        userBlogCommentResource.getAllByBlog($stateParams.blogId,{page:$scope.currentPage - 1,size:5},function(data) {
            $scope.userBlogCommentList = data;
        }, function() {
            alert("Retrieve comment failure");
        });
    };

    $scope.submitComment = function() {
        $scope.comment.commentDate = new Date();
        userBlogCommentResource.addComment(userLoginStatus.getUserId(),$stateParams.blogId,$scope.comment, function() {
            //alert("success");
            userBlogCommentResource.getAllByBlog($stateParams.blogId,{page:0,size:5},function(data) {
                $scope.userBlogCommentList = data;
                $scope.commentable = false;
            }, function() {
                alert("Retrieve comment failure");
            });
        },function() {
            alert("falure");
        });
    }

});