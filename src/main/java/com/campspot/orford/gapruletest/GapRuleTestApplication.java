package com.campspot.orford.gapruletest;


import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;

import com.campspot.orford.gapruletest.controller.FileDataController;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootApplication
public class GapRuleTestApplication implements ApplicationRunner {
	private final static Logger log = LoggerFactory.getLogger(GapRuleTestApplication.class);
	
	public final static String FILE_CMD_LINE_ARG = "file.loc";
	public final static String GAP_DAYS_CMD_LINE_ARG = "default.gap";
	
	@Autowired
	FileDataController fileDataController;
	
	public static void main(String[] args) {
		log.info("Main() called.");
		SpringApplication.run(GapRuleTestApplication.class, args);
	}
	
	@Override
	public void run(ApplicationArguments args) throws Exception {
		log.info("Run() function called.");

		Map<String, Object> cmdLineArgs = parseCmdLineArgs(args);
		
		fileDataController.loadData((Integer)cmdLineArgs.get(GAP_DAYS_CMD_LINE_ARG),
																(String)cmdLineArgs.get(FILE_CMD_LINE_ARG));
		
		ClassPathResource jsonCPresource = new ClassPathResource("test-case.json");
		log.info("Length of json file: " + jsonCPresource.contentLength());
		InputStream isStream = jsonCPresource.getInputStream();
		JsonNode rootNode = new ObjectMapper().readTree(isStream);
		
		JsonNode searchNode = rootNode.get("search");
		log.info("search: " + (searchNode == null));
		log.info("startDate: " + searchNode.get("startDate").asText());
		
	}
	
	private Map<String, Object> parseCmdLineArgs(ApplicationArguments _args) {
		// Initialize map to nulls for each potential command line arg
		Map<String, Object> argsMap = new HashMap<String, Object>();
		argsMap.put(FILE_CMD_LINE_ARG, null);
		argsMap.put(GAP_DAYS_CMD_LINE_ARG, null);

		// Set filePath if exists
		List<String> filePathArgList = _args.getOptionValues(FILE_CMD_LINE_ARG);
		if(filePathArgList != null && !filePathArgList.isEmpty()) {
			argsMap.put(FILE_CMD_LINE_ARG, filePathArgList.get(0));
		}

		// Set gap value if exists
		List<String> gapArgStrList = _args.getOptionValues(GAP_DAYS_CMD_LINE_ARG);
		if(gapArgStrList != null && !gapArgStrList.isEmpty()) {
			String gapStr = gapArgStrList.get(0);
			Integer gapDays = Integer.valueOf(gapStr);
			argsMap.put(GAP_DAYS_CMD_LINE_ARG, gapDays);
		}
	
		return argsMap;
	}
}
