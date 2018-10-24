package com.orford.gapruletest.model;

import static org.junit.Assert.*;

import java.time.LocalDate;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.orford.gapruletest.model.CampsiteSearch;

public class CampsiteSearchTest {

	private CampsiteSearch search;

	@Before
	public void setUp() throws Exception {
		search = new CampsiteSearch();
		search.setStartDate(LocalDate.parse("2018-01-20"));
		search.setEndDate(LocalDate.parse("2018-01-29"));
	}

	@Test
	public void testStartDate() {
		LocalDate expectedStart = LocalDate.parse("2018-01-20");
		assertEquals(search.getStartDate(), expectedStart);
	}

	@Test
	public void testEndDate() {
		LocalDate expectedEnd = LocalDate.parse("2018-01-29");
		assertEquals(search.getEndDate(), expectedEnd);
	}
	
	@Test
	public void testFailStartDate() {
		LocalDate unExpectedStart = LocalDate.parse("2018-01-22");
		assertNotEquals(search.getStartDate(), unExpectedStart);
	}

	@Test
	public void testFailEndDate() {
		LocalDate expectedEnd = LocalDate.parse("2018-01-25");
		assertNotEquals(search.getEndDate(), expectedEnd);
	}
}
