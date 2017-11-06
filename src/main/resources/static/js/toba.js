/**
 * 
 */

var c;
var ctx;
var game;
var vertices;
var plots;
var roads;
let separation = 80;
let x = Math.sqrt(separation*separation - ((separation/2)*(separation/2)));


$(document).ready(function() {
	c = document.getElementById("canvasHex");
	ctx = c.getContext("2d");
	console.log("Window loaded and ready");
});

function oldCode() {
	var image5 = new Image();
	image5.src = "img/frank.jpg";
	image5.onload = init;
}

function drawBoard() {
	drawSpots();
	drawLines();
   	drawPlots();
	drawRoads();
   	drawImprovements();
}

function drawRoads() {
	for (let i=0;i<roads.length;i++) {
		let r = roads[i];
		console.log("r=" + r.owner + "," + r.fromVertex.col + "," + r.fromVertex.row + "," + r.toVertex.col + "," + r.toVertex.row);
		lineIt(r.owner, r.fromVertex.col, r.fromVertex.row, r.toVertex.col, r.toVertex.row);
	}
}

function drawSpots() {
	for (let row=0;row<16;row++) {
		for (let col=0;col<15;col++) {
			if (!vertices[col][row].hidden) {
				drawSpot(col,row);
			}
		}
	}	
}

function drawLines() {
	for (let row=0;row<16;row++) {
		for (let col=0;col<15;col++) {
			if (!vertices[col][row].hidden) {
				let id = vertices[col][row].id;
				let listAdjIds = vertices[col][row].adjVertices;
				if (listAdjIds.length > 0) {
					for (let i=0;i<listAdjIds.length;i++) {
						let id2 = listAdjIds[i];
						//console.log("from id " + id + " to id " + id2);
						let col2 = id2 % 15;
						let row2 = parseInt(id2 / 15, 10);
						//console.log(col2 + "," + row2);
						lineIt("NONE",col,row,col2,row2);
					}
				}
			}
		}
	}
}

function drawImprovements() {
	for (let row=0;row<16;row++) {
		for (let col=0;col<15;col++) {
			drawImprovement(col,row, vertices[col][row].improvement, vertices[col][row].owner);
		}
	}	
}

function drawImprovement(col,row,improvement,owner) {
	if (improvement == "NONE") {
		return;
	}
	
	let color = getOwnerColor(owner);
	
	// Draw large circle first - if needed
	if (improvement == "CITY") {
		let xy = getXY(col,row);
		// Circle
		let radius = 25;
		ctx.beginPath();
		ctx.arc(xy[0], xy[1], radius, 0, Math.PI*2, true); 
		ctx.closePath();
		ctx.fillStyle = color;
		ctx.fill();
		
		// Border
		ctx.lineWidth = 3;
	    ctx.strokeStyle = '#000';
	    ctx.stroke();	      
	}
	
	// Draw small circle by itself or on top of large circle
	if (improvement == "TOWN" || improvement == "CITY") {
		let xy = getXY(col,row);
		// Circle
		let radius = 15;
		ctx.beginPath();
		ctx.arc(xy[0], xy[1], radius, 0, Math.PI*2, true); 
		ctx.closePath();
		ctx.fillStyle = color;
		ctx.fill();
		
		// Border
		ctx.lineWidth = 3;
	    ctx.strokeStyle = '#000';
	    ctx.stroke();	      
	}
}

function getOwnerColor(owner) {
	let color = "#bbb";
	
	if (owner == "P1") {
		color = "#d00";
	} else if (owner == "P2") {
		color = "#0d0";
	} else if (owner == "P3") {
		color = "#00d";
	} else if (owner == "P4") {
		color = "#dd0";
	}
	
	return color;
}

function lineIt(owner, col, row, col2, row2) {
	let fromPoint = getXY(col,row);
	let toPoint = getXY(col2,row2);	
	ctx.beginPath();
	if (owner == "NONE") {
		ctx.lineWidth = 2;
	    ctx.strokeStyle = '#000';
	} else {
		ctx.lineWidth = 10;
	    ctx.strokeStyle = getOwnerColor(owner);
	}
    ctx.moveTo(fromPoint[0], fromPoint[1]);
	ctx.lineTo(toPoint[0], toPoint[1]);
	console.log("lineIt: " + separation + "," + x + ", " + fromPoint[0] + "," + fromPoint[1] + "," + toPoint[0] + "," + toPoint[1]);
	ctx.stroke();
}

