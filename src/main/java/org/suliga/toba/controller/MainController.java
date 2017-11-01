package org.suliga.toba.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
	@GetMapping({ "/test", "/index", "/home", "/" })
	public String getTest(Model model) {
		return "index";
	}
}
