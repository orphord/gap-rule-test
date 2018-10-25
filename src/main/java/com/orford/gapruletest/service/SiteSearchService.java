package com.orford.gapruletest.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.orford.gapruletest.model.Site;
import com.orford.gapruletest.model.SiteSearch;

@Service
public class SiteSearchService {
	private final static Logger log = LoggerFactory.getLogger(SiteSearchService.class);
	
	SearchByDateService locByDateService;
	SiteService siteService;
	
	@Autowired
	public SiteSearchService(SiteService _locService, SearchByDateService _locByDateService) {
		locByDateService = _locByDateService;
		siteService = _locService;
		initialize();
	}


	public void initialize() {
		// THIS IS A HACK TO LOAD FILE BASED DATA AT STARTUP
		locByDateService.mungeReservationData();
	}

	public List<Site> performSearch(SiteSearch _search) {
		log.info("performSearch() called in service.");
		List<Integer> availableSiteIDsByDate = locByDateService.performSearch(_search);
		List<Site> availableSitesByDate = siteService.getSiteListFromIDs(availableSiteIDsByDate);

		return availableSitesByDate;
	}

}
