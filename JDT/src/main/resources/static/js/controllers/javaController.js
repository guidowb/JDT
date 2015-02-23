mobyControllers.controller('javaController', ['$scope', '$http',
	function ($scope, $http) {
		$http.get('/api/java').success(function(data) {
			$scope.unit = data;
			$scope.statements = $scope.unit['statements'];
		});
	}
]);
