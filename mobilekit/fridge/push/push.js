com_xdev_mobile_service_push_PushService = function() {
	var connector = this;
	
	window.push_on_registration = function(data) {
		connector.push_on_registration(data);
	};	
	window.push_on_notification = function(data) {
		connector.push_on_notification(data);
	};	
	window.push_on_error = function(error) {
		connector.push_on_error(error);
	};
	
	window.push_setApplicationIconBadgeNumber_success = function(caller) {
		connector.push_setApplicationIconBadgeNumber_success(caller);
	};
	window.push_setApplicationIconBadgeNumber_error = function(caller) {
		connector.push_setApplicationIconBadgeNumber_error(caller);
	};
	
	window.push_clearAllNotifications_success = function(caller) {
		connector.push_clearAllNotifications_success(caller);
	};
	window.push_clearAllNotifications_error = function(caller) {
		connector.push_clearAllNotifications_error(caller);
	};
	
	window.push_call_action_callback = function(name,data){
		connector.push_action_callback(name,data);
	};
};

var push;

function push_init(androidSenderID) {
	push = PushNotification.init({
	    android: {
	        senderID: androidSenderID
	    },
	    ios: {
	        alert: "true",
	        badge: "true",
	        sound: "true"
	    },
	    windows: {}
	});
}

var registrationListener;

function push_addRegistrationListener() {
	registrationListener = function(data) {
		window.push_on_registration(data)
	};
	push.on('registration', registrationListener);
}

function push_removeRegistrationListener() {
	if(registrationListener) {
		push.off('registration', registrationListener);
		delete registrationListener;
	}
}

var notificationListener;

function push_addNotificationListener() {
	notificationListener = function(data) {
		window.push_on_notification(data)
	};
	push.on('notification', notificationListener);
}

function push_removeNotificationListener() {
	if(notificationListener) {
		push.off('notification', notificationListener);
		delete notificationListener;
	}
}

var errorListener;

function push_addErrorListener() {
	errorListener = function(error) {
		window.push_on_error(error)
	};
	push.on('error', errorListener);
}

function push_removeErrorListener() {
	if(errorListener) {
		push.off('error', errorListener);
		delete errorListener;
	}
}

function push_setApplicationIconBadgeNumber(caller,number) {
	var success = function(contact) {
		window.push_setApplicationIconBadgeNumber_success(caller);
	};

	var error = function(error) {
		window.push_setApplicationIconBadgeNumber_error(caller);
	};

	push.setApplicationIconBadgeNumber(success, error, number);
}

function push_clearAllNotifications(caller) {
	var success = function(contact) {
		window.push_clearAllNotifications_success(caller);
	};

	var error = function(error) {
		window.push_clearAllNotifications_error(caller);
	};

	push.clearAllNotifications(success, error);
};

function push_register_action_callbacks(names) {
	for (index = 0, len = names.length; index < len; ++index) {
		push_register_action_callback(names[index]);
	}
}

function push_register_action_callback(name) {
	window["push_action_callback_"+name] = function(data){
		window.push_call_action_callback(name,data);
	};
}
