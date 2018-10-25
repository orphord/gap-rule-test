package com.orford.gapruletest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class GapRuleTestApplication {
	private final static Logger log = LoggerFactory.getLogger(GapRuleTestApplication.class);

	public static void main(String[] args) {
		log.info("Main() called.");
		System.setProperty("server.servlet.context-path", "/gaprule");
		SpringApplication.run(GapRuleTestApplication.class, args);
	}

}
