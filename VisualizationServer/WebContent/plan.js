
loadTree = function(data) {

	// update all labels in order to get the maximum length
	var updateLabels = function(d){
		d.name = getNodeName(d);
		if (d.children){
			d.children.forEach(updateLabels);
		}
	}


	var treeData = data;
	var probName = treeData.probName;
	treeData.children.forEach(updateLabels);

	// Calculate total nodes, max label length
	var totalNodes = 0;
	var maxLabelLength = 0;

	// Misc. variables
	var i = 0;
	var duration = 750;
	var root;

	// size of the diagram
	var viewerWidth = $(document).width();
	var viewerHeight = $(document).height()*0.65;

	var tree = d3.layout.tree()
	.size([viewerHeight, viewerWidth]);

	// define a d3 diagonal projection for use by the node paths later on.
	var diagonal = d3.svg.diagonal()
	.projection(function(d) {
		return [d.y, d.x];
	});

	// A recursive helper function for performing some setup by walking through all nodes
	$("#tool-bar").append(
			"<button type=\"button\" id=\"expandButton\">Expand Tree</button>" + 
	"<button type=\"button\" id=\"collapseButton\">Collapse Tree</button>");

	$("#expandButton").click(function() {
		expandTree(root);
	});

	var collapseTree = function(node){
		if(node.children){
			node.children.forEach(collapseTree);
		}
		if(node._children){
			node._children.forEach(collapseTree);
		}
		if (node.children) {
			node._children = node.children;
			node.children = null;
		}
		if(node.name == "root"){
			update(node);
			centerNode(node);
		}
	}

	var expandTree = function(node){
		if(node.children){
			node.children.forEach(expandTree);
		}
		if(node._children){
			node._children.forEach(expandTree);
		}
		if (node._children) {
			node.children = node._children;
			node._children = null;
		}
		if(node.name == "root"){
			update(node);
			leftNode(node);
		}
	}

	// added functionality to the collapse button
	$("#collapseButton").click(function() {
		collapseTree(root);
	});

	function visit(parent, visitFn, childrenFn) {
		if (!parent) return;

		visitFn(parent);

		var children = childrenFn(parent);
		if (children) {
			var count = children.length;
			for (var i = 0; i < count; i++) {
				visit(children[i], visitFn, childrenFn);
			}
		}
	}

	// Call visit function to establish maxLabelLength
	visit(treeData, function(d) {
		totalNodes++;
		maxLabelLength = Math.max(d.name.length, maxLabelLength);

	}, function(d) {
		return d.children && d.children.length > 0 ? d.children : null;
	});


	// Define the zoom function for the zoomable tree
	function zoom() {
		svgGroup.attr("transform", "translate(" + d3.event.translate + ")scale(" + d3.event.scale + ")");
	}


	// define the zoomListener which calls the zoom function on the "zoom" event constrained within the scaleExtents
	var zoomListener = d3.behavior.zoom().scaleExtent([0.1, 3]).on("zoom", zoom);

	// define the baseSvg, attaching a class for styling and the zoomListener
	var baseSvg = d3.select("#viewContainer").append("svg")
	.attr("width", viewerWidth)
	.attr("height", viewerHeight)
	.attr("class", "overlay")
	.attr("id", "mainSVG")
	.call(zoomListener);

	// get the label of node d according to the problem type
	function getNodeName(d){
		if (probName.toLowerCase() === "oracle.xml"){
			return getOracleNodeName(d);
		} else 
			if (probName.toLowerCase() === "unknown acid problem"){
				return getUnknownAcidNodeName(d);
			} else
				return "warning - unknown problem";
	}

	function getUnknownAcidNodeName(d){
		return d.name;
	}

	function getOracleNodeName(d){
		var nodeName = "";
		// first check if its a leaf - if so, then represent the source content
		if (!d.children && !d._children){
			nodeName = getLeafName(d);
		} else
			// not a leaf - check if the node represent a reaction in order to name it as a reaction node
			if (d.information.hasReaction) {
				nodeName = getReactionNodeName(d);
			} else 
			{
				// not a leaf and not a reaction node.
				nodeName = getRegularNodeName(d);
			} 
		return nodeName;

	}

	function getRegularNodeName(d){
		var nodeName = "";
		var hasPlus = false;
		if (d.information.actualAmount_A > 0){
			nodeName = nodeName + "A+";
			hasPlus = true;
		}
		if (d.information.actualAmount_B > 0){
			nodeName = nodeName + "B+";
			hasPlus = true;
		}           		
		if (d.information.actualAmount_C > 0){
			nodeName = nodeName + "C+";
			hasPlus = true;
		}     
		if (d.information.actualAmount_D > 0){
			nodeName = nodeName + "D+";
			hasPlus = true;
		}     
		// delete the unnecessary "plus" char 
		if (hasPlus == true) {
			nodeName = nodeName.substring(0, nodeName.length-1);
		}
		return nodeName;
	}
	
	function getReactionNodeName(d){
		var nodeName = "";
		var hasPlus = false;
		if (d.information.amount_A > 0){
			nodeName = nodeName + "A+";
			hasPlus = true;
		}
		if (d.information.amount_B > 0){
			nodeName = nodeName + "B+";
			hasPlus = true;
		}           		
		if (d.information.amount_C > 0){
			nodeName = nodeName + "C+";
			hasPlus = true;
		}     
		if (d.information.amount_D > 0){
			nodeName = nodeName + "D+";
			hasPlus = true;
		}     
		// delete the unnecessary "plus" char 
		if (hasPlus == true) {
			nodeName = nodeName.substring(0, nodeName.length-1);
		}
		nodeName = "{" + nodeName + "} -> {";
		if (d.information.actualAmount_A > 0){
			nodeName = nodeName + "A+";
			hasPlus = true;
		}
		if (d.information.actualAmount_B > 0){
			nodeName = nodeName + "B+";
			hasPlus = true;
		}           		
		if (d.information.actualAmount_C > 0){
			nodeName = nodeName + "C+";
			hasPlus = true;
		}     
		if (d.information.actualAmount_D > 0){
			nodeName = nodeName + "D+";
			hasPlus = true;
		}     
		// delete the unnecessary "plus" char 
		if (hasPlus == true) {
			nodeName = nodeName.substring(0, nodeName.length-1);
		}      
		nodeName = nodeName + "}";

		return nodeName;
	}
	
	function getLeafName(d){
		var nodeName = "";
		var hasPlus = false;
		if (d.information.srcAmount_A > 0){
			nodeName = nodeName + "A+";
			hasPlus = true;
		}
		if (d.information.srcAmount_B > 0){
			nodeName = nodeName + "B+";
			hasPlus = true;
		}           		
		if (d.information.srcAmount_C > 0){
			nodeName = nodeName + "C+";
			hasPlus = true;
		}     
		if (d.information.srcAmount_D > 0){
			nodeName = nodeName + "D+";
			hasPlus = true;
		}    
		// delete the unnecessary "plus" char 
		if (hasPlus == true) {
			nodeName = nodeName.substring(0, nodeName.length-1);
		}
		return nodeName;

	}

	// moves the selected node to the center of the screen
	function centerNode(source) {
		scale = zoomListener.scale();
		x = -source.y0;
		y = -source.x0;
		x = x * scale + viewerWidth / 2;
		y = y * scale + viewerHeight / 2;
		d3.select('g').transition()
		.duration(duration)
		.attr("transform", "translate(" + x + "," + y + ")scale(" + scale + ")");
		zoomListener.scale(scale);
		zoomListener.translate([x, y]);
	}

	// moves the selected node to the 10% left side of the screen - using it for root node
	function leftNode(source) {
		scale = zoomListener.scale();
		x = -source.y0;
		y = -source.x0;
		x = x * scale + viewerWidth * 0.1;
		y = y * scale + viewerHeight / 2;
		d3.select('g').transition()
		.duration(duration)
		.attr("transform", "translate(" + x + "," + y + ")scale(" + scale + ")");
		zoomListener.scale(scale);
		zoomListener.translate([x, y]);
	}
	
	// Toggle children function
	function toggleChildren(d) {
		if (d.children) {
			d._children = d.children;
			d.children = null;
		} else if (d._children) {
			d.children = d._children;
			d._children = null;
		}
		return d;
	}

	// Toggle children on click.
	function click(d) {
		if (d3.event.defaultPrevented) return; // click suppressed
		d = toggleChildren(d);
		update(d);
		centerNode(d);
	}

	
	function mouseHoverOracle(d){

		// deletes the old information bar
		if ($("#detailsP") != null) 
			$("#detailsP").remove();
		
		// if root node - present the problem name and the answer
		if (d.name == "root"){
			$("#details").append("<p id=detailsP> " +  
					"Answer: ".bold().fontsize(2.8) + d.answer + "<BR/>" + 
					"Problem Name: ".bold().fontsize(2.8) + d.probName + "<BR/>" + 
			"</p>");
			return;
		}
		
		var action = "none";
		if (d.children || d._children) {
			action = "mix";
		}
		var equation = "";
		if (d.information.reactionEquation != "") {
			equation = 	d.information.reactionEquation.bold().fontsize(3.5);
		} else {
			equation = 	getNodeName(d).bold().fontsize(3.5);
		}
		$("#details").append("<p id=detailsP> " + 
				equation + "<BR/>" + 
				"pos: ".bold().fontsize(2.8) + d.information.pos + "<BR/>" + 
				"IDs: ".bold().fontsize(2.8) + d.information.ids + "<BR/>" + 
				"scd: ".bold().fontsize(2.8).fontcolor("blue") + d.information.scdDesc + "<BR/>" + 
				"dcd: ".bold().fontsize(2.8).fontcolor("orange") + d.information.dcdDesc + "<BR/>" + 
				"rcd: ".bold().fontsize(2.8).fontcolor("green") + d.information.rcdDesc + "<BR/>" + 
				"vol: ".bold().fontsize(2.8) + d.information.volume + "<BR/>" + 
				"action: ".bold().fontsize(2.8) + action + " <BR/>" + 
		"</p>");

	}

	function mouseHoverUnknownAcid(d){
		
		// deletes the old information bar
		if ($("#detailsP") != null) 
			$("#detailsP").remove();
		
		// if root node - present the problem name and the answer
		if (d.name == "root"){
			$("#details").append("<p id=detailsP> " +  
					"Answer: ".bold().fontsize(2.8) + d.answer + "<BR/>" + 
					"Problem Name: ".bold().fontsize(2.8) + d.probName + "<BR/>" + 
			"</p>");
			return;
		}
		var action = "none";
		if (d.children || d._children) {
			action = "mix";
		}
		var equation = "";
		equation = 	d.name.bold().fontsize(3.5);


		$("#details").append("<p id=detailsP> " + 
				equation + "<BR/>" + 
				"pos: ".bold().fontsize(2.8) + d.pos + "<BR/>" + 
				"IDs: ".bold().fontsize(2.8) + d.IDs + "<BR/>" + 
				"scd: ".bold().fontsize(2.8).fontcolor("blue") + d.scd + "<BR/>" + 
				"dcd: ".bold().fontsize(2.8).fontcolor("orange") + d.dcd + "<BR/>" + 
				"rcd: ".bold().fontsize(2.8).fontcolor("green") + d.rcd + "<BR/>" + 
				"vol: ".bold().fontsize(2.8) + d.vol + "<BR/>" + 
				"action: ".bold().fontsize(2.8) + action + " <BR/>" + 
		"</p>");
	}

	function mouseHover(d) {
		if (probName.toLowerCase() === "oracle.xml"){
			mouseHoverOracle(d);
		} else 
			if (probName.toLowerCase() === "unknown acid problem"){
				mouseHoverUnknownAcid(d);
			} 

	}

	function mouseleave(d){
		
	}

	function update(source) {
		// Compute the new height, function counts total children of root node and sets tree height accordingly.
		// This prevents the layout looking squashed when new nodes are made visible or looking sparse when nodes are removed
		// This makes the layout more consistent.
		var levelWidth = [1];
		var childCount = function(level, n) {

			if (n.children && n.children.length > 0) {
				if (levelWidth.length <= level + 1) levelWidth.push(0);

				levelWidth[level + 1] += n.children.length;
				n.children.forEach(function(d) {
					childCount(level + 1, d);
				});
			}
		};
		childCount(0, root);
		var newHeight = d3.max(levelWidth) * 70; // 70 pixels per line  
		tree = tree.size([newHeight, viewerWidth]);

		// Compute the new tree layout.
		var nodes = tree.nodes(root).reverse(),
		links = tree.links(nodes);

		// Set widths between levels based on maxLabelLength.
		nodes.forEach(function(d) {
			d.y = (d.depth * (maxLabelLength * 10)); //maxLabelLength * 10px
			// alternatively to keep a fixed scale one can set a fixed depth per level
			// Normalize for fixed-depth by commenting out below line
			// d.y = (d.depth * 500); //500px per level.
		});

		// Update the nodes
		node = svgGroup.selectAll("g.node")
		.data(nodes, function(d) {
			return d.id || (d.id = ++i);
		});

		// Enter any new nodes at the parent's previous position.
		var nodeEnter = node.enter().append("g")
//		.call(dragListener)
		.attr("class", "node")
		.attr("transform", function(d) {
			return "translate(" + source.y0 + "," + source.x0 + ")";
		})
		.on('click', click)
		.on('mouseenter', mouseHover)
		.on('mouseleave', mouseleave);

		nodeEnter.append("circle")
		.attr('class', 'nodeCircle')
		.attr("r", 0)
		.style("fill", function(d) {
			return d._children ? "red" : "#fff";
		});

		nodeEnter.append("text") 
		.attr("x", function(d) {
			return d.children || d._children ? -10 : 10;
		})
		.attr("dy", ".35em")
		.attr('class', 'nodeText')
		.attr("text-anchor", function(d) {
			return d.children || d._children ? "end" : "start";
		})
		.text(function(d) {
			return d.name;
		})
		.style("fill-opacity", 0);

		// Update the text to reflect whether node has children or not.
		node.select('text')
		.attr("x", function(d) {
			return d.children || d._children ? -10 : -10;
		})
		.attr("y", function(d) {
			if (d.name == "root"){
				return 0;
			}
			return d.side == "first" ? -10 : 10;
		})
		.attr("text-anchor", function(d) {
			return d.children || d._children ? "end" : "end";
		})
		.text(function(d) {
			if (d.name=="root"){
				return "Solve Problem";
			}
			var nodeName = "";
			nodeName = getNodeName(d);

			return nodeName;
		});

		// Change the circle fill depending on whether it has children and is collapsed
		node.select("circle.nodeCircle")
		.attr("r", 4.5)
		.style("fill", function(d) {
			return d._children ? "red" : "#fff";
		});

		// Transition nodes to their new position.
		var nodeUpdate = node.transition()
		.duration(duration)
		.attr("transform", function(d) {
			return "translate(" + d.y + "," + d.x + ")";
		});

		// set the text with the right color and font
		nodeUpdate.select("text")
		.style("fill-opacity", 1);
		
		// if a node is end_point
		nodeUpdate.select("text")
		.style("font-weight", function(d){
			if (probName.toLowerCase() === "unknown acid problem"){
				if (d.end_point){
					return "bold";
				}
			}
		})
		.style("text-decoration", function(d){
			if (probName.toLowerCase() === "unknown acid problem"){
				if (d.end_point){
					return "underline";
				}
			}
		});
		nodeUpdate.select("text")
		.style("fill", function(d){
			if (probName.toLowerCase() === "oracle.xml"){
				if (d && d.information){
					switch(d.information.color){
					case "Green":
						return "green";
					case "Orange":
						return "orange";
					case "Red":
						return "red";

					default: // default color is black
						return "black";
					}
				}
			} else 
				if (probName.toLowerCase() === "unknown acid problem"){
					//the colors:
					var amount = d.ph;
					var color = "black";
					if (amount <= 3) {
						color = "red"; //red
					} else if (amount <= 6) {
						color = "orange"; //orange
					} else if (amount <= 8) {
						color = "green"; //green
					} else if (amount <= 11) {
						color = "blue"; //blue
					} else if (amount <= 14) {
						color = "purple"; //purple
					}
					return color;
				}

		});
		
		// Transition exiting nodes to the parent's new position.
		var nodeExit = node.exit().transition()
		.duration(duration)
		.attr("transform", function(d) {
			return "translate(" + source.y + "," + source.x + ")";
		})
		.remove();

		nodeExit.select("circle")
		.attr("r", 0);

		nodeExit.select("text")
		.style("fill-opacity", 0);

		// Update the links�
		var link = svgGroup.selectAll("path.link")
		.data(links, function(d) {
			return d.target.id;
		});

		// Enter any new links at the parent's previous position.
		link.enter().insert("path", "g")
		.attr("class", "link")
		.attr("d", function(d) {
			var o = {
					x: source.x0,
					y: source.y0
			};
			return diagonal({
				source: o,
				target: o
			});
		});

		// Transition links to their new position.
		link.transition()
		.duration(duration)
		.attr("d", diagonal);

		// Transition exiting nodes to the parent's new position.
		link.exit().transition()
		.duration(duration)
		.attr("d", function(d) {
			var o = {
					x: source.x,
					y: source.y
			};
			return diagonal({
				source: o,
				target: o
			});
		})
		.remove();

		// Stash the old positions for transition.
		nodes.forEach(function(d) {
			d.x0 = d.x;
			d.y0 = d.y;
		});
	}

	// Append a group which holds all nodes and which the zoom Listener can act upon.
	var svgGroup = baseSvg.append("g");

	// Define the root
	root = treeData;
	root.x0 = viewerHeight / 2;
	root.y0 = 0;


	// Layout the tree initially and left it on the root node.
	update(root);
	leftNode(root);
};

