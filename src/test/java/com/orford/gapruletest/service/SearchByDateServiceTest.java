package com.orford.gapruletest.service;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.orford.gapruletest.model.Site;
import com.orford.gapruletest.service.SiteService;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;

public class SearchByDateServiceTest {
	SiteService testService = new SiteService();
	
	List<Site> sites = new ArrayList<Site>();

	@Before
	public void setUp() throws Exception {
		Site site1 = new Site();
		site1.setId(new Integer(1));
		site1.setName("Numero Uno");
		sites.add(site1);
		Site site2 = new Site();
		site2.setId(new Integer(2));
		site2.setName("Numero Dos");
		sites.add(site2);
		Site site3 = new Site();
		site3.setId(new Integer(3));
		site3.setName("Numero tres");
		sites.add(site3);
		testService.setAllSites(sites);
	}


	@Test
	public void testGetSiteIDs() {
		Set<Integer> siteIDs = testService.getSiteIDs();
		assertThat(siteIDs, containsInAnyOrder(1,2,3));
	}
	
	
	@Test
	public void testGetSitesForIDList() {
		List<Integer> siteIDs = new ArrayList<Integer>();
		siteIDs.add(new Integer(2));
		siteIDs.add(new Integer(3));

		List<Site> sitesForIDs = testService.getSiteListFromIDs(siteIDs);
		assertThat(sitesForIDs.size(), is(equalTo(2)));
		assertThat(sitesForIDs.get(0).getId(), is(equalTo(2)));

	}

}
