package com.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.core.services.HomeServices;

@RestController
public class HomeController {
	
	@Autowired
	private HomeServices homeServices;
	
	@RequestMapping("/index")
	public String index() {
		return homeServices.sayHello();
	}
	
}
