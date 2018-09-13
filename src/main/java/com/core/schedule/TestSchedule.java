package com.core.schedule;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.core.services.HomeServices;

/**
 * 
 * @author Bonjour
 *
 */
@Component
public class TestSchedule {
	private static final Logger log = Logger.getLogger(TestSchedule.class);
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
	
	@Autowired
	private HomeServices homeServices;
	
	/**
	 * 定時檢查是否需發送簡訊
	 */
	@Scheduled(cron = "${scheduled.test.cron}")
	public void test(){
		log.info("TestSchedule test start :" + dateFormat.format(new Date()));
		
		String testString = homeServices.sayHello();
		log.info(testString);
		
		log.info("TestSchedule test end :" + dateFormat.format(new Date()));
	}
}
