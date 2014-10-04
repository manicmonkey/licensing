'use strict';
angular.module('licensingApp', ['ngRoute', 'licensingServices'])
    .constant('_', window._)
    .constant('moment', window.moment)
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
    .controller('ConfigurationController', ['$scope', '$q', '_', 'moment', 'Configuration', 'Customer', 'Product', function ($scope, $q, _, moment, Configuration, Customer, Product) {
        Customer.query()
            .$promise
            .then(function(customers) {
                $scope.customers = _.sortBy(customers, 'name');
            });
        $scope.updateConfiguration = function(customer) {
            Configuration
                .query({customer: customer})
                .$promise
                .then(function(configurations) {
                    var productIds = _.map(configurations, 'productId');
                    var uniqueProductIds = _.uniq(productIds);
                    var productPromises = _.map(uniqueProductIds, function (id) { return Product.query({id: id}).$promise; });
                    var productsPromise = $q.all(productPromises);
                    productsPromise.then(function (products) {
                        _.forEach(configurations, function(configuration) {
                            configuration.product = _.find(products, {id: configuration.productId}).name;
                            configuration.created = moment(new Date(configuration.created)).format('Do MMM YYYY');
                        });
                        $scope.configurations = configurations;
                    });
                });
        };
    }])
    .controller('ActivationController', ['$scope', function ($scope) {}])
    .controller('ProductOptionController', ['$scope', function ($scope) {}]);