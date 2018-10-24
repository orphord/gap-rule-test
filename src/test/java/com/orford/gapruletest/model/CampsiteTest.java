package com.orford.gapruletest.model;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.orford.gapruletest.model.Campsite;

public class CampsiteTest {

	private Campsite campsite;

	@Before
	public void setUp() throws Exception {
		campsite = new Campsite();
		campsite.setId(new Integer(1));
		campsite.setName("Trojan Rabbit");
	}

	@Test
	public void testCampsiteID() {
		Integer expectedID = new Integer(1);
		assertEquals(campsite.getId(), expectedID);
	}

	@Test
	public void testCampsiteName() {
		String expectedName = "Trojan Rabbit";
		assertEquals(campsite.getName(), expectedName);
	}
}
