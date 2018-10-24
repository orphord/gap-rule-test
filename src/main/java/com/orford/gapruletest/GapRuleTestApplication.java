package com.orford.gapruletest;

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

import com.orford.gapruletest.controller.FileDataController;


@SpringBootApplication
public class GapRuleTestApplication implements ApplicationRunner {
	private final static Logger log = LoggerFactory.getLogger(GapRuleTestApplication.class);
	
	public final static String FILE_CMD_LINE_ARG = "file.loc";
	
	@Autowired
	FileDataController fileDataController;
	
	public static void main(String[] args) {
		log.info("Main() called.");
		SpringApplication.run(GapRuleTestApplication.class, args);
	}

	/**
	 * Override for ApplicationRunner implementation.  Used to conveniently handle file-based Json
	 * data. 
	 */
	@Override
	public void run(ApplicationArguments args) throws Exception {
		log.info("Run() function called.");

		Map<String, Object> cmdLineArgs = parseCmdLineArgs(args);

		fileDataController.loadData((String)cmdLineArgs.get(FILE_CMD_LINE_ARG));
		log.info("About to call perforSearch()");
		fileDataController.performSearch();

	}
	
	/**
	 * Function to parse command line args and create a Map of them for simpler processing.
	 * 
	 * @param _args
	 * @return
	 */
	private Map<String, Object> parseCmdLineArgs(ApplicationArguments _args) {
		// Initialize map to nulls for each potential command line arg
		Map<String, Object> argsMap = new HashMap<String, Object>();
		argsMap.put(FILE_CMD_LINE_ARG, null);

		// Set filePath if exists
		List<String> filePathArgList = _args.getOptionValues(FILE_CMD_LINE_ARG);
		if(filePathArgList != null && !filePathArgList.isEmpty()) {
			argsMap.put(FILE_CMD_LINE_ARG, filePathArgList.get(0));
		}

		return argsMap;
	}
}
