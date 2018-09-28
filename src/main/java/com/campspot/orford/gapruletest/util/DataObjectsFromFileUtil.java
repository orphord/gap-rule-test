package com.campspot.orford.gapruletest.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.campspot.orford.gapruletest.exception.GapRuleException;
import com.campspot.orford.gapruletest.model.Campsite;
import com.campspot.orford.gapruletest.model.CampsiteSearch;
import com.campspot.orford.gapruletest.model.Reservation;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class DataObjectsFromFileUtil {
	private static Logger log = LoggerFactory.getLogger(DataObjectsFromFileUtil.class);
	private final static String searchNodeIdentifier = "search";
	private final static String reservationNodeIdentifier = "reservations";
	private final static String campsiteNodeIdentifier = "campsites";

	@Value("${default.json.file}")
	private String testJsonFileLoc;

	private JsonNode rootNode = null;

	/**
	 * Function will return a List of Reservation objects
	 * @param _fileLoc
	 * @return
	 * @throws GapRuleException
	 */
	public List<Reservation> getReservationListFromFile(String _fileLoc) throws GapRuleException {
		List<Reservation> reservationList = new ArrayList<Reservation>();
		initRootNode(_fileLoc);
		JsonNodeToObjectUtil jsonObjectUtil = new JsonNodeToObjectUtil();

		JsonNode reservationNode = rootNode.get(reservationNodeIdentifier);
		reservationList = jsonObjectUtil.getReservations(reservationNode);

		return reservationList;
		
	}

	/**
	 * Function will return a List of Reservation objects
	 * @param _fileLoc
	 * @return
	 * @throws GapRuleException
	 */
	public List<Campsite> getCampsiteListFromFile(String _fileLoc) throws GapRuleException {
		List<Campsite> campsiteList = new ArrayList<Campsite>();
		initRootNode(_fileLoc);
		JsonNodeToObjectUtil jsonObjectUtil = new JsonNodeToObjectUtil();

		JsonNode campsiteNode = rootNode.get(campsiteNodeIdentifier);
		campsiteList = jsonObjectUtil.getCampsites(campsiteNode);

		return campsiteList;
		
	}
	

	/**
	 * Function will return a CampsiteSearch object
	 * @param _optFilePath
	 * @return
	 */
	public CampsiteSearch getSearchFromFile(String _fileLoc) throws GapRuleException {
		CampsiteSearch search = new CampsiteSearch();
		initRootNode(_fileLoc);
		JsonNodeToObjectUtil jsonObjectUtil = new JsonNodeToObjectUtil();
		
		JsonNode searchNode = rootNode.get(searchNodeIdentifier);
		search = jsonObjectUtil.getCampsiteSearch(searchNode);
		
		return search;
	}
	
	private void initRootNode(String _fileLoc) throws GapRuleException {
		
		// If rootNode has already been initialized ==> do nothing
		if(rootNode == null) {
			InputStream jsonIS = null;
			
			// 1. Check if _fileLoc is null, if null ==> use the test json file
			if(_fileLoc == null) {
				try {
					jsonIS = useTestFile();
				} catch (IOException ex) {
					String errMsg = "An IOException was thrown attempting to transform input stream to root JsonNode from test-case.json.";
					log.error(errMsg);
					throw new GapRuleException(errMsg);
				}
			} else {
				// 2. Try to get data Json from file
				File tempFileObj = new File(_fileLoc);
				
				if(tempFileObj.exists()) {
					try {
						jsonIS = new FileInputStream(_fileLoc);
					} catch(FileNotFoundException ex) {
						log.error("I checked that it exists, this should seriously not happen.");
						throw new GapRuleException("File " + _fileLoc + " doesn't exist, but I checked so WTH!");
					}
				} else { // 3. if file doesn't exist --> use test-case.json file in jar
					try {
						jsonIS = useTestFile();
					} catch (IOException ex) {
						String errMsg = "An IOException was thrown attempting to transform input stream to root JsonNode from test-case.json.";
						log.error(errMsg);
						throw new GapRuleException(errMsg);
					}
				}

			}

			// Now set rootNode to be root of Json file.
			if(jsonIS != null) {
				try {
					rootNode = new ObjectMapper().readTree(jsonIS);
				} catch (IOException ex) {
					String errMsg = "An IOException was thrown attempting to transform input stream to root JsonNode.";
					log.error(errMsg);
					throw new GapRuleException(errMsg);
				}
			}
		}
	}
	
	private InputStream useTestFile() throws IOException {
		ClassPathResource jsonCPresource = new ClassPathResource("test-case.json");
		InputStream jsonIS = null;

		log.info("Json file from property: " + testJsonFileLoc);
		jsonIS = jsonCPresource.getInputStream();
	
		return jsonIS;
	}

}
