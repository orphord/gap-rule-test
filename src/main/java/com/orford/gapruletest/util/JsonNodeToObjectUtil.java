package com.orford.gapruletest.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.orford.gapruletest.exception.GapRuleException;
import com.orford.gapruletest.model.Site;
import com.orford.gapruletest.model.SiteSearch;
import com.orford.gapruletest.model.Reservation;

public class JsonNodeToObjectUtil {
	private static Logger log = LoggerFactory.getLogger(JsonNodeToObjectUtil.class);
	
	public List<Site> getSites(JsonNode _siteNode) throws GapRuleException {
		log.debug("getSites from JsonNode called.");
		List<Site> sites = new ArrayList<Site>();

		try {
			sites = new ObjectMapper().readerFor(new TypeReference<List<Site>>(){}).readValue(_siteNode);
		} catch (IOException ex) {
			String errMsg = "An IOException was thrown attempting to transform Site node to Site objects.";
			log.error(errMsg);
			throw new GapRuleException(errMsg);
		}

		return sites;
	}

	public List<Reservation> getReservations(JsonNode _resNode) throws GapRuleException {
		log.debug("getReservations from JsonNode called.");
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

	public SiteSearch getSiteSearch(JsonNode _searchNode) throws GapRuleException {
		log.debug("getSiteSearch from JsonNode called.");
		SiteSearch searchObj = new SiteSearch();

		try {
			searchObj = new ObjectMapper().treeToValue(_searchNode, SiteSearch.class);
		} catch (IOException ex) {
			String errMsg = "An IOException was thrown attempting to transform Search node to SiteSearch object.";
			log.error(errMsg);
			throw new GapRuleException(errMsg);
		}

		return searchObj;
	}

}
