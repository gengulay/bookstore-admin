package com.adminportal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

	@RequestMapping("/")
	public String getRoot() {
		return "redirect:/home";
	}

	@RequestMapping("/home")
	public String getHome() {
		return "home";
	}

	@RequestMapping("/login")
	public String getLogin() {
		return "login";
	}

}
