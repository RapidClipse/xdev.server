com_xdev_mobile_service_app_AppService = function() {
	var connector = this;

	window.onBackKeyDown = function(caller) {
		connector.onBackKeyDown(caller);
	};
};

function app_closeApp() {
	navigator.app.exitApp();
}

function app_onBackKeyDown(caller) {
	var onBackKeyDown = function() {
		window.onBackKeyDown(caller);
	}

	document.addEventListener("backbutton", onBackKeyDown, false);
}
