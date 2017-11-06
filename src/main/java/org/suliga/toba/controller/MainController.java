package org.suliga.toba.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.suliga.toba.model.Game;
import org.suliga.toba.model.Vertex;

@Controller
public class MainController {
	
	@Autowired private SimpMessagingTemplate simpMessagingTemplate;

	@GetMapping({ "/index", "/home", "/" })
	public String getSynonym(Model model) {
		return "redirect:toba";
	}
	
	@GetMapping({ "/toba" })
	public String getToba(Model model) {
		Game game = new Game();
		model.addAttribute("vertices", game.getVertices());
		model.addAttribute("adjMap", game.getAdjMap());
		return "toba";
	}
	
	@MessageMapping("/toba/getGame")
	public void handleGetGame() {
		//logger.info("Received stomp msg: " + incoming);
		Game game = new Game();
		simpMessagingTemplate.convertAndSend("/topic/result/getGame", game);
	}
}
