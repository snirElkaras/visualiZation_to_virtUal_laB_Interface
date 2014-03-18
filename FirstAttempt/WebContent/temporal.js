//hard coded
var csvAsString;
$.ajax({
	url: 'add_flask_ex.csv',
	type: 'get',
	async: false,
	success: function(csv) {
		csvAsString = csv;
	}
});
var dataCSV = $.csv.toObjects(csvAsString,  {"separator":"\t"});
var flasks = new Array();
for(var i = 0; i<dataCSV.length; i++){
	flasks[i] = dataCSV[i].flask_id;
}

var flasksCount = flasks.length;
var startTime;
var endTime;
//set the stage
var margin = {
		t : 30,
		r : 20,
		b : 20,
		l : 40
},

w = 1000 - margin.l - margin.r, h = 500 - margin.t - margin.b, hIndent = h - 60, x = d3.time.scale().rangeRound([ 0, w ]), y = d3.scale.ordinal().rangeBands(
		[ hIndent, 0 ]);
//colors that will reflect geographical regions
color = d3.scale.category10();
var svg = d3.select("#chart").append("svg").attr("width",
		w + margin.l + margin.r).attr("height", h + margin.t + margin.b);

//set axes, as well as details on their ticks
var xAxis = d3.svg.axis().scale(x).ticks(20).tickSubdivide(false).tickSize(6,
		3, 0).orient("bottom");

var yAxis = d3.svg.axis().scale(y).ticks(20).tickSubdivide(true).tickSize(6, 3,
		0).orient("left");

//group that will contain all of the plots
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



//bring in the data, and do everything that is data-driven
d3.tsv("solution_mix_ex.csv", function(data) {
	startTime = new Date(parseInt(data[0].timestamp));
	endTime = new Date(parseInt(data[data.length-1].timestamp));
	timePadding = calcTimePadd(startTime, endTime, 0.1);
	startTime = startTime.getTime()-timePadding;
	endTime = endTime.getTime()+timePadding;

	// sort data alphabetically, so that the colors match src and trg
	data.sort(function(a, b) {
		return d3.ascending(a.action_id, b.action_id);
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
			return y(d.trg_id) + (hIndent / flasksCount) / 2;
		}, // (range/#flasks)/2
		r : 8,
		id : function(d) {
			return d.trg_id;
		},
		action_id :  function(d) {
			return d.action_id;
		}
		
	}).style("fill", "orange");
	
	// style the circles, set their locations based on data
	groups.selectAll("srcCircle").data(data).enter()
	.append("circle").attr("class", "circles").attr({
		cx : function(d) {
			return x(new Date(parseInt(d.timestamp)));
		},
		cy : function(d) {
			return y(d.src_id) + (hIndent / flasksCount) / 2;
		}, // (range/#flasks)/2
		r : 8,
		id : function(d) {
			return d.src_id;
		},
		action_id :  function(d) {
			return d.action_id;
		}
	}).style("fill", "blue");
	
	var circles = groups.selectAll("circle");

	// what to do when we mouse over a bubble
	var mouseOn = function() {
		var pair = "circle[action_id=" + this.getAttribute("action_id") + "]";
		$(pair).each(function() {
			// transition to increase size/opacity of bubble
			var currCircle = d3.select(this);
			currCircle.transition().duration(800).style("opacity", 1).attr("r", 16)
			.ease("elastic");

		});
		var circle = d3.select(this);
		$("#details").text(this.id);
		// append lines to bubbles that will be used to show the precise data
		// points.
		// translate their location based on margins
		svg.append("g").attr("class", "guide").append("line").attr("x1",
				circle.attr("cx")).attr("x2", circle.attr("cx")).attr("y1",
						+circle.attr("cy") + 26).attr("y2", h - margin.t - margin.b)
						.attr("transform", "translate(40,20)").style("stroke",
								circle.style("fill")).transition().delay(200).duration(
										400).styleTween("opacity", function() {
											return d3.interpolate(0, .5);
										})

										svg.append("g").attr("class", "guide").append("line").attr("x1",
												+circle.attr("cx") - 16).attr("x2", 0).attr("y1",
														circle.attr("cy")).attr("y2", circle.attr("cy")).attr(
																"transform", "translate(40,30)").style("stroke",
																		circle.style("fill")).transition().delay(200).duration(400)
																		.styleTween("opacity", function() {
																			return d3.interpolate(0, .5);
																		});

		// function to move mouseover item to front of SVG stage, in case
		// another bubble overlaps it
		d3.selection.prototype.moveToFront = function() {
			return this.each(function() {
				this.parentNode.appendChild(this);
			});
		};

		// skip this functionality for IE9, which doesn't like it
		if (!$.browser.msie) {
			circle.moveToFront();
		}
	};
	// what happens when we leave a bubble?
	var mouseOff = function() {
		var pair = "circle[action_id=" + this.getAttribute("action_id") + "]";
		$(pair).each(function() {
			// transition to increase size/opacity of bubble
			var currCircle = d3.select(this);
			// go back to original size and opacity
			currCircle.transition().duration(800).style("opacity", .5).attr("r", 8)
			.ease("elastic");
		});
		var circle = d3.select(this);
		$("#details").text("");

		// fade out guide lines, then remove them
		d3.selectAll(".guide").transition().duration(100).styleTween("opacity",
				function() {
			return d3.interpolate(.5, 0);
		}).remove()
	};

	// run the mouseon/out functions
	circles.on("mouseover", mouseOn);
	circles.on("mouseout", mouseOff);

	// tooltips (using jQuery plugin tipsy)
	groups.selectAll("circle")[0].forEach(function(circ){
		return circ.setAttribute("title", circ.getAttribute("id"));
	})

	$(".circles").tipsy({
		gravity : 's',
	});

	var src = [ "source", "target" ];
	// the legend color guide
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

	// // legend labels
	svg.selectAll("text").data(src).enter().append("text")
	.attr({
		x : function(d, i) {
			return (40 + i * 80);
		},
		y : h +44,
	}).text(function(d) {
		return d;
	});

	// draw axes and axis labels
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
});