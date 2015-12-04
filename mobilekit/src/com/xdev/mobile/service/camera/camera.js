com_xdev_mobile_service_camera_CameraService = function() {
	var connector = this;
	window.camera_getPicture_success = function(caller, contacts) {
		connector.camera_getPicture_success(caller, contacts);
	};
	window.camera_getPicture_error = function(caller, error) {
		connector.camera_getPicture_error(caller, error);
	};
};

function camera_getPicture(caller, options) {

	var success = function(imageData) {
		window.camera_getPicture_success(caller, imageData);
	};

	var error = function(error) {
		window.camera_getPicture_error(caller, error);
	};

	navigator.camera.getPicture(success, error, options);
}