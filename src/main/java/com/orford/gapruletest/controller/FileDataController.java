package com.orford.gapruletest.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

import com.orford.gapruletest.exception.GapRuleException;
import com.orford.gapruletest.model.Campsite;
import com.orford.gapruletest.model.CampsiteSearch;
import com.orford.gapruletest.model.Reservation;
import com.orford.gapruletest.service.CampsiteSearchService;
import com.orford.gapruletest.service.CampsiteService;
import com.orford.gapruletest.service.ReservationService;
import com.orford.gapruletest.util.DataObjectsFromFileUtil;


@Controller
public class FileDataController {
	private static Logger log = LoggerFactory.getLogger(FileDataController.class);

	@Autowired
	ReservationService resService;
	@Autowired
	CampsiteService siteService;
	@Autowired
	CampsiteSearchService searchService;
	
	CampsiteSearch searchRequest;

	/**
	 * loadData function gets optional parameters _optGapDays and _optFilePath parameters
	 * representing the command-line arguments which may be passed in indicating:
	 * 1) The operational gap in days that will be used to make the decision as to whether
	 * 		to return sites for a particular search request.
	 * 2) The path on the local filesystem to a json file (*assumed to be valid for this request*) 
	 * @param _optGapDays - gap in days
	 * @param _optFilePath - path on the local filesystem to a json file
	 *	(*assumed to be valid for this request*)
	 */
	public void loadData(String _optFilePath) {
		log.debug("File path passed in: " + _optFilePath);
		try {

			// Acquire data from file and set to appropriate services
			DataObjectsFromFileUtil dataFromFile = new DataObjectsFromFileUtil();
			List<Reservation> reservationList = dataFromFile.getReservationListFromFile(_optFilePath);
			List<Campsite> campsiteList = dataFromFile.getCampsiteListFromFile(_optFilePath);
			searchRequest = dataFromFile.getSearchFromFile(_optFilePath);

			resService.setAllReservations(reservationList);
			siteService.setAllCampsites(campsiteList);
			
			// Call CampsiteSearchService initialize procedure to process data for search
			searchService.initialize();

		} catch(GapRuleException ex) {
			log.error("-------------------------------");
			log.error("A GapRuleException was thrown with the following message:");
			log.error(ex.getMessage());
			log.error("--------------------------------");
		}
	}

	public void performSearch() {
		log.info("performSearch() called");
		List<Campsite> availableSites = searchService.performSearch(searchRequest);
		log.info("==============================================");
		log.info("Search Criteria:");
		log.info("\tStart Date: " + searchRequest.getStartDate());
		log.info("\tEnd Date: " + searchRequest.getEndDate());
		log.info("Sites Available for search criteria: ");
		for(Campsite site : availableSites) {
			log.info(site.getName());
		}
		log.info("==============================================");
	}

}
