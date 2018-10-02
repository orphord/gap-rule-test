package com.campspot.orford.gapruletest.service;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.campspot.orford.gapruletest.model.Campsite;

@Service
public class CampsiteService {
	List<Campsite> allCampsites = new LinkedList<Campsite>();

	/**
	 * Function to add a single campsite, checking that it meets acceptability criteria.
	 * 
	 * @param _inSite
	 * @return Campsite indicating that it was successfully added or null if not.
	 */
	public Campsite addCampsite(Campsite _inSite) {
		return null;
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
		this.allCampsites = _allCampsites;
	}

	public Set<Integer> getCampsiteIDs() {
		Set<Integer> campsiteIDSet = new HashSet<Integer>();
		for(Campsite site : getAllCampsites()) {
			campsiteIDSet.add(site.getId());
		}

		return campsiteIDSet;
	}
	
	
}
