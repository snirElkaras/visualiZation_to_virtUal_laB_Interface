


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
			cleanView();
			var fileExt = request.getResponseHeader('fileExt');
			switch(fileExt){
			case "xml":
				loadTree(data);
				removeListener();
				break;
			case "log":
				loadTemporal(data);
				removeListener();
				break;
			default:
				console.info("unknown file extension");
			}
			$("#footer").css("display", "inline-block");
		}});
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
}