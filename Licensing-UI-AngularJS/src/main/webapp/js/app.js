'use strict';
angular.module('licensingApp', ['ngRoute', 'licensingServices'])
    .constant('_', window._)
    .constant('moment', window.moment)
    .constant('credentials', 'YWRtaW46cGFzc3dvcmQ=')
    .constant('defaultDateFormat', 'Do MMM YYYY')
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
    .filter('formatDate', ['moment', 'defaultDateFormat', function (moment, defaultDateFormat) {
        return function (input, format) {
            if (!format)
                format = defaultDateFormat;
            return moment(new Date(input)).format(format);
        };
    }])
    .controller('ConfigurationController', ['$scope', '$q', '_', 'moment', 'Configuration', 'Customer', 'Product', function ($scope, $q, _, moment, Configuration, Customer, Product) {
        Customer
            .query().$promise
            .then(function(customers) {
                $scope.customers = _.sortBy(customers, 'name');
            });
        $scope.customerSelected = function(customer) {
            Configuration
                .query({customer: customer.name}).$promise
                .then(function(configurations) {
                    var productIds = _.map(configurations, 'productId');
                    var uniqueProductIds = _.uniq(productIds);
                    var productPromises = _.map(uniqueProductIds, function (id) { return Product.query({id: id}).$promise; });
                    var productsPromise = $q.all(productPromises);
                    productsPromise.then(function (products) {
                        _.forEach(configurations, function(configuration) {
                            configuration.product = _.find(products, {id: configuration.productId}).name;
                        });
                        $scope.configurations = _.sortBy(configurations, 'created').reverse();
                    });
                });
            $scope.configurationOptions = [];
            $scope.activations = [];
            $scope.activationInfos = [];
        };
        $scope.configurationSelected = function(configuration) {
            $scope.selectedConfigurationId = configuration.id;
            $scope.activations = _.sortBy(configuration.activations, 'created').reverse();
            $scope.configurationOptions = _.sortBy(_.map(_.pairs(configuration.options),
                function(pair) {
                    return { name: pair[0], value: pair[1] }
                }), 'name');
        };
        $scope.activationSelected = function(activation) {
            $scope.selectedActivationId = activation.id;
            $scope.activationInfos = _.sortBy(_.map(_.pairs(activation.extraInfo),
                function(pair) {
                    return { name: pair[0], value: pair[1] }
                }), 'name');
        }
    }])
    .controller('ActivationController', ['$scope', function ($scope) {}])
    .controller('ProductOptionController', ['$scope', function ($scope) {}]);