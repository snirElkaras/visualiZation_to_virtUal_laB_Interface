/*var jsonData;
$.ajax({
	url: 'Json.txt',
	type: 'get',
	async: false,
	success: function(data) {
		jsonData = JSON.parse(data);
	}
});*/
var loadTemporal = function (jsonData){

	var flasksAsJson = jsonData.add_flask_list;
	var solutionMixAsJson = jsonData.solution_mix_list;
	var flasks = new Array();
	var i = 0;
	flasksAsJson.forEach(function(entry) {
		flasks[i] = entry.flask.id;
		i++;
	});

	var flasksCount = flasks.length;
	var startTime;
	var endTime;
//	set the stage
	var margin = {
			t : 30,
			r : 200,
			b : 20,
			l : 40
	},

	w = 1000 - margin.l - margin.r, h = 500 - margin.t - margin.b, hIndent = h - 60, x = d3.time.scale().rangeRound([ 0, w ]), y = d3.scale.ordinal().rangeBands(
			[ hIndent, 0 ]);
//	colors that will reflect geographical regions
	color = d3.scale.category10();


	function zoom() {
		svg.attr("transform", "translate(" + d3.event.translate + ")scale(" + d3.event.scale + ")");
	}

	var zoomListener = d3.behavior.zoom().scaleExtent([0.1, 3]).on("zoom", zoom);


	var svgBase = d3.select("#viewContainer").append("svg").call(zoomListener);

	var svg = svgBase.append("g");

	svg.attr("transform", "scale(0.85)");

//	set axes, as well as details on their ticks
	var xAxis = d3.svg.axis().scale(x).ticks(20).tickSubdivide(false).tickSize(6,
			3, 0).orient("bottom");

	var yAxis = d3.svg.axis().scale(y).ticks(20).tickSubdivide(true).tickSize(6, 3,
			0).orient("left");

//	group that will contain all of the plots
	var groups = svg.append("g").attr("transform",
			"translate(" + margin.l + "," + margin.t + ")");

	var calcTimePadd = function(startTime, endTime, paddPercent){
		timeDiff = endTime.getTime()-startTime.getTime();
		timePadding = timeDiff*paddPercent;
		return timePadding;
	}
	/***add grid***/
	function make_x_axis() {        
		return d3.svg.axis()
		.scale(x)
		.orient("bottom")
		.ticks(20);
	}

	function make_y_axis() {        
		return d3.svg.axis()
		.scale(y)
		.orient("left")
		.ticks(flasksCount);
	}


	/*****************/
	var findFirstAndLastSolMix = function(){
		var candidateFirst = solutionMixAsJson[0];
		var candidateLast = solutionMixAsJson[0];
		solutionMixAsJson.forEach(function(entry){
			if(candidateFirst.timestamp > entry.timestamp){
				candidateFirst = entry;
			}
			if(candidateLast.timestamp < entry.timestamp){
				candidateLast = entry;
			}
		});
		return {"first" : candidateFirst, "last" : candidateLast} ;
	};

//	calculates axis x boundries
	var firstAndLastSolMix = findFirstAndLastSolMix();
	var startTime = new Date(parseInt(firstAndLastSolMix.first.timestamp));
	var endTime = new Date(parseInt(firstAndLastSolMix.last.timestamp));
	var timePadding = calcTimePadd(startTime, endTime, 0.1);
	var startTime = startTime.getTime()-timePadding;
	var endTime = endTime.getTime()+timePadding;

	var data = [];
	for(var key in solutionMixAsJson){
		var entry = solutionMixAsJson[key];
		data.push(entry);
	}





//	sort data alphabetically, so that the colors match src and trg

	data.sort(function(a, b) {
		return d3.ascending(a.event_id, b.event_id);
	});

	var x0 = Math.max(-d3.min(data, function(d) {
		return d.timestamp;
	}), d3.max(data, function(d) {
		return d.timestamp;
	}));
	x.domain([ startTime , endTime ]);
	y.domain(flasks);

	groups.selectAll("trgCircle").data(data).enter()
	.append("circle").attr("class", "circles").attr({
		cx : function(d) {
			return x(new Date(parseInt(d.timestamp)));
		},
		cy : function(d) {
			return y(d.recipient_flask.id) + (hIndent / flasksCount) / 2;
		}, // (range/#flasks)/2
		r : 8,
		id : function(d) {
			return d.recipient_flask.id;
		},
		event_id :  function(d) {
			return d.event_id;
		},
		readable :  function(d) {
			return d.readable;
		},
		type : function(d){
			return "trg"; 
		} 

	}).style("fill", "orange");

//	style the circles, set their locations based on data
	groups.selectAll("srcCircle").data(data).enter()
	.append("circle").attr("class", "circles").attr({
		cx : function(d) {
			return x(new Date(parseInt(d.timestamp)));
		},
		cy : function(d) {
			return y(d.source_flask.id) + (hIndent / flasksCount) / 2;
		}, // (range/#flasks)/2
		r : 8,
		id : function(d) {
			return d.source_flask.id;
		},
		event_id :  function(d) {
			return d.event_id;
		},
		readable :  function(d) {
			return d.readable;
		},
		type : function(d){
			return "src"; 
		} 
	}).style("fill", "blue");

	var circles = groups.selectAll("circle");

//	what to do when we mouse over a bubble
	var mouseOn = function() {
		var pair = "circle[event_id=" + this.getAttribute("event_id") + "]";
		$(pair).each(function() {
			// transition to increase size/opacity of bubble
			var currCircle = d3.select(this);
			currCircle.transition().duration(800).style("opacity", 1).attr("r", 10)
			.ease("elastic");

		});
		var circle = d3.select(this);
		$("#details").text("");
		$("#details").text(this.getAttribute("readable"));
		// append lines to bubbles that will be used to show the precise data
		// points.
		// translate their location based on margins
		
		var cx1, cy1, cx2, cy2; 
		var srcLoc, trgLoc;
		if($(pair)[0].getAttribute("type")=="src"){
			srcLoc = $(pair)[0];
			trgLoc = $(pair)[1];
		}
		else{
			srcLoc = $(pair)[1];
			trgLoc = $(pair)[0];
		}

		cx1 = srcLoc.getAttribute("cx");
		cx2 = trgLoc.getAttribute("cx");
		cy1 = srcLoc.getAttribute("cy");
		cy2 = trgLoc.getAttribute("cy");

		var radius = 8;
		var arrowPos = (cy1-cy2) ? 10 : 20;
		// when trg on top - refX is 20
		// when src on top - refX is 10
		// todo: define between 2 cases,
		// narrow the triangle,
		// shorten the line in both cases
		
		svg.append("g")
		.append("svg:marker")
		.attr("id", "arrow")
		.attr("viewBox", "0 0 10 10")
		.attr("refX", arrowPos)
		.attr("refY", 5)
		.attr("markerUnits", "strokeWidth")
		.attr("markerWidth", 8)
		.attr("markerHeight", 6)
		.attr("orient", "auto")
		.append("svg:path")
		.attr("d", "M 0 0 L 10 5 L 0 10 z")


		svg.append("g").attr("class", "guide")
		.append("line")
		.attr("x1", cx1)
		.attr("x2", cx2)
		.attr("y1", cy1)
		.attr("y2", cy2)

//		.attr("x1",	circle.attr("cx"))
//		.attr("x2", circle.attr("cx"))
//		.attr("y1",	circle.attr("cy"))
//		.attr("y2", h - margin.t - margin.b)
		.attr("transform", "translate(40,20)")
		.style("stroke", "pink")
		.attr("stroke-width", 3)
		.attr ("marker-end", "url(\#arrow)")
		.transition()
		.delay(200).duration(400)
		.styleTween("opacity", function() {return d3.interpolate(0, .5);})

//		svg.append("g").attr("class", "guide").append("line").attr("x1",
//		+circle.attr("cx") - 16).attr("x2", 0).attr("y1",
//		circle.attr("cy")).attr("y2", circle.attr("cy"))
//		.attr("transform", "translate(40,30)").style("stroke",
//		circle.style("fill")).transition().delay(200).duration(400)
//		.styleTween("opacity", function() {
//		return d3.interpolate(0, .5);
//		});

		// function to move mouseover item to front of SVG stage, in case
		// another bubble overlaps it
		d3.selection.prototype.moveToFront = function() {
			return this.each(function() {
				this.parentNode.appendChild(this);
			});
		};
	};
//	what happens when we leave a bubble?
	var mouseOff = function() {
		var pair = "circle[event_id=" + this.getAttribute("event_id") + "]";
		$(pair).each(function() {
			// transition to increase size/opacity of bubble
			var currCircle = d3.select(this);
			// go back to original size and opacity
			currCircle.transition().duration(800).style("opacity", .5).attr("r", 8)
			.ease("elastic");
		});

		// fade out guide lines, then remove them
		d3.selectAll(".guide").transition().duration(100).styleTween("opacity",
				function() {
			return d3.interpolate(.5, 0);
		}).remove();
	};

//	run the mouseon/out functions
	circles.on("mouseover", mouseOn);
	circles.on("mouseout", mouseOff);

//	tooltips (using jQuery plugin tipsy)
	groups.selectAll("circle")[0].forEach(function(circ){
		return circ.setAttribute("title", circ.getAttribute("id"));
	})

	$(".circles").tipsy({
		gravity : 's',
	});

	var src = [ "source", "target" ];
//	the legend color guide
	var legend = svg.selectAll("rect").data(src).enter()
	.append("rect").attr({
		x : function(d, i) {
			return (40 + i * 80);
		},
		y : h+20,
		width : 25,
		height : 12
	}).style("fill", function(d) {
		return d=="source"? "blue" : "orange";
	});

//	// legend labels
	svg.selectAll("text").data(src).enter().append("text")
	.attr({
		x : function(d, i) {
			return (40 + i * 80);
		},
		y : h +44,
	}).text(function(d) {
		return d;
	});

//	draw axes and axis labels
	svg.append("g").attr("class", "x axis").attr("transform",
			"translate(" + margin.l + "," + (h - 60 + margin.t) + ")").call(
					xAxis).selectAll("text").style("text-anchor", "end").attr("dx",
					"-.8em").attr("dy", ".15em").attr("transform", function(d) {
						return "rotate(-65)"
					});

	svg.append("g").attr("class", "y axis").attr("transform",
			"translate(" + margin.l + "," + margin.t + ")").call(yAxis);

	svg.append("text").attr("class", "x label").attr("text-anchor", "end")
	.attr("x", w + 50).attr("y", h - margin.t - 5).text("Timeline");

	svg.append("text").attr("class", "y label").attr("text-anchor", "end")
	.attr("x", -20).attr("y", 45).attr("dy", ".75em").attr("transform",
	"rotate(-90)").text("Flasks");

	svg.append("g")         
	.attr("class", "grid")
	.attr("transform", "translate(" + margin.l + "," + (h - 60 + margin.t) + ")")
	.call(make_x_axis()
			.tickSize(-h+60, 0, 0)
			.tickFormat("")
	)

	svg.append("g")         
	.attr("class", "grid")
    .attr("transform", "translate(" + margin.l + "," + margin.t + ")")	
	.call(make_y_axis()
			.tickSize(-w, 0, 0)
			.tickFormat("")
	)

//	svg.append("svg:defs")
//	.append("svg:marker")
//	.attr("id", "arrow")
//	.attr("viewBox", "0 0 10 10")
//	.attr("refX", 27)
//	.attr("refY", 5)
//	.attr("markerUnits", "strokeWidth")
//	.attr("markerWidth", 8)
//	.attr("markerHeight", 6)
//	.attr("orient", "auto")
//	.append("svg:path")
//	.attr("d", "M 0 0 L 10 5 L 0 10 z")

//	svg.append("line")
//	.attr ("x1", 5) 
//	.attr ("x2", 50) 
//	.attr ("y1", 5)
//	.attr ("y2", 50)
//	.style ("stroke", "black")
//	.attr ("stroke-width", 2)
//	.attr ("marker-end", "url(\#arrow)")
}
