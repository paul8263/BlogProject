/**
 * Created by paulzhang on 15/04/15.
 */
angular.module('app.createModifyBlog',['ui.router','textAngular']).config(function($stateProvider,$provide) {
    $stateProvider.state('createModifyBlog', {
        url: '/createModifyBlog/:blogId',
        templateUrl: 'createModifyBlog/createModifyBlog.tpl.html',
        controller: 'createModifyBlogCtrl',
        data: {pageTitle: 'Create Blog'}
    });
    $provide.decorator('taOptions',['$delegate', function (taOptions) {
        taOptions.toolbar = [
            ['h1', 'h2', 'h3', 'h4', 'h5', 'h6', 'p', 'pre', 'quote'],
            ['bold', 'italics', 'underline', 'strikeThrough', 'ul', 'ol', 'redo', 'undo', 'clear'],
            ['justifyLeft', 'justifyCenter', 'justifyRight', 'indent', 'outdent'],
            ['html', 'insertImage','insertLink', 'wordcount', 'charcount']
        ];
        return taOptions;
    }]);
}).controller('createModifyBlogCtrl', function ($scope,$state,$stateParams,blogResource,userLoginStatus) {

    if($stateParams.blogId == '' || $stateParams.blogId == null) {
        $scope.createModifyBlogText = "Create Blog";
    } else {
        $scope.createModifyBlogText = "Modify Blog";
    }


    if($stateParams.blogId == '' || $stateParams.blogId == null) {
        $scope.blog = {};
    } else {
        blogResource.getOneBlog(userLoginStatus.getUserId(),$stateParams.blogId,function(data) {
            alert("success");
            $scope.blog = data;
        }, function() {
            alert("failure");
        });
    }

    $scope.submit = function () {

        if($stateParams.blogId == null || $stateParams.blogId == '') {
            $scope.blog.createDate = new Date();
            $scope.blog.modifyDate = new Date();
            blogResource.addBlog(userLoginStatus.getUserId(),$scope.blog,function(data) {
                alert("create success");
                $state.go('viewBlog',{userId:userLoginStatus.getUserId(),blogId:data.blogId});
            },function() {
                alert("create failure");
            });
        } else {
            $scope.blog.modifyDate = new Date();
            blogResource.updateBlog(userLoginStatus.getUserId(),$scope.blog.blogId,$scope.blog,function(data) {
                alert("modify success");
                $state.go('viewBlog',{userId:userLoginStatus.getUserId(),blogId:data.blogId});
            },function() {
                alert("modify failure");
            });
        }

    };

});