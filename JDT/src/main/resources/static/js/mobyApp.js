var mobyApp = angular.module('mobyApp',
	[
	 	'ui.router',
	 	'ui.bootstrap',
	 	'mobyControllers'
	]
);

mobyApp.config(
	['$stateProvider', '$urlRouterProvider',
	 	function($stateProvider, $urlRouterProvider) {
	    	$urlRouterProvider.otherwise('/');
	    
	    	$stateProvider
	        	.state('java', {
	        		url:'/',
	        		templateUrl: 'partials/java.html',
	        		controller: 'javaController'
	        	})
		}
	]
);

mobyApp.run(
	['$http',
	 	function($http) {
			$http.defaults.xsrfHeaderName = 'X-CSRF-TOKEN';
		}
	]
);

mobyControllers = angular.module('mobyControllers', []);
