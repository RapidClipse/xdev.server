com_xdev_mobile_service_app_AppService = function() {
	var connector = this;

	window.app_onBackButton = function() {
		connector.app_onBackButton();
	};
};

function app_closeApp() {
	navigator.app.exitApp();
}

var backButtonDelegate = function() {
	window.app_onBackButton();
}

function app_addBackButtonHandler(){
	document.addEventListener("backbutton", backButtonDelegate, false);
}

function app_removeBackButtonHandler() {
	document.removeEventListener("backbutton", backButtonDelegate);
}
