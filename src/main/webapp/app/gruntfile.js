module.exports = function(grunt) {
    var pkg = grunt.file.readJSON("package.json");

    var userConfig = {
        banner: '/* Created by Paul. Version: ' + pkg.version + ' . Compiled on '+ new Date() + ' */\n',
        distDir: 'dist',
        jsDir: 'dist/js',
        cssDir: 'dist/css',
        testFiles: ['src/*.spec.js','src/*/*.spec.js'],

        imgFiles: ['img/**'],

        htmlFiles: [
            '*.html','*/*.html','!index.html'
        ],

        vendorFiles: {
            js: [
                'bower_components/jquery/dist/jquery.min.js',
                'bower_components/angular/angular.min.js',
                'bower_components/angular-resource/angular-resource.min.js',
                'bower_components/angular-animate/angular-animate.min.js',
                'bower_components/angular-cookies/angular-cookies.min.js',
                'bower_components/angular-ui-router/release/angular-ui-router.min.js',
                'bower_components/bootstrap/dist/js/bootstrap.min.js',
                'bower_components/angular-bootstrap/ui-bootstrap.min.js',
                'bower_components/angular-bootstrap/ui-bootstrap-tpls.min.js',
                'bower_components/textAngular/dist/textAngular.min.js',
                'bower_components/textAngular/dist/textAngular-rangy.min.js',
                'bower_components/textAngular/dist/textAngular-sanitize.min.js'
            ],
            css: [
                'bower_components/bootstrap/dist/css/bootstrap.min.css',
                'bower_components/rangy/rangy-core.min.js',
                'bower_components/font-awesome/css/font-awesome.min.css',
                'bower_components/textAngular/src/textAngular.css'
            ]
        }
    };




    grunt.initConfig({
        clean: [
            userConfig.distDir
        ],
        concat: {
            concatJs: {
                options: {
                    separator: ';'
                },
                src: ['src/*.js','src/*/*.js','!src/*.spec.js','!src/*/*.spec.js'],
                dest: userConfig.distDir + '/js/output.min.js'
            }
        },
        copy: {
            copyVendorJs: {
                files: [{
                    expand: true,
                    flatten: true,
                    cwd: '.',
                    src: userConfig.vendorFiles.js,
                    dest: userConfig.jsDir
                }]
            },
            copyVendorCss: {
                files: [{
                    expand: true,
                    flatten: true,
                    cwd: '.',
                    src: userConfig.vendorFiles.css,
                    dest: userConfig.cssDir
                }]
            },
            copyBootstrapFonts: {
                files: [{
                    expand: true,
                    cwd: 'bower_components/font-awesome/fonts',
                    src: "*.*",
                    dest: userConfig.distDir + "/fonts"
                }]
            },
            copyFontAwesomeFonts: {
                files: [{
                    expand: true,
                    cwd: 'bower_components/bootstrap/fonts',
                    src: "*.*",
                    dest: userConfig.distDir + "/fonts"
                }]
            },
            copyOther: {
                files:[{
                    expand: true,
                    cwd: 'src',
                    src: [userConfig.htmlFiles,userConfig.imgFiles],
                    dest: userConfig.distDir
                }]
            }

        },
        index: {
            dist: {
                src: [userConfig.vendorFiles.js,'dist/js/output.min.js','dist/css/*.css']
            }
        },
        jshint: {
            options: {
                curly: true,
                eqeqeq: true,
                eqnull: true,
                browser: true,
                globals: {
                    jQuery: true
                }
            },
            js: [
                'src/*/*.js',
                'src/*.js'
            ],
            gruntfile: [
                'gruntfile.js'
            ]
        },
        less: {
            development: {
                options: {
                    paths: ["src/less"],
                    banner: userConfig.banner
                },
                files: {
                    "dist/css/output.min.css": "src/less/app.less"
                }
            },
            production: {
                options: {
                    paths: ["src/less"],
                    banner: userConfig.banner,
                    compress: true,
                    cleancss: true
                },
                files: {
                    "dist/css/output.min.css": "src/less/app.less"
                }
            }
        },
        karma: {
            unit: {
                configFile: 'karma.conf.js'
            }

        },
        uglify: {
            compile: {
                options: {
                    mangle: false,
                    banner: userConfig.banner
                },

                files: {
                    'dist/js/output.min.js': ['dist/js/output.min.js']
                }

            }

        },
        watch: {
            options: {
                spawn: false,
                livereload: true
            },
            jshint: {
                files: ['src/*.js','src/*/*.js'],
                tasks: ['jshint']

            },
            test: {
                files: userConfig.testFiles,
                tasks: ['karma']
            }

        }
    });

    grunt.loadNpmTasks("grunt-contrib-copy");
    grunt.loadNpmTasks("grunt-contrib-uglify");
    grunt.loadNpmTasks("grunt-contrib-jshint");
    grunt.loadNpmTasks("grunt-contrib-watch");
    grunt.loadNpmTasks("grunt-contrib-clean");
    grunt.loadNpmTasks("grunt-contrib-less");
    grunt.loadNpmTasks("grunt-contrib-concat");
    grunt.loadNpmTasks("grunt-karma");


    grunt.registerTask('compile',['copy','concat','uglify','less:production','index']);
    grunt.registerTask('rebuild',['clean','compile']);
    grunt.registerTask('default',['watch']);


    function filterForJs(files) {
        return files.filter(function(file) {
            return file.match(/\.js$/);
        });
    }
    function filterForCss(files) {
        return files.filter(function(file) {
            return file.match(/\.css$/);
        });
    }

    grunt.registerMultiTask("index","Compile index.html",function() {
        var files = this.filesSrc;
        var jsFiles = filterForJs(files).map(function(file) {
            return 'js' + file.substring(file.lastIndexOf('/'));
        });
        var cssFiles = filterForCss(files).map(function(file) {
            return file.replace(userConfig.distDir + "/",'');
        });
        /*jsFiles.forEach(function(file){
            grunt.log.write(file + '\n');
        });
        cssFiles.forEach(function(file){
            grunt.log.write(file + '\n');
        });*/

        grunt.file.copy('src/index.html', userConfig.distDir + '/index.html', {
            process: function ( contents, path ) {
                return grunt.template.process( contents, {
                    data: {
                        scripts: jsFiles,
                        styles: cssFiles
                    }
                });
            }
        });
    });

};