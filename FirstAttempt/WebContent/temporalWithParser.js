$.ajax({
	url: 'uploadTemporalFile',
	type: 'post',
	async: false,
	success: function() {
		$.ajax({
			url: 'getTemporalViewFlasksList',
			type: 'get',
			async: false,
			success: function() {
				flasksList();
			},
			error: function(){
				errorHandler();
			}
		});
		$.ajax({
			url: 'getTemporalViewSolutionMixList',
			type: 'get',
			async: false,
			success: function() {
				solutionMixList();
			},
			error: function(){
				errorHandler();
			}
		});
	},
	error: function(){
		errorHandler();
	}
});

var flasksList = function(){
	alert("flasksList");
}

var solutionMixList = function(){
	alert("solutionMixList");
}

var errorHandler = function(){
	alert("errorHandler");
}
