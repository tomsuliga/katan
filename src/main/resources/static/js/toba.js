'use strict';

var c;
var ctx;
var game;
var vertices;
var plots;
var roads;
var playerCards;
let separation = 80;
let xHexOffset = Math.sqrt(separation*separation - ((separation/2)*(separation/2)));
let resourceAlpha = 1.0;
let lastDiceTotal = 0;

var imgWheat = new Image();
imgWheat.src = "img/wheat.jpg";
var imgGrass = new Image();
imgGrass.src = "img/grass.jpg";
var imgRubbies = new Image();
imgRubbies.src = "img/rubies.jpg";
var imgCash = new Image();
imgCash.src = "img/cash.jpg";
var imgLava = new Image();
imgLava.src = "img/lava.jpg";

var audioRollDice = new Audio('sound/rollDice.wav');

function init() {
	var payload = JSON.stringify( { 'a':'b' } );
	stomp.send('/stomp/toba/getGame', {}, payload);
}

$(document).ready(function() {
	c = document.getElementById("canvasHex");
	ctx = c.getContext("2d");
	console.log("Window loaded and ready");
});

function drawBoard() {
   	drawPlots(); 					// Lines, ocean and resource plots
	drawRoads(); 					// Use black border and player colors
   	drawHarborPiers(); 				// Dotted piers
   	drawImprovements(); 			// Forts and castles
   	drawPlotCirclesAndNumbers(); 	// Plot dice numbers, harbor exchange rate, robber
   	drawDice(); 					// Two die
   	setPlayerStatusNumbers();
}

function drawRoads() {
	for (let i=0;i<roads.length;i++) {
		let road = roads[i];
		drawRoad(road);
	}
}

function drawRoad(road) {
	console.log("drawRoad " + road.player + ", " + road.fromCol + ", " + road.fromRow + ", " + road.toCol + ", " + road.toRow);
	drawLine(road.player, road.fromCol, road.fromRow, road.toCol, road.toRow);
}

function setPlayerStatusNumbers() {
	for (let i=0;i<4;i++) {
	   	if (game.numVictoryPoints[i] != 0) {
    		$('div#numVictoryPoints' + (i+1)).text(game.numVictoryPoints[i]);
    	}
    	if (game.numLongestRoad[i] != 0) {
    		$('div#numLongestRoad' + (i+1)).text(game.numLongestRoad[i]);
    	}
    	if (game.numForts[i] != 0) {
    		$('div#numForts' + (i+1)).text(game.numForts[i]);
    	}
    	if (game.numCastles[i] != 0) {
    		$('div#numCastles' + (i+1)).text(game.numCastles[i]);
    	}
		$('div#totalAllCards' + (i+1)).text(game.playerCardsTotal[i]);  
   	}
	
	for (let i=0;i<5;i++) {
		$('div#numResourceCards' + (i+1)).text(game.playerCards[0][i]);  
	}
}

function drawImprovements() {
	for (let row=0;row<16;row++) {
		for (let col=0;col<15;col++) {
			drawImprovement(col,row, vertices[col][row].improvement, vertices[col][row].player);
		}
	}	
}

function drawImprovement(col,row,improvement,player) {
	if (improvement == "NONE") {
		return;
	}
	
	let color = getPlayerColor(player);
	
	// Draw large circle first - if needed
	if (improvement == "CASTLE") {
		drawCircleAndBorder(col, row, 25, 3, color);
	}
	
	// Draw small circle by itself or on top of large circle
	if (improvement == "FORT" || improvement == "CASTLE") {
		drawCircleAndBorder(col, row, 16, 3, color);
	}
}

function drawCircleAndBorder(col, row, radius, width, color) {
	let xy = getXY(col,row);
	// Circle
	ctx.beginPath();
	ctx.arc(xy[0], xy[1], radius, 0, Math.PI*2, true); 
	ctx.closePath();
	ctx.fillStyle = color;
	ctx.fill();
	
	// Border
	ctx.lineWidth = width;
    ctx.strokeStyle = '#000';
    ctx.stroke();	      	
}

function getPlayerColor(player) {
	let color = "#bbb";
	
	if (player == "P1") {
		color = "#00f";
	} else if (player == "P2") {
		color = "#d00";
	} else if (player == "P3") {
		color = "#0d0";
	} else if (player == "P4") {
		color = "#dd0";
	}
	
	return color;
}

function drawLine(player, col, row, col2, row2) {
	let fromPoint = getXY(col,row);
	let toPoint = getXY(col2,row2);	
	ctx.beginPath();
	if (player == "NONE") {
		ctx.lineWidth = 2;
	    ctx.strokeStyle = '#000';
	} else {
		ctx.lineWidth = 16;
		ctx.strokeStyle = "#000";
	    ctx.moveTo(fromPoint[0], fromPoint[1]);
		ctx.lineTo(toPoint[0], toPoint[1]);
		ctx.stroke();
		ctx.lineWidth = 12;
	    ctx.strokeStyle = getPlayerColor(player);
	}
    ctx.moveTo(fromPoint[0], fromPoint[1]);
	ctx.lineTo(toPoint[0], toPoint[1]);
	ctx.stroke();
}

