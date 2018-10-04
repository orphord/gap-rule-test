package com.campspot.orford.gapruletest.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.campspot.orford.gapruletest.model.Campsite;

@Service
public class CampsiteService {
	private static final Logger log = LoggerFactory.getLogger(CampsiteService.class);

	private List<Campsite> allCampsites = new LinkedList<Campsite>();
	private Map<Integer, Campsite> mapCampsiteByID = new HashMap<Integer, Campsite>();

	/**
	 * Function to add a single campsite, checking that it meets acceptability criteria.
	 * 
	 * @param _inSite
	 * @return Campsite indicating that it was successfully added or null if not.
	 */
	public Campsite addCampsite(Campsite _inSite) {
		log.debug("addCampsite() called. " + _inSite.getId() + "; " + _inSite.getName());
		allCampsites.add(_inSite);
		Integer siteID = _inSite.getId();
		mapCampsiteByID.put(siteID, _inSite);

		return mapCampsiteByID.get(siteID);
	}

	/**
	 * @return the allCampsites
	 */
	public List<Campsite> getAllCampsites() {
		return this.allCampsites;
	}

	/**
	 * @param _allCampsites the allCampsites to set
	 */
	public void setAllCampsites(List<Campsite> _allCampsites) {
		log.debug("setAllCampsites() called.");
		for(Campsite site : _allCampsites) {
			this.addCampsite(site);
		}

	}

	public Set<Integer> getCampsiteIDs() {
		return mapCampsiteByID.keySet();
	}


	public List<Campsite> getCampsiteListFromIDs(List<Integer> _siteIDs) {
		log.debug("getCampsiteListFromIDs() called.");
		List<Campsite> siteList = new ArrayList<Campsite>();
		for(Integer siteID : _siteIDs) {
			siteList.add(mapCampsiteByID.get(siteID));
		}

		return siteList;
	}
}
