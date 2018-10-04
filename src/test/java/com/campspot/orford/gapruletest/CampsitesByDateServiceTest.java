package com.campspot.orford.gapruletest;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;

import com.campspot.orford.gapruletest.model.Campsite;
import com.campspot.orford.gapruletest.service.CampsiteService;

public class CampsitesByDateServiceTest {
	CampsiteService testService = new CampsiteService();
	
	List<Campsite> sites = new ArrayList<Campsite>();

	@Before
	public void setUp() throws Exception {
		Campsite site1 = new Campsite();
		site1.setId(new Integer(1));
		site1.setName("Numero Uno");
		sites.add(site1);
		Campsite site2 = new Campsite();
		site2.setId(new Integer(2));
		site2.setName("Numero Dos");
		sites.add(site2);
		Campsite site3 = new Campsite();
		site3.setId(new Integer(3));
		site3.setName("Numero tres");
		sites.add(site3);
		testService.setAllCampsites(sites);
	}


	@Test
	public void testGetSiteIDs() {
		Set<Integer> siteIDs = testService.getCampsiteIDs();
		assertThat(siteIDs, containsInAnyOrder(1,2,3));
	}
	
	
	@Test
	public void testGetCampsitesForIDList() {
		List<Integer> siteIDs = new ArrayList<Integer>();
		siteIDs.add(new Integer(2));
		siteIDs.add(new Integer(3));

		List<Campsite> sitesForIDs = testService.getCampsiteListFromIDs(siteIDs);
		assertThat(sitesForIDs.size(), is(2));
		assertThat(sitesForIDs.get(0).getId(), is(2));

	}

}
