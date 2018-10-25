package com.orford.gapruletest.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.orford.gapruletest.model.Campsite;
import com.orford.gapruletest.model.CampsiteSearch;

@Service
public class CampsiteSearchService {
	private final static Logger log = LoggerFactory.getLogger(CampsiteSearchService.class);
	
	CampsiteSearchByDateService campsitesByDateService;
	CampsiteService siteService;
	
	@Autowired
	public CampsiteSearchService(CampsiteService _locService, CampsiteSearchByDateService _locByDateService) {
		campsitesByDateService = _locByDateService;
		siteService = _locService;
		initialize();
	}


	public void initialize() {
		campsitesByDateService.mungeReservationData();
	}

	public List<Campsite> performSearch(CampsiteSearch _search) {
		log.info("performSearch() called in service.");
		List<Integer> availableSiteIDsByDate = campsitesByDateService.performSearch(_search);
		List<Campsite> availableSitesByDate = siteService.getCampsiteListFromIDs(availableSiteIDsByDate);

		return availableSitesByDate;
	}

}
