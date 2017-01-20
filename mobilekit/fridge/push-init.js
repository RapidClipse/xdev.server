
document.addEventListener("deviceready", initPush, false);

function initPush() {
	
	var push = PushNotification.init({
		android : {
			senderID : "${senderID}",
			icon: "icon"
		},
		ios : {
			alert : "true",
			badge : "true",
			sound : "true"
		},
		windows : {}
	});
    window.mobilekit_push = push;
	
	var push_data = { 
		registrations : [], 
		notifications : [],
		errors: [],
		addRegistration : function(data){
			console.log("Pre registration:");
			console.log(data);
			this.registrations.push(data);
		},
		addNotification : function(data){
			console.log("Pre notification:");
			console.log(data);
			this.notifications.push(data);
		},
		addError : function(error){
			console.log("Pre error:");
			console.log(error);
			this.errors.push(error);
		}
	};	
	window.mobilekit_push_data = push_data;

	push.on("registration", function(data){
		push_data.addRegistration(data);
	});
    push.on("notification", function(data){
		push_data.addNotification(data);
	});
    push.on("error", function(error){
		push_data.addError(error);
	});
}