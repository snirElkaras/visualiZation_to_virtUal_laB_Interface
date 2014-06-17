
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
				styleAfterLoading();
				break;
			case "log":
				loadTemporal(data);
				removeListener();
				displayDetailsTemporal();
				styleAfterLoading();
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
	$("#container").css("display", "inline-block");
	$("#footer").css("display", "inline-block");
	$(typeID).css("display", "inline-block");
	$("#footer").css("display", "inline-block");
}

var chooseFileButton = function(){
	removeErrorNotification();
	$("#chooseFile").click();
    setInterval(intervalFunc, 1);
}

var submitFileButton = function(){
	if($('#chooseFile').val()==""){
		uploadFailed("Please select a file");
	}
	else{
		$("#submitFile").click();
	}
}

var intervalFunc = function () {
	$('#inputTextFile').html($('#chooseFile').val().substring(12));
}

var styleAfterLoading = function(){
	$("#subTitle")
		.css('display', 'inline-table')
		.css('position', 'absolute')
		.css('padding-top', '18px')
		.css('padding-left', '31px');
	$("#mainScreenContainer")
		.css('width', '100%')
		.css('position', 'inherit')
		.css('margin', '0px')
		.css('top', '0%')
		.css('left', '0%')
		.addClass("backgroundColor");
	$("#title")
			.css('display', 'inline-block')
			.css('margin', '0px')
			.css('height', '100px');
	$("#errorNotification")
			.css('margin-left', '0px')
			.css('position', 'inherit')
			.css('bottom', '0px')
			.css('padding-left', '31px');
	$("body")
			.removeClass("backgroundColor");

}
