angular.module('licensingServices', ['ngResource'])
    .factory('Configuration', ['$resource',
        function($resource) {
            return $resource('configuration', {}, {
                query: {method: 'GET', isArray: true}
            })
        }]);