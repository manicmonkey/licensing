'use strict';
angular.module('licensingApp', ['ngRoute', 'licensingServices'])
    .constant('_', window._)
    .constant('moment', window.moment)
    .constant('credentials', 'YWRtaW46cGFzc3dvcmQ=')
    .constant('defaultDateFormat', 'Do MMM YYYY HH:mm')
    .constant('formDateFormat', 'YYYY-MM-DD')
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
            .when('/administration/auditing', {
                controller: 'AuditController',
                templateUrl: 'html/auditing.html'
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
    .filter('cleverOrdering', ['_', function(_) {
        return function (input) {
            //sort as int if they're all ints
            if (_.all(input, function(v) { return !isNaN(parseInt(v)); })) {
                return _.sortBy(input, function(v) { return parseInt(v); });
            } else {
                return input.sort();
            }
        };
    }])
    .controller('ConfigurationController', ['$scope', '$q', '_', 'moment', 'Configuration', 'Customer', 'Product', function ($scope, $q, _, moment, Configuration, Customer, Product) {
        Customer
            .getAll({enabled: true}).$promise
            .then(function(customers) {
                $scope.customers = _.sortBy(customers, 'name');
            });
        $scope.customerSelected = function(customer) {
            Configuration
                .getAll({customer: customer.name}).$promise
                .then(function(configurations) {
                    var productIds = _.map(configurations, 'productId');
                    var uniqueProductIds = _.uniq(productIds);
                    var productPromises = _.map(uniqueProductIds, function (id) { return Product.getOne({id: id}).$promise; });
                    var productsPromise = $q.all(productPromises);
                    productsPromise.then(function (products) {
                        _.forEach(configurations, function(configuration) {
                            configuration.product = _.find(products, {id: configuration.productId}).name;
                        });
                        $scope.configurations = _.sortBy(configurations, 'created').reverse();
                    });
                });
            $scope.selectedConfiguration = undefined;
            $scope.selectedActivation = undefined;
        };
        $scope.configurationSelected = function(configuration) {
            $scope.selectedConfiguration = configuration;
            $scope.selectedActivation = undefined;
        };
        $scope.activationSelected = function(activation) {
            $scope.selectedActivation = activation;
        };
    }])
    .controller('ConfigurationCreation', ['$scope', '_', 'Configuration', 'Product', function ($scope, _, Configuration, Product) {
        //at the moment the product options which the user fills in operates directly on the product.options model - maybe
        //it would be better to create a configuration object here and operate on that (including maxActivations)
        Product.getAll().$promise
            .then(function(products) {
                _.forEach(products, function(product) {
                    _.forEach(product.options, function(option) {
                        if (option.default != undefined)
                            option.value = option.default;
                    });
                });
                $scope.products = products;
            });

        $scope.customerSelected = function(customer) {
            $scope.customer = customer;
        };
        $scope.productSelected = function(product) {
            $scope.product = product;
        };
        $scope.save = function() {
            //todo probably can replace this complicated map/reduce with pluck
            var configurationOptions = _.map($scope.product.options, function(option) {
                var o = {};
                o[option.name] = option.value;
                return o;
            });
            configurationOptions = _.reduce(configurationOptions, function(left, right) {
                for (var option in right) {
                    left[option] = right[option];
                }
                return left;
            }, {});
            var configuration = {
                user: "jbaxter", //todo this should be set automatically by server...
                productId: $scope.product.id,
                customerId: $scope.customer.id,
                created: new Date().getTime(),
                enabled: true,
                activations: [],
                maxActivations: $scope.product.maxActivations,
                options: configurationOptions
            };
            Configuration.save(configuration);
        };
    }])
    .controller('AdministrationController', [function (){}])
    .controller('AuditController', ['$scope', 'moment', 'formDateFormat', 'Audit', function ($scope, moment, formDateFormat, Audit){
        $scope.search = {
            fromDate: moment(new Date()).subtract(1, 'week').format(formDateFormat),
            toDate: moment(new Date()).format(formDateFormat),
            message: ''
        };
        Audit.getAuditCodes().$promise
            .then(function(auditCodes) {
                $scope.search.auditCodes = _.map(auditCodes, function(auditCode) {
                    return {
                        name: auditCode.value, //todo should get i18n version
                        selected: true
                    };
                });
            });
        Audit.getUsernames().$promise
            .then(function(usernames) {
                $scope.search.users = _.map(usernames.sort(), function(username) {
                    return {
                        name: username,
                        selected: true
                    };
                });
            });
        $scope.selectUser = function(user) {
            user.selected = !user.selected; //todo this could go on a user object
        };
        $scope.selectAuditCode = function(auditCode) {
            auditCode.selected = !auditCode.selected; //todo this could go on a user object
        };
        $scope.filter = function(search) {
            $scope.audits = Audit.getAudits(
                {
                    "fromDate": moment(search.fromDate).toDate(),
                    "toDate": moment(search.toDate).add(1, 'day').toDate(), //add a day for query so we include the whole day
                    "users": _.pluck(_.filter(search.users, 'selected'), 'name'),
                    "auditCodes": _.pluck(_.filter(search.auditCodes, 'selected'), 'name'),
                    "text": search.message
                });
        }
    }])
    .controller('ActivationController', ['$scope', function ($scope) {}])
    .controller('ProductOptionController', ['$scope', function ($scope) {}])
    .controller('MenuController', ['$rootScope', '$scope', '$location', function ($rootScope, $scope, $location) {
        $rootScope.$on('$routeChangeSuccess', function(event, current, previous) {
            $scope.current = $location.path().substring(1);
        });
        $scope.current = $location.path().substring(1);
    }]);