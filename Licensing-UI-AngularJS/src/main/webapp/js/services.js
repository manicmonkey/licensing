angular.module('licensingServices', ['ngResource'])
    .factory('Configuration', ['$resource', '$http',
        function($resource, $http) {
            $http.defaults.headers.common['Authorization'] = 'Basic YWRtaW46cGFzc3dvcmQ=';
            return $resource('http://localhost:8080/rest/configurations', {}, {
                query: {
                    method: 'GET',
                    isArray: true
                }
            });
        }])
    .factory('Customer', ['$resource', '$http', function($resource, $http) {
        $http.defaults.headers.common['Authorization'] = 'Basic YWRtaW46cGFzc3dvcmQ=';
        return $resource('http://localhost:8080/rest/customers', {}, {
            query: {
                method: 'GET',
                isArray: true
            }
        });
    }])
    .factory('Product', ['$resource', '$http', function($resource, $http) {
        $http.defaults.headers.common['Authorization'] = 'Basic YWRtaW46cGFzc3dvcmQ=';
        return $resource('http://localhost:8080/rest/products', {}, {
            query: {
                method: 'GET'
            }
        });
    }]);