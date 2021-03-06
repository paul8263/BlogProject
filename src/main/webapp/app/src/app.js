angular.module('app',['ui.router','ngAnimate','ngCookies','ngResource', 'ui.bootstrap','app.home','app.signIn','app.register','app.userProfile','app.changeUserProfile','app.createModifyBlog','app.viewBlog']).config(function($stateProvider,$urlRouterProvider,$httpProvider) {
    $urlRouterProvider.otherwise('/home');

    //Set default CSRF header and cookie name
    $httpProvider.defaults.xsrfHeaderName = 'X-CSRF-TOKEN';
    $httpProvider.defaults.xsrfCookieName = 'CSRF-TOKEN';

    //$httpProvider.defaults.headers.common.Cookie = document.cookie;

    //$httpProvider.defaults.headers.common['Access-Control-Allow-Origin'] = 'localhost:8080/';
    //$httpProvider.defaults.headers.common['Access-Control-Allow-Credentials'] = 'true';

    $httpProvider.interceptors.push('authInterceptor');

}).value('basePath','/myblog/').run(function($rootScope,$state) {
    $rootScope.state = $state;
}).factory('authInterceptor',function($cookies) {
    function b(a){return a?(a^Math.random()*16>>a/4).toString(16):([1e16]+1e16).replace(/[01]/g,b)}

    var token = b();

    return {
        'request': function(config) {

            $cookies['CSRF-TOKEN'] = token;

            //config.headers['Cookie'] = 'CSRF-TOKEN=' + $cookies['CSRF-TOKEN'];

            return config;
        }
    };
}).factory('userResource',function($resource,basePath) {

    var res = $resource(basePath + 'user/:userId',{userId:'@userId'},{
        update: {
            method:'PUT'
        },
        add: {
            method: 'POST',
            headers: {
                'Content-Type': undefined,
                enctype: 'multipart/form-data'
            },
            transformRequest: function(data,headers) {
                var fd = new FormData();
                angular.forEach(data,function(value,key) {
                    fd.append(key,value);
                });
                return fd;
            }
        }
    });
    var resUsername = $resource(basePath + 'user/username/:username',{username:'@username'});
    var userIcon = $resource(basePath + 'user/:userId/icon',{userId:'@userId'},{
        add: {
            method: 'POST',
            headers: {
                'Content-Type': undefined,
                enctype: 'multipart/form-data'
            },
            transformRequest: function(data,headers) {
                var fd = new FormData();
                angular.forEach(data,function(value,key) {
                    fd.append(key,value);
                });
                return fd;
            }
        }
    });
    return {
        addUser: function(user,success,failure) {
            res.add(user,success,failure);
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
        },
        updateUserIcon: function(userId,icon,success,failure) {
            userIcon.add({userId:userId,icon:icon},success,failure);
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
}).directive('fileread', function() {
    return {
        restrict: 'A',
        link: function(scope,element,attr) {
            element.bind('change',function(changeEvent) {
                scope.$apply(function() {
                    scope[attr.fileread] = changeEvent.target.files[0];
                });
            });
        }
    };
}).controller( 'appCtrl', function($scope,$state,$location,userLoginStatus,sessionService,serverLoginStatusGetter) {

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

        //var res = $resource("/test",{});
        //res.save({},function(){},function(){alert("Toggle!")});

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