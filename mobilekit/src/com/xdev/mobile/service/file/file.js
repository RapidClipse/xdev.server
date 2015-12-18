com_xdev_mobile_service_file_FileService = function() {
	var connector = this;
	window.file_readDirectoryEntries_success = function(caller, entries) {
		connector.file_readDirectoryEntries_success(caller, entries);
	};
	window.file_readDirectoryEntries_error = function(caller, error) {
		connector.file_readDirectoryEntries_error(caller, error);
	};
	
	window.file_getMetadata_success = function(caller, entries, entryType) {
		connector.file_getMetadata_success(caller, entries);
	};
	window.file_getMetadata_error = function(caller, error, entryType) {
		connector.file_getMetadata_error(caller, error);
	};
};




function file_readDirectoryEntries(caller) {

	var success = function(entries) {
		window.file_readDirectoryEntries_success(caller, entries);
	};

	var error = function(error) {
		window.file_readDirectoryEntries_error(caller, error);
	};
	
	var directoryReader = dirEntry.createReader();

	// Get a list of all the entries in the directory
	// successCallback - A callback that is passed an array of FileEntry and DirectoryEntry objects. (Function)
	directoryReader.readEntries(success,fail);
}



function file_getMetaData(caller, entryType) {

	var success = function(metadata) {
		window.file_readDirectoryEntries_success(caller, metadata, entryType);
	};

	var error = function(error) {
		window.file_readDirectoryEntries_error(caller, error, entryType);
	};
	
	//TODO  Metadata object by calling the getMetadata method of a [DirectoryEntry]
	//(../directoryentry/directoryentry.html) or [FileEntry](../fileentry/fileentry.html) object.
	
	// entry.getMetadata(win, null);
}