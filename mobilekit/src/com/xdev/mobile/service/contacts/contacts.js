com_xdev_mobile_service_contacts_ContactsService = function() {
	var connector = this;
	window.contacts_find_success = function(caller, contacts) {
		connector.contacts_find_success(caller, contacts);
	};
	window.contacts_find_error = function(caller, error) {
		connector.contacts_find_error(caller, error);
	};
	window.contacts_pick_success = function(caller, contact) {
		connector.contacts_pick_success(caller, contact);
	};
	window.contacts_pick_error = function(caller, error) {
		connector.contacts_pick_error(caller, error);
	};
};

function contacts_find(caller, fields, options) {

	var success = function(contacts) {
		window.contacts_find_success(caller, contacts);
	};

	var error = function(error) {
		window.contacts_find_error(caller, error);
	};

	navigator.contacts.find(fields, success, error, options);
}

function contacts_pick(caller) {

	var success = function(contact) {
		window.contacts_pick_success(caller, contact);
	};

	var error = function(error) {
		window.contacts_pick_error(caller, error);
	};

	navigator.contacts.pickContact(success, error);
}

function contacts_create(contact){
	var contact = navigator.contacts.create(JSON.parse(contact));
	contact.save();
}