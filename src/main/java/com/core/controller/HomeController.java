package com.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.core.services.HomeServices;

@Controller
public class HomeController {
	
	@Autowired
	private HomeServices homeServices;
	
	@RequestMapping("/")
	public String index(Model model) {
		model.addAttribute("msg", homeServices.sayHello());
		return "index";
	}
	
}
