package com.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.core.services.HomeServices;

@Controller
public class HomeController {
	
	@Autowired
	private HomeServices homeServices;
	
	@RequestMapping("")
	public String index(Model model) {
		model.addAttribute("msg", homeServices.sayHello());
		return "index";
	}
	
	@RequestMapping("/test")
	public String test(@RequestParam(value="key", required=false, defaultValue="World!") String key, Model model){
		model.addAttribute("msg", "Hello " + key);
		// 若在不同層級回傳字串也需提供 path
		return "test/test";
	}
}