function drawPlots() {
	console.log("plotit called");
	let waterTurn = true;
	
	for (let j=0;j<2;j++) {
		if (j == 1) {
			waterTurn = false;
		}
		for (let i=0;i<plots.length;i++) {
			if (!waterTurn && plots[i].resource == "WATER") {
				continue;
			}
			if (waterTurn && plots[i].resource != "WATER") {
				continue;
			}

			let col = plots[i].col * 2;
			let row = plots[i].row * 2;
			
			let colOffset = 0;
			
			if (plots[i].row > 0 && plots[i].row%2 == 1) {
				colOffset = 1;
			}
			
			ctx.beginPath();
		
			if (plots[i].resource == "WATER") {
				ctx.lineWidth = 1;
			    ctx.strokeStyle = '#000';
			} else {
				ctx.lineWidth = 3;
			    ctx.strokeStyle = '#000';
			}
		    
			// Draw 6 lines for Hex
			let p = getXY(col+colOffset,row);
		    ctx.moveTo(p[0], p[1]);	
			p = getXY(col+1+colOffset,row+1);
		    ctx.lineTo(p[0], p[1]);
			p = getXY(col+1+colOffset,row+2);
		    ctx.lineTo(p[0], p[1]);
			p = getXY(col+colOffset,row+3);
		    ctx.lineTo(p[0], p[1]);
			p = getXY(col-1+colOffset,row+2);
		    ctx.lineTo(p[0], p[1]);
			p = getXY(col-1+colOffset,row+1);
		    ctx.lineTo(p[0], p[1]);
			p = getXY(col+colOffset,row);
		    ctx.lineTo(p[0], p[1]);
			ctx.stroke();

			// Fill in plots with image
			let robberOffsetY = 0;
			
			if (plots[i].resource == "WATER") {
				ctx.fillStyle = "#115";
			} else if (plots[i].resource == "ROBBER") {
				ctx.fillStyle = "#333";
				robberOffsetY = 32;
			} else if (plots[i].resource == "ONE") {
				ctx.fillStyle = ctx.createPattern(imgWheat, "repeat");
			} else if (plots[i].resource == "TWO") {
				ctx.fillStyle = ctx.createPattern(imgGrass, "repeat");
			} else if (plots[i].resource == "THREE") {
				ctx.fillStyle = ctx.createPattern(imgRubbies, "repeat");
			} else if (plots[i].resource == "FOUR") {
				ctx.fillStyle = ctx.createPattern(imgCash, "repeat");
			} else if (plots[i].resource == "FIVE") {
				ctx.fillStyle = ctx.createPattern(imgLava, "repeat");
			}
			
			if (plots[i].resource != "WATER" && plots[i].resource != "ROBBER") {
				ctx.globalAlpha = resourceAlpha;
			}
			ctx.fill();
			ctx.globalAlpha = 1.0;				
		}
	}
}

function drawHarborPiers() {
	for (let i=0;i<plots.length;i++) {
		const die = plots[i].die;

		// 1 == ocean, -1,-2,-3,-4,-5 == harbor exchange rates
		if (die == 0 || die >= 2) {
			continue;
		}
		
		const pCol = plots[i].col;
		const pRow = plots[i].row;
		let col = pCol * 2;
		let row = pRow * 2;
		let colOffset = 0;
		
		if (pRow > 0 && pRow%2 == 1) {
			colOffset = 1;
		}
		
		let p = getXY(col + colOffset, row);
			
		// Harbor pier road
		ctx.beginPath();
		ctx.lineWidth = 6;
	    ctx.strokeStyle = '#22a';
	    ctx.setLineDash([3,6]);

		if (pCol == 2 && pRow == 0) {
			drawPier(p, col + colOffset + 1, pRow + 2);
			drawPier(p, col + colOffset, pRow + 3);
		} else if (pCol == 4 && pRow == 0) {
			drawPier(p, col + colOffset - 1, pRow + 2);
			drawPier(p, col + colOffset, pRow + 3);
		} else if (pCol == 5 && pRow == 1) {
			drawPier(p, col + colOffset + 0, pRow + 4);
			drawPier(p, col + colOffset - 1, pRow + 3);
		} else if (pCol == 1 && pRow == 2) {
			drawPier(p, col + colOffset + 1, pRow + 3);
			drawPier(p, col + colOffset + 1, pRow + 4);
		} else if (pCol == 6 && pRow == 3) {
			drawPier(p, col + colOffset - 1, pRow + 4);
			drawPier(p, col + colOffset - 1, pRow + 5);
		} else if (pCol == 1 && pRow == 4) {
			drawPier(p, col + colOffset + 1, pRow + 5);
			drawPier(p, col + colOffset + 1, pRow + 6);
		} else if (pCol == 2 && pRow == 6) {
			drawPier(p, col + colOffset + 0, pRow + 6);
			drawPier(p, col + colOffset + 1, pRow + 7);
		} else if (pCol == 4 && pRow == 6) {
			drawPier(p, col + colOffset - 1, pRow + 7);
			drawPier(p, col + colOffset + 0, pRow + 6);
		} else if (pCol == 5 && pRow == 5) {
			drawPier(p, col + colOffset - 1, pRow + 6);
			drawPier(p, col + colOffset + 0, pRow + 5);
		}
		
		ctx.stroke();
		ctx.closePath();
	    ctx.setLineDash([0]);
	}
}

