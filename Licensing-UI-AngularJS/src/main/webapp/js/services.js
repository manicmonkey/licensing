angular.module('licensingServices', ['ngResource'])
    .factory('Configuration', ['$resource', '$http', 'credentials',
        function ($resource, $http, credentials) {
            $http.defaults.headers.common['Authorization'] = 'Basic ' + credentials;
            return $resource('http://localhost:8080/rest/configurations', {}, {
                getAll: {
                    method: 'GET',
                    isArray: true
                }
            });
        }])
    .factory('Customer', ['$resource', '$http', 'credentials',
        function ($resource, $http, credentials) {
            $http.defaults.headers.common['Authorization'] = 'Basic ' + credentials;
            return $resource('http://localhost:8080/rest/customers', {}, {
                getAll: {
                    method: 'GET',
                    isArray: true
                }
            });
        }])
    .factory('Product', ['$resource', '$http', 'credentials',
        function ($resource, $http, credentials) {
            $http.defaults.headers.common['Authorization'] = 'Basic ' + credentials;
            return $resource('http://localhost:8080/rest/products', {}, {
                getOne: {
                    method: 'GET'
                },
                getAll: {
                    method: 'GET',
                    isArray: true
                }
            });
        }]);