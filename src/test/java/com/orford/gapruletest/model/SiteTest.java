package com.orford.gapruletest.model;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.orford.gapruletest.model.Site;

public class SiteTest {

	private Site site;

	@Before
	public void setUp() throws Exception {
		site = new Site();
		site.setId(new Integer(1));
		site.setName("Trojan Rabbit");
	}

	@Test
	public void testSiteID() {
		Integer expectedID = new Integer(1);
		assertEquals(site.getId(), expectedID);
	}

	@Test
	public void testSiteName() {
		String expectedName = "Trojan Rabbit";
		assertEquals(site.getName(), expectedName);
	}
}
