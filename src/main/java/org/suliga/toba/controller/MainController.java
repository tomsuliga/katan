package org.suliga.toba.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
	
	@GetMapping({ "/index", "/home", "/" })
	public String getSynonym(Model model) {
		return "redirect:toba";
	}
	
	@GetMapping({ "/toba" })
	public String getToba(Model model) {
		return "toba";
	}
}
