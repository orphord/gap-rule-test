package com.orford.gapruletest.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.orford.gapruletest.model.Site;

@Service
public class SiteService {
	private static final Logger log = LoggerFactory.getLogger(SiteService.class);

	private List<Site> allSites = new LinkedList<Site>();
	private Map<Integer, Site> mapSiteByID = new HashMap<Integer, Site>();

	/**
	 * Function to add a single site, checking that it meets acceptability criteria.
	 * 
	 * @param _inSite
	 * @return Site indicating that it was successfully added or null if not.
	 */
	public Site addSite(Site _inSite) {
		log.debug("addSite() called. " + _inSite.getId() + "; " + _inSite.getName());
		allSites.add(_inSite);
		Integer siteID = _inSite.getId();
		mapSiteByID.put(siteID, _inSite);

		return mapSiteByID.get(siteID);
	}

	/**
	 * @return the allSites
	 */
	public List<Site> getAllSites() {
		return this.allSites;
	}

	/**
	 * @param _allSites the allSites to set
	 */
	public void setAllSites(List<Site> _allSites) {
		log.debug("setAllSites() called.");
		for(Site site : _allSites) {
			this.addSite(site);
		}

	}

	public Set<Integer> getSiteIDs() {
		return mapSiteByID.keySet();
	}


	public List<Site> getSiteListFromIDs(List<Integer> _siteIDs) {
		log.debug("getSiteListFromIDs() called.");
		List<Site> siteList = new ArrayList<Site>();
		for(Integer siteID : _siteIDs) {
			siteList.add(mapSiteByID.get(siteID));
		}

		return siteList;
	}
}
