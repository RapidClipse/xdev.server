com_xdev_mobile_service_geolocation_GeolocationService = function() {
	var connector = this;
	window.geolocation_get_success = function(caller, position) {
		connector.geolocation_get_success(caller, position);
	};
	window.geolocation_get_error = function(caller, error) {
		connector.geolocation_get_error(caller, error);
	};

	window.geolocation_get_future_success = function(position) {
		connector.geolocation_get_future_success(position);
	};
	window.geolocation_get_future_error = function(error) {
		connector.geolocation_get_future_error(error);
	};
};

function geolocation_get(caller) {

	var success = function(position) {
		window.geolocation_get_success(caller, position);
	};

	var error = function(error) {
		window.geolocation_get_error(caller, error);
	};

	navigator.geolocation.getCurrentPosition(success, error);
}

function geolocation_get_future() {
	var success = function(position) {
		console.log(position);
		window.geolocation_get_future_success(position);
	};

	var error = function(error) {
		window.geolocation_get_future_error(error);
	};

	navigator.geolocation.getCurrentPosition(success, error);
}