com_xdev_mobile_service_app_AppService = function() {
	var connector = this;

	window.app_onPause = function() {
		connector.app_onPause();
	};

	window.app_onResume = function() {
		connector.app_onResume();
	};

	window.app_onBackButton = function() {
		connector.app_onBackButton();
	};

	window.app_onMenuButton = function() {
		connector.app_onMenuButton();
	};

	window.app_onSearchButton = function() {
		connector.app_onSearchButton();
	};

	window.app_onVolumeDownButton = function() {
		connector.app_onVolumeDownButton();
	};

	window.app_onVolumeUpButton = function() {
		connector.app_onVolumeUpButton();
	};
};

function app_closeApp() {
	navigator.app.exitApp();
}

function app_clearCache() {
	var success = function(status) {
		console.log("Cache cleared successfully: " + status)
	}

	var error = function(status) {
		console.log("Error clearing cache: " + status);
	}

	window.cache.clear(success, error);
	window.cache.cleartemp();
}

var pauseDelegate = function() {
	window.app_onPause();
}

var resumeDelegate = function() {
	window.app_onResume();
}

var backButtonDelegate = function() {
	window.app_onBackButton();
}

var menuButtonDelegate = function() {
	window.app_onMenuButton();
}

var searchButtonDelegate = function() {
	window.app_onSearchButton();
}

var volumeDownButtonDelegate = function() {
	window.app_onVolumeDownButton();
}

var volumeUpButtonDelegate = function() {
	window.app_onVolumeUpButton();
}

function app_addPauseHandler() {
	document.addEventListener("pause", pauseDelegate, false);
}

function app_removePauseHandler() {
	document.removeEventListener("pause", pauseDelegate);
}

function app_addResumeHandler() {
	document.addEventListener("resume", resumeDelegate, false);
}

function app_removeResumeHandler() {
	document.removeEventListener("resume", resumeDelegate);
}

function app_addBackButtonHandler() {
	document.addEventListener("backbutton", backButtonDelegate, false);
}

function app_removeBackButtonHandler() {
	document.removeEventListener("backbutton", backButtonDelegate);
}

function app_addMenuButtonHandler() {
	document.addEventListener("menubutton", menuButtonDelegate, false);
}

function app_removeMenuButtonHandler() {
	document.removeEventListener("menubutton", menuButtonDelegate);
}

function app_addSearchButtonHandler() {
	document.addEventListener("searchbutton", searchButtonDelegate, false);
}

function app_removeSearchButtonHandler() {
	document.removeEventListener("searchbutton", searchButtonDelegate);
}

function app_addVolumeDownButtonHandler() {
	document.addEventListener("volumedownbutton", volumeDownButtonDelegate,
			false);
}

function app_removeVolumeDownButtonHandler() {
	document.removeEventListener("volumedownbutton", volumeDownButtonDelegate);
}

function app_addVolumeUpButtonHandler() {
	document.addEventListener("volumeupbutton", volumeUpButtonDelegate, false);
}

function app_removeVolumeUpButtonHandler() {
	document.removeEventListener("volumeupbutton", volumeUpButtonDelegate);
}
