(function() {
    'use strict';
    angular
        .module('aiProjektApp')
        .factory('UserBorrows', UserBorrows);

    UserBorrows.$inject = ['$resource'];

    function UserBorrows ($resource) {
        var resourceUrl =  'api/borrows/user/:userId';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                isArray: true,
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
