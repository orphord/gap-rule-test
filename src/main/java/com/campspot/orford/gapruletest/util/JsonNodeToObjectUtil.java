package com.campspot.orford.gapruletest.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.campspot.orford.gapruletest.exception.GapRuleException;
import com.campspot.orford.gapruletest.model.Campsite;
import com.campspot.orford.gapruletest.model.CampsiteSearch;
import com.campspot.orford.gapruletest.model.Reservation;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonNodeToObjectUtil {
	private static Logger log = LoggerFactory.getLogger(JsonNodeToObjectUtil.class);
	
	public List<Campsite> getCampsites(JsonNode _campsiteNode) throws GapRuleException {
		log.info("getCampsites from JsonNode called.");
		List<Campsite> campsites = new ArrayList<Campsite>();

		try {
			campsites = new ObjectMapper().readerFor(new TypeReference<List<Campsite>>(){}).readValue(_campsiteNode);
		} catch (IOException ex) {
			String errMsg = "An IOException was thrown attempting to transform Campsite node to Campsite objects.";
			log.error(errMsg);
			throw new GapRuleException(errMsg);
		}

		return campsites;
	}

	public List<Reservation> getReservations(JsonNode _resNode) throws GapRuleException {
		log.info("getReservations from JsonNode called.");
		List<Reservation> reservations = new ArrayList<Reservation>();

		try {
			reservations = new ObjectMapper().readerFor(new TypeReference<List<Reservation>>(){}).readValue(_resNode);
		} catch (IOException ex) {
			String errMsg = "An IOException was thrown attempting to transform reservation node to Reservation objects.";
			log.error(errMsg);
			throw new GapRuleException(errMsg);
		}

		return reservations;
		
	}

	public CampsiteSearch getCampsiteSearch(JsonNode _searchNode) throws GapRuleException {
		log.info("getCampsiteSearch from JsonNode called.");
		CampsiteSearch searchObj = new CampsiteSearch();

		try {
			searchObj = new ObjectMapper().treeToValue(_searchNode, CampsiteSearch.class);
		} catch (IOException ex) {
			String errMsg = "An IOException was thrown attempting to transform Search node to CampsiteSearch object.";
			log.error(errMsg);
			throw new GapRuleException(errMsg);
		}

		return searchObj;
	}

}