function drawPlots() {
	console.log("plotit called");

	for (let i=0;i<plots.length;i++) {
		let col = plots[i].col * 2;
		let row = plots[i].row * 2;
		
		let colOffset = 0;
		
		if (plots[i].row > 0 && plots[i].row%2 == 1) {
			colOffset = 1;
		}
		
		ctx.beginPath();
	
		ctx.lineWidth = 1;
	    ctx.strokeStyle = '#000';
	    
	    let offset1 = 0; //8;
	    let offset2 = 0; //4;
	
		let p = getXY(col+colOffset,row);
	    ctx.moveTo(p[0], p[1] + offset1);
	
		p = getXY(col+1+colOffset,row+1);
	    ctx.lineTo(p[0] - offset1, p[1] + offset2);
	
		p = getXY(col+1+colOffset,row+2);
	    ctx.lineTo(p[0]- offset1, p[1] - offset2);
	
		p = getXY(col+colOffset,row+3);
	    ctx.lineTo(p[0], p[1] - offset1);
	
		p = getXY(col-1+colOffset,row+2);
	    ctx.lineTo(p[0] + offset1, p[1] - offset2);
	
		p = getXY(col-1+colOffset,row+1);
	    ctx.lineTo(p[0] + offset1, p[1] + offset2);
	
		p = getXY(col+colOffset,row);
	    ctx.lineTo(p[0], p[1] + offset1);
	   
		ctx.stroke();
		
		if (plots[i].resource == "WATER") {
			ctx.fillStyle = "#116";
		} else if (plots[i].resource == "ROBBER") {
			ctx.fillStyle = "#444";
		} else if (plots[i].resource == "ONE") {
			ctx.fillStyle = "#a44";
		} else if (plots[i].resource == "TWO") {
			ctx.fillStyle = "#4a4";
		} else if (plots[i].resource == "THREE") {
			ctx.fillStyle = "#44a";
		} else if (plots[i].resource == "FOUR") {
			ctx.fillStyle = "#a4a";
		} else if (plots[i].resource == "FIVE") {
			ctx.fillStyle = "#aa4";
		}
		
		ctx.fill();
		
		if (plots[i].die != 0) {
			const die = plots[i].die;
			p = getXY(col+colOffset,row);
			
			// Circle
			let radius = 25;
			ctx.beginPath();
			ctx.arc(p[0], p[1] + (1.0 * separation), radius, 0, Math.PI*2, true); 
			ctx.closePath();
			if (die == 7) {
				ctx.fillStyle = "#000";
			} else {
				ctx.globalAlpha = 0.75;
				ctx.fillStyle = "#777";
			}
			ctx.fill();
			ctx.globalAlpha = 1.0;
			
			// Circle border
			ctx.lineWidth = 1;
		    ctx.strokeStyle = '#000';
		    ctx.stroke();

			// Number
			ctx.font = "26px Arial";
			ctx.fillStyle = "#000";
			let ch = die;

			if (die == 7) {
				ctx.fillStyle = "#f00";
				ctx.font = "30px Arial";
				ch = "R";
			} else if (die == 6 || die == 8) {
				ctx.fillStyle = "#b00";
				ctx.font = "bold 30px Arial";
			} else if (die == 2 || die == 12) {
				ctx.font = "16px Arial";
			} else if (die == 3 || die == 11) {
				ctx.font = "20px Arial";
			}
			ctx.fillText(ch, p[0] - 10, p[1] + separation + 10);
		}
	}
}

function drawSpot(col, row) {
	let xy = getXY(col, row);
	
	// Circle
	let radius = 1;
	ctx.beginPath();
	ctx.arc(xy[0], xy[1], radius, 0, Math.PI*2, true); 
	ctx.closePath();
	ctx.fillStyle = "#000";
	ctx.fill();
}

function getXY(col, row)
{
	let marginX = 400;
	let marginY = 100;
	
	let centerX = (col) * x;
	let centerY = row * separation;
	
	//console.log(separation + "," + x);

	switch (row) {
		case 0: 
			centerY = 0;
			break;
		case 1:
			centerY = (row-1) * separation + (separation / 2);
			break;
		case 2:
			centerY = (row-0) * separation - (separation / 2);
			break;
		case 3:
		case 4:
			centerY = (row-1) * separation;
			break;
		case 5:
		case 6:
			centerY = (row-1) * separation - (separation / 2);
			break;
		case 7:
		case 8:
			centerY = (row-2) * separation;
			break;
		case 9:
		case 10:
			centerY = (row-2) * separation - (separation / 2);
			break;
		case 11:
		case 12:
			centerY = (row-3) * separation;
			break;
		case 13:
		case 14:
			centerY = (row-3) * separation - (separation / 2);
			break;
		case 15:
			centerY = (row-4) * separation;
			break;
	}
	
	const finalX = marginX + centerX;
	const finalY = marginY + centerY;
		
	return [finalX, finalY];
}

////// STOMP ////////
var stompUrl = 'http://' + window.location.host + '/toba';
var stompSock = new SockJS(stompUrl);
var stomp = Stomp.over(stompSock);

stomp.connect({}, function(frame) {
    stomp.subscribe('/topic/result/getGame', function (message) {
    	game = JSON.parse(message.body);
    	vertices = game.vertices;
    	plots = game.plots;
    	roads = game.roads;
       	drawBoard();
   });
    
	var payload = JSON.stringify( { 'a':'b' } );
	stomp.send('/stomp/toba/getGame', {}, payload);
});




