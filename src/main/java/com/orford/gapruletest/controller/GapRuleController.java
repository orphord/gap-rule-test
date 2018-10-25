package com.orford.gapruletest.controller;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.orford.gapruletest.exception.GapRuleException;
import com.orford.gapruletest.model.Site;
import com.orford.gapruletest.model.SiteSearch;
import com.orford.gapruletest.model.Reservation;
import com.orford.gapruletest.service.SiteSearchService;
import com.orford.gapruletest.service.SiteService;
import com.orford.gapruletest.service.ReservationService;
import com.orford.gapruletest.util.DataObjectsFromFileUtil;


@RestController
public class GapRuleController {
	private static Logger log = LoggerFactory.getLogger(GapRuleController.class);

	ReservationService resService;
	SiteService locationService;
	SiteSearchService searchService;

	@Autowired
	public GapRuleController(ReservationService _resService, SiteService _locService, SiteSearchService _searchService) {
		resService = _resService;
		locationService = _locService;
		searchService = _searchService;
		this.loadData(null);
	}

	/**
	 * Method to take in a requested reservation start date and end date and return a list of Site objects that pass
	 * the defined gap-rule criteria (1 day gap by default).
	 * @param _startDate
	 * @param _endDate
	 * @return
	 */
	@RequestMapping(value="/search", method=RequestMethod.GET)
	public List<Site> findAvailableLocationsGivenGapRule(@RequestParam(value="startDate") String _startDate,
																											 @RequestParam(value="endDate") String _endDate) {
		LocalDate startDate = LocalDate.parse(_startDate);
		LocalDate endDate = LocalDate.parse(_endDate);
		SiteSearch searchReq = new SiteSearch(startDate, endDate);
		List<Site> resultList = searchService.performSearch(searchReq);

		return resultList;
	}


	/**
	 * THIS IS A HACK in order to make the odd data model load at startup time
	 * The loadData function gets optional parameters _optGapDays and _optFilePath parameters
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
			List<Site> siteList = dataFromFile.getSiteListFromFile(_optFilePath);

			resService.setAllReservations(reservationList);
			locationService.setAllSites(siteList);
			
			// Call SiteSearchService initialize procedure to process data for search
			searchService.initialize();

		} catch(GapRuleException ex) {
			log.error("-------------------------------");
			log.error("A GapRuleException was thrown with the following message:");
			log.error(ex.getMessage());
			log.error("--------------------------------");
		}
	}


}
