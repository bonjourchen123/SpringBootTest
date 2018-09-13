package com.core.services.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.core.dao.TestRepository;
import com.core.entity.Test;
import com.core.services.HomeServices;

@Service
public class HomeServicesImpl implements HomeServices {

	private static final Logger log = Logger.getLogger(HomeServicesImpl.class);
	
	@Autowired
	private TestRepository testRepository;
	
	public String sayHello() {
		log.info("HomeServicesImpl sayHello start");
		
		String result = "Hello Thymeleaf!";
		
		List<Test> testList = testRepository.findAll();
		if(testList != null && testList.size() != 0){
			StringBuffer tempString = new StringBuffer("Test:");
			for(Test test : testList){
				tempString.append(test.getTest());
				tempString.append(",");
				tempString.append(test.getMsg());
				tempString.append("; ");
			}
			
			result = tempString.toString();
		}
		
		log.info("HomeServicesImpl sayHello end");
		
        return result;
    }
}
