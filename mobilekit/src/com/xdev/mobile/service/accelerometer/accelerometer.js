com_xdev_mobile_service_accelerometer_AccelerometerService = function() {
	var connector = this;
	window.accelerometer_get_success = function(caller, acceleration) {
		connector.accelerometer_get_success(caller, acceleration);
	};
	window.accelerometer_get_error = function(caller, error) {
		connector.accelerometer_get_error(caller, error);
	};
	window.accelerometer_watch_success = function(caller, acceleration, watchID) {
		connector.accelerometer_watch_success(caller, acceleration, watchID);
	};
	window.accelerometer_watch_error = function(caller, error, watchID) {
		connector.accelerometer_watch_error(caller, error, watchID);
	};
};

function accelerometer_watch(caller, options) {
	var success = function(acceleration) {
		window.accelerometer_watch_success(caller, acceleration, watchID);
	};

	var error = function(error) {
		window.accelerometer_watch_error(caller, error, watchID);
	};

	var watchID = navigator.accelerometer.watchAcceleration(success, error, options);
}

function accelerometer_clear_watch(watchID) {
	navigator.accelerometer.clearWatch(watchID);
	watchID = null;
}

function accelerometer_get(caller) {

	var success = function(acceleration) {
		window.accelerometer_get_success(caller, acceleration);
	};

	var error = function(error) {
		window.accelerometer_get_error(caller, error);
	};

	navigator.accelerometer.getCurrentAcceleration(success, error);
}