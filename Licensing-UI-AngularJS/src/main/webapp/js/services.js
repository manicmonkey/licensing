angular.module('licensingServices', ['ngResource'])
    .factory('Audit', ['$resource', '$http', 'credentials',
        function ($resource, $http, credentials) {
            $http.defaults.headers.common['Authorization'] = 'Basic ' + credentials;
            return $resource('http://localhost:8080/rest/audits/:submodel', {}, {
                getAudits: {
                    method: 'POST',
                    params: {
                        submodel: 'messages'
                    },
                    isArray: true
                },
                getUsernames: {
                    method: 'GET',
                    params: {
                        submodel: 'usernames'
                    },
                    isArray: true
                },
                getAuditCodes: {
                    method: 'GET',
                    params: {
                        submodel: 'auditcodes'
                    },
                    isArray: true
                }
            });
        }])
    .factory('Authentication', ['$resource',
        function($resource) {
            return $resource('http://localhost:8080/rest/authentication', {}, {
                isLoggedIn: {
                    method: 'GET'
                },
                login: {
                    method: 'POST'
                },
                logout: {
                    method: 'DELETE'
                }
            });
        }
    ])
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