package com.core.services.impl;

import org.springframework.stereotype.Service;

import com.core.services.HomeServices;

@Service
public class HomeServicesImpl implements HomeServices {

	public String sayHello() {
        return "Hello Thymeleaf";
    }
}