function drawPier(p, col, row) {
    ctx.moveTo(p[0], p[1] + separation);
    let p2 = getXY(col, row);
	ctx.lineTo(p2[0], p2[1]);	
}

function drawPlotCirclesAndNumbers() {
	for (let i=0;i<plots.length;i++) {
		const die = plots[i].die;

		if (die == 0) {
			continue;
		}
			
		drawSinglePlotCircleAndNumber(die, i, 1, "#000", 25);
	}
}

function drawSinglePlotCircleAndNumber(die, i, borderSize, borderColor, baseRadius) {
		let col = plots[i].col * 2;
		let row = plots[i].row * 2;			
		let colOffset = 0;
		
		if (plots[i].row > 0 && plots[i].row%2 == 1) {
			colOffset = 1;
		}
	
		let p = getXY(col+colOffset,row);
		
		let robberOffsetY = 0;
		
		if (die == 7) {
			robberOffsetY = 32;
		}

		// Circle container space
		let radius = baseRadius;
		// 1 == ocean, -1,-2,-3,-4,-5 == harbor exchange rates
		if (die <= 1) {
			radius = baseRadius + 5;
		}
		ctx.beginPath();
		ctx.arc(p[0], p[1] + (1.0 * separation) - robberOffsetY, radius, 0, Math.PI*2, true); 
		ctx.closePath();
		
		// Fill Circle
		if (die <= 1) {
			ctx.globalAlpha = resourceAlpha;
			ctx.fillStyle = "#00b";
			switch (die) {
				case -1: ctx.fillStyle = ctx.createPattern(imgWheat, "repeat"); break;
				case -2: ctx.fillStyle = ctx.createPattern(imgGrass, "repeat"); break;
				case -3: ctx.fillStyle = ctx.createPattern(imgRubbies, "repeat"); break;
				case -4: ctx.fillStyle = ctx.createPattern(imgCash, "repeat"); break;
				case -5: ctx.fillStyle = ctx.createPattern(imgLava, "repeat"); break;
			}
		} else if (die == 7) {
			if (borderColor == "#ff0") {
				borderSize--;
			}
			ctx.fillStyle = "#000";
		} else {
			//ctx.globalAlpha = 0.75;
			ctx.fillStyle = "#777";
		}		
		ctx.fill();
		ctx.globalAlpha = 1.0;
		
		// Circle border
		ctx.lineWidth = borderSize;
	    ctx.strokeStyle = borderColor;
	    ctx.stroke();
	
		// Number
		ctx.font = "26px Arial";
		ctx.fillStyle = "#000";
		let ch = die;
		let offsetX = 0;
		let dieOffset = 0;
		if (die == 1 || die < 0) {
			ctx.font = "32px Arial";
			ctx.fillStyle = "#000";
			offsetX = 10;
			if (die < 0) {
				ch = "2:1";
			} else if (die == 1) {
				ch = "3:1";
			}
		} else if (die == 7) {
			ctx.fillStyle = "#f00";
			ctx.font = "30px Arial";
			ch = "R";
		} else {
			dieOffset = 5;
			if (die == 6 || die == 8) {
				ctx.fillStyle = "#900";
				ctx.font = "bold 26px Arial";
			} else if (die >= 10) {
				offsetX = 5;
			}
		}
		ctx.fillText(ch, p[0] - 10 - offsetX, p[1] + separation + 10 - dieOffset - robberOffsetY);
		
		// Number Dots .....
		let dots = "";
		let dieOffsetX = 0;
		let dieOffsetY = 2;
		if (die == 6 || die == 8) {
			dots = ".....";
			dieOffsetX = 14;
		} else if (die == 5 || die == 9) {
			dots = "....";
			dieOffsetX = 11;
		} else if (die == 4 || die == 10) {
			dots = "...";
			dieOffsetX = 8;
		} else if (die == 3 || die == 11) {
			dots = "..";
			dieOffsetX = 5;
		} else if (die == 2 || die == 12) {
			dots = ".";
			dieOffsetX = 0;
		}
		if (dots != "") {
			ctx.font = "24px Arial";
			ctx.fillText(dots, p[0] - 4 - dieOffsetX, p[1] + separation + 10 + dieOffsetY);
		}
}

