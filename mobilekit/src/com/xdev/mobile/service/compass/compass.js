com_xdev_mobile_service_compass_CompassService = function() {
	var connector = this;
	window.compass_get_success = function(caller, heading) {
		connector.compass_get_success(caller, heading);
	};
	window.compass_get_error = function(caller, error) {
		connector.compass_get_error(caller, error);
	};
	window.compass_watch_success = function(caller, heading, watchID) {
		connector.compass_watch_success(caller, heading, watchID);
	};
	window.compass_watch_error = function(caller, error, watchID) {
		connector.compass_watch_error(caller, error, watchID);
	};
};

function compass_watch(caller, options) {
	var success = function(heading) {
		window.compass_watch_success(caller, heading, watchID);
	};

	var error = function(error) {
		window.compass_watch_error(caller, error, watchID);
	};

	var watchID = navigator.compass.watchHeading(success, error, options);
}

function compass_clear_watch(watchID) {
	navigator.compass.clearWatch(watchID);
	watchID = null;
}

function compass_get(caller) {

	var success = function(heading) {
		window.compass_get_success(caller, heading);
	};

	var error = function(error) {
		window.compass_get_error(caller, error);
	};

	navigator.compass.getCurrentHeading(success, error);
}