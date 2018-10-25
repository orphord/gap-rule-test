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

import com.orford.gapruletest.model.SitesByDate;
import com.orford.gapruletest.model.Reservation;
import com.orford.gapruletest.service.SiteService;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SitesByDateTest {
	private static final Logger log = LoggerFactory.getLogger(SitesByDateTest.class);

	List<Reservation> reservations = new ArrayList<Reservation>();
	Reservation testReservationEarly = new Reservation();
	Reservation testReservationLate  = new Reservation();
	Reservation testReservationOverlap = new Reservation();
	
	SitesByDate testCbd = new SitesByDate();

	@Before
	public void setUp() throws Exception {
		SiteService csSvcMock = mock(SiteService.class);
		Set<Integer> siteIDs = new HashSet<Integer>();
		siteIDs.add(new Integer(1));
		siteIDs.add(new Integer(2));
		siteIDs.add(new Integer(3));
		when(csSvcMock.getSiteIDs()).thenReturn(siteIDs);
		
		testReservationEarly.setSiteID(new Integer(1));
		testReservationEarly.setStartDate(LocalDate.parse("2018-06-01"));
		testReservationEarly.setEndDate(LocalDate.parse("2018-06-07"));
		testReservationLate.setSiteID(new Integer(1));
		testReservationLate.setStartDate(LocalDate.parse("2018-06-10"));
		testReservationLate.setEndDate(LocalDate.parse("2018-06-17"));
		testReservationOverlap.setSiteID(new Integer(1));
		testReservationOverlap.setStartDate(LocalDate.parse("2018-06-05"));
		testReservationOverlap.setEndDate(LocalDate.parse("2018-06-12"));
		reservations.add(testReservationEarly);
		reservations.add(testReservationLate);
		reservations.add(testReservationOverlap);
		
		// Setup test SitesByDate object
		LocalDate startDate = LocalDate.parse("2016-04-01");
		int gapDays = 1; // Assuming 1 gap day for testing purposes
		LocalDate latestUnaccept = testReservationLate.getEndDate().plusDays(gapDays + 1);
		List<LocalDate> dates = this.datesBetweenDates(startDate, latestUnaccept);
		testCbd.initialize(dates, csSvcMock.getSiteIDs());
	}

	@Test
	public void testAddReservationReservedDates() {
		List<LocalDate> resDates =  this.datesBetweenDates(testReservationEarly.getStartDate(), testReservationEarly.getEndDate());
		testCbd.removeSiteByDates(testReservationEarly, resDates);
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
