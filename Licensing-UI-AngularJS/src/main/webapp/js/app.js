angular.module('licensingApp', ['ngRoute', 'licensingServices'])
    .config(function ($routeProvider) {
        $routeProvider
            .when('/', {
                templateUrl: 'html/welcome.html'
            })
            .when('/activate', {
                controller: 'ActivationController',
                templateUrl: 'html/activation.html'
            })
            .when('/management', {
                controller: 'ConfigurationController',
                templateUrl: 'html/licence_management.html'
            })
            .when('/administration', {
                controller: 'AdministrationController',
                templateUrl: 'html/administration.html'
            })
            .when('/product_option', {
                controller: 'ProductOptionController',
                templateUrl: 'html/product_option.html'
            })
            .otherwise({
                redirectTo: '/'
            })
    })
    .controller('ConfigurationController', ['$scope', 'Configuration', function ($scope, Configuration) {
        $scope.configurations = Configuration.query();
//            [
//            {
//                product: "Product 1",
//                created: "14-02-2014",
//                user: "jbaxter",
//                activations: "1 / 2",
//                serial: "F3SC7-ASS54-L94FF-CXF1Q-POOUP"
//            },
//            {
//                product: "Product 2",
//                created: "13-02-2014",
//                user: "mattf",
//                activations: "1 / 1",
//                serial: "ASX3S-P234D-PLM20-PK02B-WX923"
//            }
//        ]
    }])
    .controller('ActivationController', ['$scope', function ($scope) {}])
    .controller('ProductOptionController', ['$scope', function ($scope) {}]);