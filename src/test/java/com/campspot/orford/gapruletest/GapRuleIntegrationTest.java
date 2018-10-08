package com.campspot.orford.gapruletest;

import static org.junit.Assert.*;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.hasItems;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.campspot.orford.gapruletest.model.Campsite;
import com.campspot.orford.gapruletest.model.CampsiteSearch;
import com.campspot.orford.gapruletest.service.CampsiteSearchService;
import com.campspot.orford.gapruletest.service.CampsiteService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GapRuleIntegrationTest {
	private static final Logger log = LoggerFactory.getLogger(GapRuleIntegrationTest.class);

	@Autowired
	CampsiteSearchService searchService;
	@Autowired
	CampsiteService siteService;

	@Before
	public void setUp() throws Exception {
		log.debug("setup() method called.");
		
	}


	@Test
	public void testEndDateGapHitsSites2and4() {
		CampsiteSearch search = new CampsiteSearch();
		search.setStartDate(LocalDate.parse("2018-06-04"));
		search.setEndDate(LocalDate.parse("2018-06-05"));
		List<Campsite> actualAvailSites = searchService.performSearch(search);
		List<String> acutalSiteNames = actualAvailSites.stream().map(site -> site.getName()).collect(Collectors.toList());

		assertThat("These should both be there", acutalSiteNames, hasItems("Cozy Cabin", "Cabin in the Woods"));
		assertThat("These should not be there", acutalSiteNames, not(hasItems("Comfy Cabin", "Rustic Cabin", "Rickety Cabin")));
	}


	@Test
	public void testStartDateAfterLastUnaccept() {
		CampsiteSearch search = new CampsiteSearch();
		search.setStartDate(LocalDate.parse("2018-06-25"));
		search.setEndDate(LocalDate.parse("2018-06-30"));
		List<Campsite> actualAvailSites = searchService.performSearch(search);
		List<String> actualAvailSiteNames = actualAvailSites.stream().map(site -> site.getName()).collect(Collectors.toList());

		assertThat("All sites should be there", actualAvailSiteNames, hasItems("Cozy Cabin", "Comfy Cabin", "Rustic Cabin", "Rickety Cabin", "Cabin in the Woods"));
	}


	@Test
	public void testStartDateGapHitsSites2And3() {
		CampsiteSearch search = new CampsiteSearch();
		search.setStartDate(LocalDate.parse("2018-06-11"));
		search.setEndDate(LocalDate.parse("2018-06-12"));
		List<Campsite> actualAvailSites = searchService.performSearch(search);
		List<String> actualAvailSiteNames = actualAvailSites.stream().map(site -> site.getName()).collect(Collectors.toList());

		assertThat("All sites should be there", actualAvailSiteNames, hasItems("Cozy Cabin","Rickety Cabin", "Cabin in the Woods"));
		assertThat("These two should NOT be there", actualAvailSiteNames, not(hasItems( "Comfy Cabin", "Rustic Cabin")));
	}
	
	
	@Test
	public void testSpanSite1Res() {
		CampsiteSearch search = new CampsiteSearch();
		search.setStartDate(LocalDate.parse("2018-06-06"));
		search.setEndDate(LocalDate.parse("2018-06-13"));
		List<Campsite> actualAvailSites = searchService.performSearch(search);
		List<String> actualAvailSiteNames = actualAvailSites.stream().map(site -> site.getName()).collect(Collectors.toList());

		assertThat("All sites should be there", actualAvailSiteNames, hasItems("Cabin in the Woods"));
		assertThat("These should NOT be there", actualAvailSiteNames, not(hasItems("Cozy Cabin","Rickety Cabin", "Comfy Cabin", "Rustic Cabin")));
	}
}
