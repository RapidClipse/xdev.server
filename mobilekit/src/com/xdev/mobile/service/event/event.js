com_xdev_mobile_service_event_EventService = function() {
	var connector = this;

	window.onBackKeyDown = function(caller) {
		connector.onBackKeyDown(caller);
	};
};

function event_closeApp() {
	navigator.app.exitApp();
}

function event_onBackKeyDown(caller) {
	var onBackKeyDown = function() {
		window.onBackKeyDown(caller);
	}

	document.addEventListener("backbutton", onBackKeyDown, false);
}
