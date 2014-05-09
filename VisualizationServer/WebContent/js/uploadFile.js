
var removeListener = function(){
	var dropbox = document.getElementById("viewContainer");
	dropbox.removeEventListener("drop", dropUpload, false);
	dropbox.addEventListener("drop", noop, false);
}

function noop(event) {
	event.stopPropagation();
	event.preventDefault();
}

function dropUpload(files) {
	for (var i = 0; i < files.length; i++) {
		upload(files[i]);
	}
}



function upload(file) {
	removeErrorNotification();
	cleanView();
	var formData = new FormData();
	formData.append("file", file);

	/*var xhr = new XMLHttpRequest();
        xhr.upload.addEventListener("progress", uploadProgress, false);
        xhr.addEventListener("load", uploadComplete, false);
        xhr.open("POST", "UploadServlet", true); // If async=false, then you'll miss progress bar support.
        xhr.send(formData);*/
	$.ajax({
		url : 'UploadServlet',
		data: formData,
		dataType: "json",
		processData: false, // Don't process the files
		contentType: false, // Set content type to false as jQuery will tell the server its a query string request
		async : false,
		type: 'POST',
		success : function(data, textStatus, request){
			var fileExt = request.getResponseHeader('fileExt');
			switch(fileExt){
			case "xml":
				loadTree(data);
				removeListener();
				displayDetailsPlan();
				break;
			case "log":
				loadTemporal(data);
				removeListener();
				displayDetailsTemporal();
				break;
			default:
				console.info("unknown file extension");
			}
		},
		error: function(data, textStatus, errorThrown){
			uploadFailed(data.getResponseHeader('invalidFile'));
		}
	});
}

function uploadFailed(text){
	$("#errorNotification").text(text);
	$("#errorNotification").css("display", "inline-block");
}

function removeErrorNotification(){
	$("#errorNotification").css("display", "none");
}

function uploadProgress(event) {
	// Note: doesn't work with async=false.
	var progress = Math.round(event.loaded / event.total * 100);
	document.getElementById("status").innerHTML = "Progress " + progress + "%";
}

function uploadComplete(event) {
	document.getElementById("status").innerHTML = event.target.responseText;
}

var cleanView = function (){
	$("#viewContainer").empty();
	$("#details").empty();
	$("#footer").css("display", "none");
	$("#buttonsPlan").css("display", "none");
	$("#indexTemporal").css("display", "none");
}

var displayDetailsPlan = function(){
	displayDetails("#buttonsPlan");
}
var displayDetailsTemporal = function(){
	displayDetails("#indexTemporal");
}

var displayDetails = function(typeID){
	$("#footer").css("display", "inline-block");
	$(typeID).css("display", "inline-block");
	$("#footer").css("display", "inline-block");
}
