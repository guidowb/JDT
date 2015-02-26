mobyControllers.controller('javaController', ['$scope', '$http',
	function ($scope, $http) {
		$scope.open = function(filename) {
			url = '/api/java?file=' + filename;
			$http.get(url).success(function(data) {
				$scope.block = data;
			});
		}
	}
]);
