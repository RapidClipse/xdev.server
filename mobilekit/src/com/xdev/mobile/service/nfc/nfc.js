com_xdev_mobile_service_nfc_NfcService = function() {
	var connector = this;

	window.nfc_startTagDiscoveredListener_callback = function(caller, callback) {
		connector.nfc_startTagDiscoveredListener_callback(caller, callback)
	};
	window.nfc_startTagDiscoveredListener_success = function(caller,
			successCallback) {
		connector.nfc_startTagDiscoveredListener_success(caller,
				successCallback);
	};

	window.nfc_startTagDiscoveredListener_error = function(caller, error) {
		connector.nfc_startTagDiscoveredListener_error(caller, error);
	};

	window.nfc_stopTagDiscoveredListener_success = function(caller,
			successCallback) {
		connector
				.nfc_stopTagDiscoveredListener_success(caller, successCallback);
	};
	window.nfc_stopTagDiscoveredListener_error = function(caller, error) {
		connector.nfc_stopTagDiscoveredListener_error(caller, error);
	};

	window.nfc_erase_success = function(caller, success) {
		connector.nfc_erase_success(caller, success);
	};

	window.nfc_erase_error = function(caller, error) {
		connector.nfc_erase_error(caller, error);
	};

	window.nfc_startNdefListener_callback = function(caller, callback) {
		connector.nfc_startNdefListener_callback(caller, callback)
	};
	window.nfc_startNdefListener_success = function(caller, successCallback) {
		connector.nfc_startNdefListener_success(caller, successCallback);
	};
	window.nfc_startNdefListener_error = function(caller, error) {
		connector.nfc_startNdefListener_error(caller, error);
	};

	window.nfc_stopNdefListener_success = function(caller, successCallback) {
		connector.nfc_stopNdefListener_success(caller, successCallback);
	};
	window.nfc_stopNdefListener_error = function(caller, error) {
		connector.nfc_stopNdefListener_error(caller, error);
	};

	window.nfc_write_success = function(caller, successCallback) {
		connector.nfc_write_success(caller, successCallback);
	};
	window.nfc_write_error = function(caller, error) {
		connector.nfc_write_error(caller, error);
	};

	window.nfc_makeReadOnly_success = function(caller, successCallback) {
		connector.nfc_makeReadOnly_success(caller, successCallback);
	};
	window.nfc_makeReadOnly_error = function(caller, error) {
		connector.nfc_makeReadOnly_error(caller, error);
	};

	window.nfc_showSettings_success = function(caller, successCallback) {
		connector.nfc_showSettings_success(caller, successCallback);
	};
	window.nfc_showSettings_error = function(caller, error) {
		connector.nfc_showSettings_error(caller, error);
	};

};

var ndefListener;
function nfc_startNdefListener(caller) {
	ndefListener = function(callback) {
		window.nfc_startNdefListener_callback(caller, callback);
	};
	var success = function(succesCallback) {
		window.nfc_startNdefListener_success(caller, succesCallback);
	};

	var error = function(error) {
		window.nfc_startNdefListener_error(caller, error);
	};

	nfc.addNdefListener(ndefListener, success, error);
}

function nfc_stopNdefListener(caller) {

	var success = function(succesCallback) {
		window.nfc_stopNdefListener_success(caller, succesCallback);
	};

	var error = function(error) {
		window.nfc_stopNdefListener_error(caller, error);
	};

	nfc.removeNdefListener(ndefListener, success, error);
}

var tagDiscoveredListener;
function nfc_startTagDiscoveredListener(caller) {
	tagDiscoveredListener = function(callback) {
		window.nfc_startTagDiscoveredListener_callback(caller, callback);
	};

	var success = function(succesCallback) {
		window.nfc_startTagDiscoveredListener_success(caller, succesCallback);
	};

	var error = function(error) {
		window.nfc_startTagDiscoveredListener_error(caller, error);
	};

	nfc.addTagDiscoveredListener(tagDiscoveredListener, success, error);
}

function nfc_stopTagDiscoveredListener(caller) {
	var success = function(succesCallback) {
		window.nfc_stopTagDiscoveredListener_success(caller, succesCallback);
	};

	var error = function(error) {
		window.nfc_stopTagDiscoveredListener_error(caller, error);
	};

	nfc.removeTagDiscoveredListener(tagDiscoveredListener, success, error);
}

var writeListener;
function nfc_write_text(caller, messageString) {
	var success = function(success) {
		window.nfc_write_success(caller, success);
		nfc.removeNdefListener(writeListener);
	}

	var error = function(error) {
		window.nfc_write_error(caller, error);
		nfc.removeNdefListener(writeListener);
	}

	writeListener = function(callback) {
		var message = [ ndef.textRecord(messageString) ];
		nfc.write(message, success, error);
	}

	var writeListenersuccess = function(success) {
		console.log(success);
	};

	var writeListenererror = function(error) {
		console.log(error);
	};

	nfc.addNdefListener(writeListener, writeListenersuccess,
					writeListenererror);

}

function nfc_makeReadOnly(caller) {
	var success = function(success) {
		window.nfc_makeReadOnly_success(caller, success);
	}

	var error = function(error) {
		window.nfc_makeReadOnly_error(caller, error);
	}

	nfc.makeReadOnly(success, error);
}

function nfc_showSettings(caller) {
	var success = function(success) {
		window.nfc_showSettings_success(caller, success);
	}

	var error = function(error) {
		window.nfc_showSettings_error(caller, error);
	}

	nfc.showSettings(success, error);
}

var listenerRead;
function nfc_erase_tag(caller) {

	var success = function(success) {
		window.nfc_erase_success(caller, success);
		nfc.removeNdefListener(listenerRead);
	};

	var error = function(error) {
		window.nfc_erase_error(caller, error);
		nfc.removeNdefListener(listenerRead);
	};

	listenerRead = function(callback) {
		nfc.erase(success, error);
	}

	var listenersuccess = function(success) {
		console.log(success);
	};

	var listenererror = function(error) {
		console.log(error);
	};

	nfc.addNdefListener(listenerRead, listenersuccess, listenererror);

};

function nfc_remove_all_Listener() {
	// TODO nfc.removeMimeTypeListener(mimeType, callback, [onSuccess],
	// [onFailure]);
	nfc.removeTagDiscoveredListener(tagDiscoveredListener);
	nfc.removeNdefListener(ndefListener);
	nfc.removeNdefListener(listenerRead);
}