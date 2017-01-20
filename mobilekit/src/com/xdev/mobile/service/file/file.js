com_xdev_mobile_service_file_FileService = function() {
	var connector = this;
	window.file_readFile_success = function(caller, result) {
		connector.file_readFile_success(caller, result);
	};
	window.file_readFile_error = function(caller, error) {
		connector.file_readFile_error(caller, error);
	};
};

function file_readFile(caller, path) {

	var success = function(result) {
		window.file_readFile_success(caller, result);
	};

	var error = function(error) {
		window.file_readFile_error(caller, error);
	};

	window.resolveLocalFileSystemURL(path, function(fileEntry) {

		fileEntry.file(function(file) {
			var reader = new FileReader();

			reader.onloadend = function() {
				success(this.result);
			};

			reader.readAsDataURL(file);

		}, error);

	}, error);
}
