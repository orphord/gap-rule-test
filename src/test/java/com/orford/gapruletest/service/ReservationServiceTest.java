package com.orford.gapruletest.service;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.orford.gapruletest.model.Reservation;
import com.orford.gapruletest.service.ReservationService;

public class ReservationServiceTest {
	private static final Logger log = LoggerFactory.getLogger(ReservationServiceTest.class);

	ReservationService testResSvc = new ReservationService();
	List<Reservation> testReservations = new ArrayList<Reservation>();
	Reservation testReservation1 = new Reservation();
	Reservation testReservation2 = new Reservation();
	Reservation testReservation3 = new Reservation();

	@Before
	public void setUp() throws Exception {
		log.debug("setup() function called.");
		testReservation1.setCampsiteID(new Integer(1));
		testReservation1.setStartDate(LocalDate.parse("2018-06-01"));
		testReservation1.setEndDate(LocalDate.parse("2018-06-07"));
		testReservation2.setCampsiteID(new Integer(1));
		testReservation2.setStartDate(LocalDate.parse("2018-06-10"));
		testReservation2.setEndDate(LocalDate.parse("2018-06-17"));
		testReservation3.setCampsiteID(new Integer(1));
		testReservation3.setStartDate(LocalDate.parse("2018-06-18"));
		testReservation3.setEndDate(LocalDate.parse("2018-06-20"));
		testReservations.add(testReservation1);
		testReservations.add(testReservation2);
		testReservations.add(testReservation3);

	}


	@Test
	public void testSetAllReservations() {
		log.debug("Testing setAllReservations().");
		testResSvc.setAllReservations(testReservations);
		List<Reservation> serviceRes = testResSvc.getAllReservations();
		assertThat(serviceRes.size(), is(equalTo(3)));
	}
	
	@Test
	public void testAddReservation() {
		log.debug("Testing addReservation().");
		testResSvc.setAllReservations(testReservations);
		List<Reservation> serviceRes = testResSvc.getAllReservations();
		assertThat(serviceRes.size(), is(equalTo(3)));
		Reservation anotherRes = new Reservation();
		anotherRes.setCampsiteID(new Integer(1));
		anotherRes.setStartDate(LocalDate.parse("2018-06-21"));
		anotherRes.setEndDate(LocalDate.parse("2018-06-23"));
		testResSvc.addReservation(anotherRes);
		serviceRes = testResSvc.getAllReservations();
		assertThat(serviceRes.size(), is(equalTo(4)));
	}

	@Test
	public void testLatestUnacceptDate() {
		log.debug("Testing latestUnacceptDate gets updated properly.");
		testResSvc.setAllReservations(testReservations);
		assertThat(testResSvc.findLatestUnacceptDate(new Integer(1)), is(equalTo(testReservation3.getEndDate().plusDays(testResSvc.getGapDays() + 1))));
		Reservation anotherRes = new Reservation();
		anotherRes.setCampsiteID(new Integer(1));
		anotherRes.setStartDate(LocalDate.parse("2018-06-21"));
		anotherRes.setEndDate(LocalDate.parse("2018-06-23"));
		testResSvc.addReservation(anotherRes);

		assertThat(testResSvc.findLatestUnacceptDate(new Integer(1)), is(equalTo(anotherRes.getEndDate().plusDays(testResSvc.getGapDays() + 1))));
	}
}
