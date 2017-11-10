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
import org.suliga.toba.model.Improvement;
import org.suliga.toba.model.Owner;
import org.suliga.toba.model.Road;
import org.suliga.toba.model.TobaMessage;
import org.suliga.toba.model.Vertex;

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
		logger.info("sessionId = " + sessionId);
		Game game = games.get(sessionId);
		if (game == null) {
			game = new Game(sessionId);
			games.put(sessionId, game);
		}
		simpMessagingTemplate.convertAndSend("/topic/result/getGame", game);
	}
	
	@MessageMapping("/toba/getNextStep")
	public void handleGetNextStep(String sessionId) {
		logger.info("sessionId = " + sessionId);
		TobaMessage tm = new TobaMessage();
		Game game =  games.get(sessionId);
		
		// temp - first 8 moves
		if (game.getNumTurns() >= 8) {
			return;
		}
		
		class Moves {
			Owner owner;
			Improvement improvement;
			int v1;
			int v2;
			Moves(Owner owner, Improvement improvement, int v1, int v2) {
				this.owner = owner;
				this.improvement = improvement;
				this.v1 = v1;
				this.v2 = v2;
			}
		}
		Moves[] allMoves = new Moves[8];
		allMoves[0] = new Moves(Owner.P1, Improvement.FORT, 66, 51);
		allMoves[1] = new Moves(Owner.P2, Improvement.FORT, 109, 124);
		allMoves[2] = new Moves(Owner.P3, Improvement.FORT, 157, 171);
		allMoves[3] = new Moves(Owner.P4, Improvement.FORT, 99, 84);
		allMoves[4] = new Moves(Owner.P4, Improvement.FORT, 144, 130);
		allMoves[5] = new Moves(Owner.P3, Improvement.FORT, 155, 171);
		allMoves[6] = new Moves(Owner.P2, Improvement.FORT, 111, 95);
		allMoves[7] = new Moves(Owner.P1, Improvement.CASTLE, 68, 84);
		
		Moves thisMove = allMoves[game.getNumTurns()];
		
		Vertex vertex1 = game.getAdjMap().get(thisMove.v1);
		Vertex vertex2 = game.getAdjMap().get(thisMove.v2);
		vertex1.setImprovement(thisMove.improvement);
		vertex1.setOwner(thisMove.owner);
		Road road = new Road(thisMove.owner, vertex1, vertex2);
		vertex1.addRoad(road);
		vertex2.addRoad(road);
		game.addRoad(road);
		tm.setRoad1(road);
		tm.setVertex1(vertex1);
		game.addNumForts(thisMove.owner.ordinal()-1);
		game.endTurn();
		game.setTobaMessage(tm);
		
		simpMessagingTemplate.convertAndSend("/topic/result/doNextStep", game);
	}
	
	@MessageMapping("/toba/rollDice")
	public void handleRollDice(String sessionId) {
		logger.info("sessionId = " + sessionId);
		TobaMessage tm = new TobaMessage();
		tm.rollDice();
		Game game =  games.get(sessionId);
		game.setTobaMessage(tm);
		simpMessagingTemplate.convertAndSend("/topic/result/diceRolled", game);
	}
}
