package com.orford.gapruletest.model;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.orford.gapruletest.model.CampsitesByDate;
import com.orford.gapruletest.model.Reservation;
import com.orford.gapruletest.service.CampsiteService;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CampsitesByDateTest {
	private static final Logger log = LoggerFactory.getLogger(CampsitesByDateTest.class);

	List<Reservation> reservations = new ArrayList<Reservation>();
	Reservation testReservationEarly = new Reservation();
	Reservation testReservationLate  = new Reservation();
	Reservation testReservationOverlap = new Reservation();
	
	CampsitesByDate testCbd = new CampsitesByDate();

	@Before
	public void setUp() throws Exception {
		CampsiteService csSvcMock = mock(CampsiteService.class);
		Set<Integer> campsiteIDs = new HashSet<Integer>();
		campsiteIDs.add(new Integer(1));
		campsiteIDs.add(new Integer(2));
		campsiteIDs.add(new Integer(3));
		when(csSvcMock.getCampsiteIDs()).thenReturn(campsiteIDs);
		
		testReservationEarly.setCampsiteID(new Integer(1));
		testReservationEarly.setStartDate(LocalDate.parse("2018-06-01"));
		testReservationEarly.setEndDate(LocalDate.parse("2018-06-07"));
		testReservationLate.setCampsiteID(new Integer(1));
		testReservationLate.setStartDate(LocalDate.parse("2018-06-10"));
		testReservationLate.setEndDate(LocalDate.parse("2018-06-17"));
		testReservationOverlap.setCampsiteID(new Integer(1));
		testReservationOverlap.setStartDate(LocalDate.parse("2018-06-05"));
		testReservationOverlap.setEndDate(LocalDate.parse("2018-06-12"));
		reservations.add(testReservationEarly);
		reservations.add(testReservationLate);
		reservations.add(testReservationOverlap);
		
		// Setup test CampsitesByDate object
		LocalDate startDate = LocalDate.parse("2016-04-01");
		int gapDays = 1; // Assuming 1 gap day for testing purposes
		LocalDate latestUnaccept = testReservationLate.getEndDate().plusDays(gapDays + 1);
		List<LocalDate> dates = this.datesBetweenDates(startDate, latestUnaccept);
		testCbd.initialize(dates, csSvcMock.getCampsiteIDs());
	}

	@Test
	public void testAddReservationReservedDates() {
		List<LocalDate> resDates =  this.datesBetweenDates(testReservationEarly.getStartDate(), testReservationEarly.getEndDate());
		testCbd.removeCampsiteByDates(testReservationEarly, resDates);
		LocalDate testDate = testReservationEarly.getStartDate().plusDays(1);
		Set<Integer> remainingSiteIDs = testCbd.get(testDate);

		assertThat(remainingSiteIDs, not(contains(new Integer(1))));
		assertThat(remainingSiteIDs, contains(2,3));

	}

	
	private List<LocalDate> datesBetweenDates(LocalDate _start, LocalDate _end) {
		List<LocalDate> daysBetween = new ArrayList<LocalDate>();
		long numDaysBetween = ChronoUnit.DAYS.between(_start, _end);
		for(int i = 0; i <= numDaysBetween; i++) {
			daysBetween.add(_start.plusDays(i));
		}

		return daysBetween;
	}

}
