com_xdev_mobile_service_deviceinfo_DeviceInfoService = function() {
	var connector = this;
	window.deviceinfo_callback = function(caller, info) {
		connector.deviceinfo_callback(caller, info);
	};
};

function deviceinfo_get(caller) {

	var info = {};
	info.model = device.model;
	info.platform = device.platform;
	info.uuid = device.uuid;
	info.version = device.version;
	info.manufacturer = device.manufacturer;
	info.virtual = device.isVirtual;
	info.serial = device.serial;
	window.deviceinfo_callback(caller,info);
}
