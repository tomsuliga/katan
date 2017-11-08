package org.suliga.toba.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
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
		if (game.getNumTurns() == 0) {
			Road road = new Road(Owner.P1, game.getAdjMap().get(66), game.getAdjMap().get(51));
			game.addRoad(road);
			tm.setRoad1(road);
			Vertex vertex1 = game.getAdjMap().get(66);
			vertex1.setImprovement(Improvement.TOWN);
			vertex1.setOwner(Owner.P1);
			tm.setVertex1(vertex1);
			game.endTurn();
		} else if (game.getNumTurns() == 1) {
			Road road = new Road(Owner.P2, game.getAdjMap().get(109), game.getAdjMap().get(124));
			game.addRoad(road);
			tm.setRoad1(road);
			Vertex vertex1 = game.getAdjMap().get(109);
			vertex1.setImprovement(Improvement.TOWN);
			vertex1.setOwner(Owner.P2);
			tm.setVertex1(vertex1);
			game.endTurn();
		} else if (game.getNumTurns() == 2) {
			Road road = new Road(Owner.P3, game.getAdjMap().get(157), game.getAdjMap().get(171));
			game.addRoad(road);
			tm.setRoad1(road);
			Vertex vertex1 = game.getAdjMap().get(157);
			vertex1.setImprovement(Improvement.TOWN);
			vertex1.setOwner(Owner.P3);
			tm.setVertex1(vertex1);
			game.endTurn();
		} else if (game.getNumTurns() == 3) {
			Road road = new Road(Owner.P4, game.getAdjMap().get(99), game.getAdjMap().get(84));
			game.addRoad(road);
			tm.setRoad1(road);
			Vertex vertex1 = game.getAdjMap().get(99);
			vertex1.setImprovement(Improvement.TOWN);
			vertex1.setOwner(Owner.P4);
			tm.setVertex1(vertex1);
			game.endTurn();
		} else if (game.getNumTurns() == 4) {
			Road road = new Road(Owner.P4, game.getAdjMap().get(144), game.getAdjMap().get(130));
			game.addRoad(road);
			tm.setRoad1(road);
			Vertex vertex1 = game.getAdjMap().get(144);
			vertex1.setImprovement(Improvement.TOWN);
			vertex1.setOwner(Owner.P4);
			tm.setVertex1(vertex1);
			game.endTurn();
		} else if (game.getNumTurns() == 5) {
			Road road = new Road(Owner.P3, game.getAdjMap().get(155), game.getAdjMap().get(171));
			game.addRoad(road);
			tm.setRoad1(road);
			Vertex vertex1 = game.getAdjMap().get(155);
			vertex1.setImprovement(Improvement.TOWN);
			vertex1.setOwner(Owner.P3);
			tm.setVertex1(vertex1);
			game.endTurn();
		} else if (game.getNumTurns() == 6) {
			Road road = new Road(Owner.P2, game.getAdjMap().get(111), game.getAdjMap().get(95));
			game.addRoad(road);
			tm.setRoad1(road);
			Vertex vertex1 = game.getAdjMap().get(111);
			vertex1.setImprovement(Improvement.TOWN);
			vertex1.setOwner(Owner.P2);
			tm.setVertex1(vertex1);
			game.endTurn();
		} else if (game.getNumTurns() == 7) {
			Road road = new Road(Owner.P1, game.getAdjMap().get(68), game.getAdjMap().get(84));
			game.addRoad(road);
			tm.setRoad1(road);
			Vertex vertex1 = game.getAdjMap().get(68);
			vertex1.setImprovement(Improvement.TOWN);
			vertex1.setOwner(Owner.P1);
			tm.setVertex1(vertex1);
			game.endTurn();
		} else if (game.getNumTurns() == 8) {
			tm.rollDice();
		}
		
		tm.setNumTurns(game.getNumTurns());
		simpMessagingTemplate.convertAndSend("/topic/result/doNextStep", tm);
	}
}
