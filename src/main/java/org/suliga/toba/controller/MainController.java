package org.suliga.toba.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.suliga.toba.model.Game;

@Controller
public class MainController {
	private static final Logger logger = LoggerFactory.getLogger(MainController.class);
	
	@Autowired private SimpMessagingTemplate simpMessagingTemplate;
	
	private Map<String, Game> games = new HashMap<>();

	@GetMapping({ "/index", "/home", "/" })
	public String getSynonym(Model model) {
		return "redirect:toba";
	}
	
	@GetMapping({ "/toba" })
	public String getToba(Model model, HttpServletRequest req) {
		String sessionId = req.getSession().getId();
		Game game = new Game(sessionId);
		games.put(sessionId, game);
		model.addAttribute("vertices", game.getVertices());
		model.addAttribute("adjMap", game.getAdjMap());
		model.addAttribute("sessionId", sessionId);
		return "toba";
	}
	
	@MessageMapping("/toba/getGame")
	public void handleGetGame(String sessionId) {
		//logger.info("sessionId = " + sessionId);
		Game game = games.get(sessionId);
		if (game == null) {
			game = new Game(sessionId);
			games.put(sessionId, game);
		}
		simpMessagingTemplate.convertAndSend("/topic/result/getGame", game);
	}
	
	@MessageMapping("/toba/getNextStep")
	public void handleGetNextStep(String sessionId) {
		//logger.info("sessionId = " + sessionId);
		Game game =  games.get(sessionId);
		
		// temp - first 8 moves
		if (game.getCurrentTurn() > 8) {
			return;
		}
		
		game.doBestPlacementMove();
		game.endTurn();
		simpMessagingTemplate.convertAndSend("/topic/result/doNextStep", game);
		
/*
		class Moves {
			Player player;
			Improvement improvement;
			int v1;
			int v2;
			Moves(Player player, Improvement improvement, int v1, int v2) {
				this.player = player;
				this.improvement = improvement;
				this.v1 = v1;
				this.v2 = v2;
			}
		}
		Moves[] allMoves = new Moves[8];
		allMoves[0] = new Moves(Player.P1, Improvement.FORT, 51, 66);
		allMoves[1] = new Moves(Player.P2, Improvement.FORT, 109, 124);
		allMoves[2] = new Moves(Player.P3, Improvement.FORT, 157, 171);
		allMoves[3] = new Moves(Player.P4, Improvement.FORT, 99, 84);
		allMoves[4] = new Moves(Player.P4, Improvement.FORT, 144, 130);
		allMoves[5] = new Moves(Player.P3, Improvement.FORT, 155, 171);
		allMoves[6] = new Moves(Player.P2, Improvement.FORT, 111, 95);
		allMoves[7] = new Moves(Player.P1, Improvement.CASTLE, 68, 84);
		
		// Different each time called 0 - 7
		Moves thisMove = allMoves[game.getNumTurns()];
		
		Vertex vertex1 = game.getAdjMap().get(thisMove.v1);
		Vertex vertex2 = game.getAdjMap().get(thisMove.v2);
		vertex1.setImprovement(thisMove.improvement);
		vertex1.setPlayer(thisMove.player);
		Road road = new Road(thisMove.player, vertex1, vertex2);
		vertex1.addRoad(road);
		vertex2.addRoad(road);
		game.addRoad(road);
		game.setRoad1(road);
		game.setVertex1(vertex1);
		if (thisMove.improvement == Improvement.CASTLE) {
			game.addNumCastles(thisMove.player.ordinal()-1);
		} else {
			game.addNumForts(thisMove.player.ordinal()-1);
		}
		// Give player resource cards for second turn
		if (game.getNumTurns() > 3) {
			logger.info("Time to get cards for: " + thisMove.player);
			game.handOutResourcesForVertex(vertex1);
		}
		game.endTurn();
		
		simpMessagingTemplate.convertAndSend("/topic/result/doNextStep", game);		

		// temp longest road
		if (game.getNumTurns() == 1) {
			int[][] roads = { {66,82}, {82,97}, {97,111}, {111,126} };
			for (int i=0;i<roads.length;i++) {
				int n1 = roads[i][0];
				int n2 = roads[i][1];
				Vertex v1 = game.getAdjMap().get(n1);
				Vertex v2 = game.getAdjMap().get(n2);
				Road r = new Road(Player.P1, v1, v2);
				v1.addRoad(r);
				v2.addRoad(r);
				game.addRoad(r);
				game.setRoad1(r);
				simpMessagingTemplate.convertAndSend("/topic/result/doNextStep", game);
			}
		}
*/				
	}
	
	@MessageMapping("/toba/rollDice")
	public void handleRollDice(String sessionId) {
		//logger.info("sessionId = " + sessionId);
		Game game =  games.get(sessionId);
		game.rollDice();
		simpMessagingTemplate.convertAndSend("/topic/result/diceRolled", game);
	}

	@MessageMapping("/toba/newGame")
	public void handleNewGame(String sessionId) {
		//logger.info("sessionId = " + sessionId);
		Game game =  games.get(sessionId);
		game.init();
		simpMessagingTemplate.convertAndSend("/topic/result/newGame", game);
	}
}
