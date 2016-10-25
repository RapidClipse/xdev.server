com_xdev_mobile_service_barcodescanner_BarcodescannerService = function() {
	var connector = this;
	window.barcodescanner_scan_success = function(caller, result) {
		connector.barcodescanner_scan_success(caller, result);
	};
	window.barcodescanner_scan_error = function(caller, error) {
		connector.barcodescanner_scan_error(caller, error);
	};
};

function barcodescanner_scan(caller, options) {

	var success = function(result) {
		window.barcodescanner_scan_success(caller, result);
	};

	var error = function(error) {
		window.barcodescanner_scan_error(caller, error);
	};

	cordova.plugins.barcodeScanner.scan(success, error, options);
}