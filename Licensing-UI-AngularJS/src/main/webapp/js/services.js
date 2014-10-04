angular.module('licensingServices', ['ngResource'])
    .factory('Configuration', ['$resource', '$http', 'credentials',
        function($resource, $http, credentials) {
            $http.defaults.headers.common['Authorization'] = 'Basic ' + credentials;
            return $resource('http://localhost:8080/rest/configurations', {}, {
                query: {
                    method: 'GET',
                    isArray: true
                }
            });
        }])
    .factory('Customer', ['$resource', '$http', 'credentials',
        function($resource, $http, credentials) {
            $http.defaults.headers.common['Authorization'] = 'Basic ' + credentials;
            return $resource('http://localhost:8080/rest/customers', {}, {
                query: {
                    method: 'GET',
                    isArray: true
                }
            });
        }])
    .factory('Product', ['$resource', '$http', 'credentials',
        function($resource, $http, credentials) {
            $http.defaults.headers.common['Authorization'] = 'Basic ' + credentials;
            return $resource('http://localhost:8080/rest/products', {}, {
                query: {
                    method: 'GET'
                }
            });
        }]);