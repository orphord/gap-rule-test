package com.campspot.orford.gapruletest.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.campspot.orford.gapruletest.model.Campsite;
import com.campspot.orford.gapruletest.model.CampsiteSearch;

@Service
public class CampsiteSearchService {
	private final static Logger log = LoggerFactory.getLogger(CampsiteSearchService.class);
	
	@Autowired
	CampsitesSearchByDateService campsitesByDateService;
	
	public void initialize() {
		campsitesByDateService.mungeReservationData();
	}

	public List<Campsite> performSearch(CampsiteSearch _search) {
		log.info("performSearch() called in service.");
		List<Integer> availableSitesByDate = campsitesByDateService.performSearch(_search);

		return null;
	}

}
