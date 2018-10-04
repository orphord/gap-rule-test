package com.campspot.orford.gapruletest.model;

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

public class CampsitesByDate implements Map<LocalDate, Set<Integer>> {
	private final static Logger log = LoggerFactory.getLogger(CampsitesByDate.class);

	private Map<LocalDate, Set<Integer>> mapCampsitesByDate;
	
	public CampsitesByDate() {
		mapCampsitesByDate = new HashMap<LocalDate, Set<Integer>>();
	}

	@Override
	public void clear() {
		mapCampsitesByDate.clear();		
	}

	@Override
	public boolean containsKey(Object _key) {
		return mapCampsitesByDate.containsKey(_key);
	}

	@Override
	public boolean containsValue(Object _value) {
		return mapCampsitesByDate.containsValue(_value);
	}

	@Override
	public Set<Entry<LocalDate, Set<Integer>>> entrySet() {
		return mapCampsitesByDate.entrySet();
	}

	@Override
	public Set<Integer> get(Object _key) {
		// Handle key outside of range case by returning empty set
		Set<Integer> outSet = mapCampsitesByDate.get(_key);
		if(outSet == null) {
			outSet = new HashSet<Integer>();
		}
		return outSet;
	}

	@Override
	public boolean isEmpty() {
		return mapCampsitesByDate.isEmpty();
	}

	@Override
	public Set<LocalDate> keySet() {
		return mapCampsitesByDate.keySet();
	}

	@Override
	public Set<Integer> put(LocalDate _key, Set<Integer> _value) {
		return mapCampsitesByDate.put(_key, _value);
	}

	@Override
	public void putAll(Map<? extends LocalDate, ? extends Set<Integer>> _m) {
		mapCampsitesByDate.putAll(_m);
	}

	@Override
	public Set<Integer> remove(Object _key) {
		return mapCampsitesByDate.remove(_key);
	}

	@Override
	public int size() {
		return mapCampsitesByDate.size();
	}

	@Override
	public Collection<Set<Integer>> values() {
		return mapCampsitesByDate.values();
	}


	public void initialize(List<LocalDate> _datesOfInterest, Set<Integer> _campsiteIDs) {
		log.debug("initialize() CampsitesByDate object.");

		for(LocalDate date : _datesOfInterest) {
			mapCampsitesByDate.put(date, _campsiteIDs.stream().collect(Collectors.toSet()));
		}
	}


	/**
	 * Proc to remove the campsite ID associated with _res from all dates represented in _datesToRemove.
	 *
	 * @param _res
	 * @param _datesToRemove
	 */
	public void removeCampsiteByDates(Reservation _res, List<LocalDate> _datesToRemove) {
		Integer campsiteID = _res.getCampsiteID();
		for(LocalDate resDate : _datesToRemove) {
			Set<Integer> campsiteIdSet = mapCampsitesByDate.get(resDate);
			campsiteIdSet.remove(campsiteID);
		}
		
	}
}
