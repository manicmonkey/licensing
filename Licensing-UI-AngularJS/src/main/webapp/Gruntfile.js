module.exports = function (grunt) {

    // Project configuration.
    grunt.initConfig({
        pkg: grunt.file.readJSON('package.json'),
        uglify: {
            options: {
                banner: '/*! <%= pkg.name %> <%= grunt.template.today("yyyy-mm-dd") %> */\n'
            },
            build: {
                src: 'src/<%= pkg.name %>.js',
                dest: 'build/<%= pkg.name %>.min.js'
            }
        },
        vulcanize: {
            default: {
                options: {},
                files: {
                    'build.html': 'index.html'
                }
            }
        },
        bower: {
            target: {
                rjsConfig: 'app/config.js'
            }
        }/*,
         bower_concat: {
         dest: 'teh_codez.js',
         exclude: ['core-component-page']
         }*/
    });

    // Load plugins

    grunt.loadNpmTasks('grunt-contrib-uglify');

    // Load grunt-bower-requirejs
    grunt.loadNpmTasks('grunt-bower-requirejs');

    // Kicks off bower to automatically download dependencies and copy them to the output dir (lib)
    grunt.loadNpmTasks('grunt-bower-task');

    // Inlines all referenced web components: https://github.com/Polymer/vulcanize#example
//    grunt.loadNpmTasks('grunt-vulcanize');

//  grunt.loadNpmTasks('grunt-bower-concat');

    //Register tasks

    grunt.registerTask('default', ['uglify']);

    grunt.registerTask('default', ['bower']);

//  grunt.registerTask('default', ['vulcanize']);


//  grunt.registerTask('default', ['bower_concat']);

};
