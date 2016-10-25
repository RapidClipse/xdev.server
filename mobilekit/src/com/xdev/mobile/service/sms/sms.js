com_xdev_mobile_service_sms_SmsService = function() {
	var connector = this;
	window.sms_send_success = function(caller, acceleration) {
		connector.sms_send_success(caller, acceleration);
	};
	window.sms_send_error = function(caller, error) {
		connector.sms_send_error(caller, error);
	};
};

function sms_send(caller,number,message,options) {

	var success = function() {
		window.sms_send_success(caller);
	};

	var error = function(error) {
		window.sms_send_error(caller, error);
	};

	sms.send(number,message,options,success, error);
}