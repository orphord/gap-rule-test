package com.orford.gapruletest.model;

import static org.junit.Assert.*;

import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;

import com.orford.gapruletest.model.Reservation;

public class ReservationTest {

	private Reservation testReservation;

	@Before
	public void setUp() throws Exception {
		testReservation = new Reservation();
		testReservation.setSiteID(new Integer(1));
		testReservation.setStartDate(LocalDate.parse("2018-06-01"));
		testReservation.setEndDate(LocalDate.parse("2018-06-07"));

	}

	@Test
	public void testSiteID() {
		Integer expectedID = new Integer(1);
		assertEquals(testReservation.getSiteID(), expectedID);
	}

	
	@Test
	public void testStartDate() {
		LocalDate expectedStartDate = LocalDate.parse("2018-06-01");
		assertEquals(testReservation.getStartDate(), expectedStartDate);
	}

	@Test 
	public void testEndDate() {
		LocalDate expectedEndDate = LocalDate.parse("2018-06-07");
		assertEquals(testReservation.getEndDate(), expectedEndDate);
	}
	
	@Test
	public void testFailStartDate() {
		LocalDate unExpectedStart = LocalDate.parse("2018-01-22");
		assertNotEquals(testReservation.getStartDate(), unExpectedStart);
	}

	@Test
	public void testFailEndDate() {
		LocalDate expectedEnd = LocalDate.parse("2018-01-25");
		assertNotEquals(testReservation.getEndDate(), expectedEnd);
	}
}