function getXY(col, row)
{
	let marginX = 10;
	let marginY = 10;
	
	let centerX = (col) * xHexOffset;
	let centerY = row * separation;
	
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

function drawDice() {
	let xy = getXY(7,8);
	let side = 45;
	
	if (game.die1 != 0) {
		drawDie(game.die1, side, xy[0] - 60, xy[1] - 45, "#b00", "#000");
	}
	if (game.die2 != 0) {
		drawDie(game.die2, side, xy[0] + 15, xy[1] - 45, "#000", "#b00");
	}
	
	// Highlight plot number(s)
	let total = game.die1 + game.die2;
	if (total != 0) {
		for (let i=0;i<plots.length;i++) {
			const die = plots[i].die;
			if (die == total) {
				drawSinglePlotCircleAndNumber(total, i, 5, "#ff0", 22);
			} else if (die == lastDiceTotal && lastDiceTotal != 0 && lastDiceTotal != total) {
				drawSinglePlotCircleAndNumber(lastDiceTotal, i, 1, "#555", 25);				
			}
		}
		lastDiceTotal = total;
	}
}

function drawDie(die, side, x, y, color1, color2) {
	ctx.roundRect(x, y, side, side, 10);
	ctx.fillStyle = color1;
	ctx.fill();
	
	if (die == 1 || die == 3 || die == 5) {
		drawPip(x+22, y+22, color2);
	}
	if (die == 2 || die == 3 || die == 4 || die == 5 || die == 6) {
		drawPip(x+11, y+10, color2);
		drawPip(x+33, y+34, color2);
	}
	if (die == 4 || die == 5 || die == 6) {
		drawPip(x+11, y+34, color2);
		drawPip(x+33, y+10, color2);
	}
	if (die == 6) {
		drawPip(x+11, y+22, color2);
		drawPip(x+33, y+22, color2);
	}
}

function drawPip(x, y, color) {
	const radius = 4;
	ctx.beginPath();
	ctx.arc(x, y, radius, 0, Math.PI*2, true); 
	ctx.closePath();
	ctx.fillStyle = color;
	ctx.fill();
}

CanvasRenderingContext2D.prototype.roundRect = function (x, y, w, h, r) {
	  if (w < 2 * r) r = w / 2;
	  if (h < 2 * r) r = h / 2;
	  this.beginPath();
	  this.moveTo(x+r, y);
	  this.arcTo(x+w, y,   x+w, y+h, r);
	  this.arcTo(x+w, y+h, x,   y+h, r);
	  this.arcTo(x,   y+h, x,   y,   r);
	  this.arcTo(x,   y,   x+w, y,   r);
	  this.closePath();
	  return this;
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
    	playerCards = game.playerCards;
    	console.log("roads=" + roads)
       	drawBoard();
   });

    stomp.subscribe('/topic/result/doNextStep', function (message) {
    	game = JSON.parse(message.body);
   	   	vertices = game.vertices;
       	plots = game.plots;
       	roads = game.roads;
       	playerCards = game.playerCards;
       	drawBoard();
   });
    
    stomp.subscribe('/topic/result/diceRolled', function (message) {
    	audioRollDice.play();
    	game = JSON.parse(message.body);
   		drawDice();
    });
    
    stomp.subscribe('/topic/result/newGame', function (message) {
    	game = JSON.parse(message.body);
    	lastDiceTotal = 0;
    	location.reload();
    });

    let sessionId = $('div#gameSpace').attr("data-sessionId");
	var payload = JSON.stringify( { 'sessionId':sessionId } );
	stomp.send('/stomp/toba/getGame', {}, payload);
});

$(document).on('click', '#btnStep', function() {
	let sessionId = $('div#gameSpace').attr("data-sessionId");
	var payload = JSON.stringify( { 'sessionId':sessionId } );
	stomp.send('/stomp/toba/getNextStep', {}, payload);
});

$(document).on('click', '#btnRollDice', function() {
	let sessionId = $('div#gameSpace').attr("data-sessionId");
	var payload = JSON.stringify( { 'sessionId':sessionId } );
	stomp.send('/stomp/toba/rollDice', {}, payload);
});

$(document).on('click', '#btnNewGame', function() {
	let sessionId = $('div#gameSpace').attr("data-sessionId");
	var payload = JSON.stringify( { 'sessionId':sessionId } );
	stomp.send('/stomp/toba/newGame', {}, payload);
});




