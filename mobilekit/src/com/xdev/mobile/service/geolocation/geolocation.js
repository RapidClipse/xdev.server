com_xdev_mobile_service_geolocation_GeolocationService = function() {
	var connector = this;
	window.geolocation_get_success = function(caller, position) {
		connector.geolocation_get_success(caller, position);
	};
	window.geolocation_get_error = function(caller, error) {
		connector.geolocation_get_error(caller, error);
	};
	window.geolocation_watch_success = function(caller, position, watchID) {
		connector.geolocation_watch_success(caller, position, watchID);
	};
	window.geolocation_watch_error = function(caller, error, watchID) {
		connector.geolocation_watch_error(caller, error, watchID);
	};
};

function geolocation_watch(caller, options) {
	var success = function(position) {
		window.geolocation_watch_success(caller, makeSerializablePosition(position), watchID);
	};

	var error = function(error) {
		window.geolocation_watch_error(caller, makeSerializableError(error), watchID);
	};

	var watchID = navigator.geolocation.watchPosition(success, error, options);
}

function geolocation_clear_watch(watchID) {
	navigator.geolocation.clearWatch(watchID);
	watchID = null;
}

function geolocation_get(caller,options) {

	var success = function(position) {
		window.geolocation_get_success(caller, makeSerializablePosition(position));
	};

	var error = function(error) {
		window.geolocation_get_error(caller, makeSerializableError(error));
	};

	navigator.geolocation.getCurrentPosition(success, error, options);
}

/**
 * Fix to serialize position object, see
 * http://stackoverflow.com/questions/32882035/cordova-geolocation-plugin-returning-empty-position-object-on-android
 */
function makeSerializablePosition(position) {
	var positionObject = {};

	if ('coords' in position) {
		positionObject.coords = {};

		if ('latitude' in position.coords) {
			positionObject.coords.latitude = position.coords.latitude;
		}
		if ('longitude' in position.coords) {
			positionObject.coords.longitude = position.coords.longitude;
		}
		if ('accuracy' in position.coords) {
			positionObject.coords.accuracy = position.coords.accuracy;
		}
		if ('altitude' in position.coords) {
			positionObject.coords.altitude = position.coords.altitude;
		}
		if ('altitudeAccuracy' in position.coords) {
			positionObject.coords.altitudeAccuracy = position.coords.altitudeAccuracy;
		}
		if ('heading' in position.coords) {
			positionObject.coords.heading = position.coords.heading;
		}
		if ('speed' in position.coords) {
			positionObject.coords.speed = position.coords.speed;
		}
	}

	if ('timestamp' in position) {
		positionObject.timestamp = position.timestamp;
	}
	
	return positionObject;
}

function makeSerializableError(error) {
	var errorObject = {};

	if ('code' in error) {
		errorObject.code = error.code;
	}
	
	if ('message' in error) {
		errorObject.message = error.message;
	}
	
	return errorObject;
}