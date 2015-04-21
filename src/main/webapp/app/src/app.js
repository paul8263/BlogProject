angular.module('app',['ui.router','ngAnimate','ngResource', 'ui.bootstrap','app.home','app.signIn','app.register','app.userProfile','app.changeUserProfile','app.createModifyBlog','app.viewBlog']).config(function($stateProvider,$urlRouterProvider) {
    $urlRouterProvider.otherwise('/home');
}).value('basePath','/myblog/').run(function($rootScope,$state) {
    $rootScope.state = $state;
}).factory('userResource',function($resource,basePath) {

    var res = $resource(basePath + 'user/:userId',{userId:'@userId'},{update:{method:'PUT'}});
    var resUsername = $resource(basePath + 'user/username/:username',{username:'@username'});
    return {
        addUser: function(user,success,failure) {
            res.save(user,success,failure);
        },
        updateUser: function(user,success,failure) {
            res.update(user,success,failure);
        },
        getAllUsers: function(pageData,success, failure) {
            res.get(pageData,success,failure);
        },
        deleteUser: function(success, failure) {
            res.delete(success,failure);
        },
        getOneUser: function (userId,success,failure) {
            res.get({userId: userId},success,failure);
        },
        getOneUserByUsername: function (username,success,failure) {
            resUsername.get({username:username},success,failure);
        }
    };

}).factory('blogResource',function($resource,basePath) {
    var res = $resource(basePath + 'user/:userId/blog/:blogId',{userId:'@userId',blogId:'@blogId'},{update:{method:'PUT'}});
    return {
        getAllBlogsByUser: function (userId,pageData,success,failure) {
            res.get({userId: userId,page:pageData['page'],size:pageData['size']},success,failure);
        },
        getOneBlog: function (userId,blogId,success,failure) {
            res.get({userId:userId,blogId:blogId},success,failure);
        },
        addBlog: function(userId,blog,success,failure) {
            blog.userId = userId;
            res.save(blog,success,failure);
        },
        updateBlog: function(userId,blogId,blog,success,failure) {
            blog.userId = userId;
            blog.blogId = blogId;
            res.update(blog,success,failure);
        },
        deleteBlog: function (userId,blogId,success,failure) {
            res.delete({userId:userId,blogId:blogId},success,failure);
        },
        getAllBlogs: function(pageData,success,failure) {
            var getAllBlogsRes = $resource(basePath + 'blog',{});
            getAllBlogsRes.get({page:pageData['page'],size:pageData['size']},success,failure);
        }
    };

}).factory('userBlogCommentResource',function($resource,basePath) {
    var resGetAllByUser = $resource(basePath + 'comment/user/:userId',{userId:'@userId'});
    var resGetAllByBlog = $resource(basePath + 'comment/blog/:blogId',{blogId:'@blogId'});
    var res = $resource(basePath + 'comment/user/:userId/blog/:blogId',{userId:'@userId',blogId:'@blogId'},{update:{method:'PUT'}});
    return {
        getAllByUser: function (userId,pageData,success,failure) {
            resGetAllByUser.get({userId:userId,page:pageData['page'],size:pageData['size']},success,failure);
        },
        getAllByBlog: function (blogId, pageData, success, failure) {
            resGetAllByBlog.get({blogId:blogId,page:pageData['page'],size:pageData['size']},success,failure);
        },
        addComment: function (userId,blogId,comment,success,failure) {
            comment.userId = userId;
            comment.blogId = blogId;
            res.save(comment,success,failure);
        },
        getOneComment: function (userId,blogId,success,failure) {
            res.get({userId:userId,blogId:blogId},success,failure);
        },
        updateComment: function (userId,blogId,comment,success,failure) {
            comment.userId = userId;
            comment.blogId = blogId;
            res.update(comment,success,failure);
        },
        deleteComment: function (userId,blogId,success,failure) {
            res.delete({userId:userId,blogId:blogId},success,failure);
        }
    };
}).factory('userLoginStatus', function () {
    var service = {};
    service.getUserId = function() {
        return localStorage.getItem("userId");
    };
    service.isLoggedIn = function() {
        return !(localStorage.getItem("userId") == '' || localStorage.getItem("userId") == null);
    };
    service.login = function(id) {
        localStorage.setItem("userId",id);
    };
    service.logout = function () {
        localStorage.removeItem("userId");
    };

    return service;

}).factory('sessionService',function($state,$http,basePath) {
    var service = {};
    service.login = function (username,password,success,failure) {
        $http.post(basePath + 'login?username=' + username + '&password=' + password, {
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            }
        }).then(success,failure);
    };

    service.logout = function (success,failure) {
        $http.post(basePath + 'logout', {
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            }
        }).then(success,failure);
    };

    return service;
}).factory('serverLoginStatusGetter',function($resource,basePath) {
    var res = $resource(basePath + "user/current",{});
    var service = {};
    service.getLoggedInUser = function(success,failure) {
        res.get({},success,failure);
    };
    return service;
}).controller( 'appCtrl', function($scope,$state, $location, userLoginStatus,sessionService,serverLoginStatusGetter ) {

    $scope.init = function() {
        serverLoginStatusGetter.getLoggedInUser(function(data) {
            if(data.userId != undefined) {
                $scope.isLoggedIn = true;
                $scope.userId = data.userId;
                userLoginStatus.login(data.userId);
            } else {
                $scope.isLoggedIn = false;
                $scope.userId = '';
                userLoginStatus.logout();
            }
        },function() {
            alert("retrieve user login status failure");
        });
    };

    $scope.$on('$stateChangeSuccess', function(event, toState, toParams, fromState, fromParams){
        if ( angular.isDefined( toState.data.pageTitle ) ) {
            $scope.pageTitle = toState.data.pageTitle + ' | My Blog' ;
        }
    });

    $scope.isLoggedIn = userLoginStatus.isLoggedIn();
    $scope.toggle = function() {
        $scope.isLoggedIn = !$scope.isLoggedIn;
        if(userLoginStatus.isLoggedIn() == false) {
            userLoginStatus.login(1);
        } else {
            userLoginStatus.logout();
        }
    };

    $scope.logout = function() {
        sessionService.logout(function() {
            $scope.isLoggedIn = false;
            $scope.userId = '';
            userLoginStatus.logout();

            alert("logout success");

            $state.go("signin");
        }, function () {
            alert("logout fail");
        });
    };
});