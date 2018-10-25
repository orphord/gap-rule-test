package com.orford.gapruletest.model;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SitesByDate implements Map<LocalDate, Set<Integer>> {
	private final static Logger log = LoggerFactory.getLogger(SitesByDate.class);

	private Map<LocalDate, Set<Integer>> mapSitesByDate;
	
	public SitesByDate() {
		mapSitesByDate = new HashMap<LocalDate, Set<Integer>>();
	}

	@Override
	public void clear() {
		mapSitesByDate.clear();		
	}

	@Override
	public boolean containsKey(Object _key) {
		return mapSitesByDate.containsKey(_key);
	}

	@Override
	public boolean containsValue(Object _value) {
		return mapSitesByDate.containsValue(_value);
	}

	@Override
	public Set<Entry<LocalDate, Set<Integer>>> entrySet() {
		return mapSitesByDate.entrySet();
	}

	@Override
	public Set<Integer> get(Object _key) {
		// Handle key outside of range case by returning empty set
		Set<Integer> outSet = mapSitesByDate.get(_key);
		if(outSet == null) {
			outSet = new HashSet<Integer>();
		}
		return outSet;
	}

	@Override
	public boolean isEmpty() {
		return mapSitesByDate.isEmpty();
	}

	@Override
	public Set<LocalDate> keySet() {
		return mapSitesByDate.keySet();
	}

	@Override
	public Set<Integer> put(LocalDate _key, Set<Integer> _value) {
		return mapSitesByDate.put(_key, _value);
	}

	@Override
	public void putAll(Map<? extends LocalDate, ? extends Set<Integer>> _m) {
		mapSitesByDate.putAll(_m);
	}

	@Override
	public Set<Integer> remove(Object _key) {
		return mapSitesByDate.remove(_key);
	}

	@Override
	public int size() {
		return mapSitesByDate.size();
	}

	@Override
	public Collection<Set<Integer>> values() {
		return mapSitesByDate.values();
	}


	public void initialize(List<LocalDate> _datesOfInterest, Set<Integer> _siteIDs) {
		log.debug("initialize() SitesByDate object.");

		for(LocalDate date : _datesOfInterest) {
			mapSitesByDate.put(date, _siteIDs.stream().collect(Collectors.toSet()));
		}
	}


	/**
	 * Proc to remove the site ID associated with _res from all dates represented in _datesToRemove.
	 *
	 * @param _res
	 * @param _datesToRemove
	 */
	public void removeSiteByDates(Reservation _res, List<LocalDate> _datesToRemove) {
		Integer siteID = _res.getSiteID();
		for(LocalDate resDate : _datesToRemove) {
			Set<Integer> siteIdSet = mapSitesByDate.get(resDate);
			siteIdSet.remove(siteID);
		}
		
	}
}
