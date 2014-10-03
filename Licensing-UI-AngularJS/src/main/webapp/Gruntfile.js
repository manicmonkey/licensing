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
        bower: {
            install: {
                options: {
                    targetDir: './lib',
                    layout: 'byComponent',
                    cleanup: true
                }
            }
        }
    });

    // Load plugins

    grunt.loadNpmTasks('grunt-contrib-uglify');

    // Kicks off bower to automatically download dependencies and copy them to the output dir (lib)
    grunt.loadNpmTasks('grunt-bower-task');


    //Register tasks

    grunt.registerTask('default', ['uglify']);

    grunt.registerTask('default', ['bower']);
};
