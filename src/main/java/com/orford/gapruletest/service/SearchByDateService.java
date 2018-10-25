package com.orford.gapruletest.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.orford.gapruletest.model.SiteSearch;
import com.orford.gapruletest.model.SitesByDate;
import com.orford.gapruletest.model.Reservation;

@Service
public class SearchByDateService {
	private static final Logger log = LoggerFactory.getLogger(SearchByDateService.class);

	@Value("${default.start.date.str}")
	String startDateStr;
	LocalDate startDate;

	@Value("${default.gap.days:1}")
	private Integer gapDays;

	@Autowired
	ReservationService resService;

	@Autowired
	SiteService siteService;

	SitesByDate sitesByUnreservedDate;
	SitesByDate sitesByAcceptableStartDate;
	SitesByDate sitesByAcceptableEndDate;
	
	private final static long DAYS_TO_GAP = 2;

	public SearchByDateService() {
		sitesByUnreservedDate = new SitesByDate();
		sitesByAcceptableStartDate = new SitesByDate();
		sitesByAcceptableEndDate = new SitesByDate();
	}

	public void mungeReservationData() {
		log.debug("mungeReservationData() function called.");

		// Get List of Reservation objects and process it for use in searches
		List<Reservation> reservations = resService.getAllReservations();

		// 1. Set latest unaccept date if later than the current
		resService.setLatestUnacceptDate(resService.findLatestUnacceptDate(gapDays));

		// 2. We have the latest unaccept date, now fill in dates from today in Map
		this.initializeSiteIDsByDatesMaps();

		// 3. Based on reservations, remove site IDs from dates where they are not acceptable
		for(Reservation res : reservations) {
			List<LocalDate> reservedDates = this.findReservedDates(res);
			sitesByUnreservedDate.removeSiteByDates(res, reservedDates);

			List<LocalDate> nonStartDates = this.findNonStartDates(res);
			nonStartDates.addAll(reservedDates);
			sitesByAcceptableStartDate.removeSiteByDates(res, nonStartDates);

			List<LocalDate> nonEndDates = this.findNonEndDates(res);
			nonEndDates.addAll(reservedDates);
			sitesByAcceptableEndDate.removeSiteByDates(res, nonEndDates);

		}
	}


	public List<Integer> performSearch(SiteSearch _search) {
		LocalDate searchStartDate = _search.getStartDate();
		LocalDate searchEndDate = _search.getEndDate();

		// Handle case where startDate is after latest unaccept date --> all sites are available
		if(searchStartDate.isAfter(resService.getLatestUnacceptDate())) {
			return new ArrayList<Integer>(siteService.getSiteIDs());
		}
		
		// At this point in the business process do not search past latest unaccept date
		// If search dates are outside of range of acceptable dates, all sites are available
		LocalDate latestUnacceptDate = resService.getLatestUnacceptDate();
		if(searchEndDate.isAfter(latestUnacceptDate)) {
			searchEndDate = latestUnacceptDate;
		}

		// Get a list of LocalDate objects representing each date between the search start and search end inclusive
		List<LocalDate> reservationDates = this.getDatesBetweenDates(searchStartDate, searchEndDate);

		// Get set of Integer objects representing sites that are acceptable start date and enddate
		Set<Integer> startDateSiteIDs = sitesByAcceptableStartDate.get(searchStartDate);
		Set<Integer> endDateSiteIDs = sitesByAcceptableEndDate.get(searchEndDate);

		// Now must be sure that every date within the reservation is checked to be sure search doesn't
		// overlap existing reservation
		Set<Integer> unreservedSiteIDs = sitesByUnreservedDate.get(searchStartDate);
		for(LocalDate date : reservationDates) {
			Set<Integer> sitesForThisDate = sitesByUnreservedDate.get(date);
			unreservedSiteIDs.retainAll(sitesForThisDate);
		}

		// Find intersection of those sites that meet search criteria
		// all sites available on start date (taking gap into account)
		// all sites available on end date (taking gap into account)
		// all sites unreserved on each date of reservation
		Set<Integer> availableSiteIDs = new HashSet<Integer>(startDateSiteIDs);
		availableSiteIDs.retainAll(endDateSiteIDs);
		availableSiteIDs.retainAll(unreservedSiteIDs);


		return new ArrayList<Integer>(availableSiteIDs);
	}
	
	/**
	 * Method to initialize sitesIDs by date map.  The idea is to start from today to the latest unaccept date
	 * and optimistically act as if all sites are available.  The next step will winnow down the sites available on each date.
	 */
	private void initializeSiteIDsByDatesMaps() {
		// Start from today and add Set of all site IDs for each date between now and latest unaccept date
		this.setStartDate(LocalDate.parse(startDateStr));
		
		List<LocalDate> datesOfInterest = this.getDatesBetweenDates(this.getStartDate(), resService.getLatestUnacceptDate());
		Set<Integer> siteIdSet = siteService.getSiteIDs();
		
		sitesByUnreservedDate.initialize(datesOfInterest, siteIdSet);
		sitesByAcceptableStartDate.initialize(datesOfInterest, siteIdSet);
		sitesByAcceptableEndDate.initialize(datesOfInterest, siteIdSet);

	}


	/**
	 * @return the gapDays
	 */
	public Integer getGapDays() {
		return this.gapDays;
	}

	/**
	 * @param _gapDays the gapDays to set
	 */
	public void setGapDays(Integer _gapDays) {
		this.gapDays = _gapDays;
	}


	/**
	 * @return the startDate
	 */
	public LocalDate getStartDate() {
		return this.startDate;
	}

	/**
	 * @param _startDate the startDate to set
	 */
	public void setStartDate(LocalDate _startDate) {
		this.startDate = _startDate;
	}

	/**
	 * Reserved dates are those dates which are between startDate and endDate (inclusive) of a reservation.
	 * 
	 * @param _res
	 * @return List of LocalDate objects representing all days this reservation includes (ie. startDate -> endDate inclusive)
	 */
	private List<LocalDate> findReservedDates(Reservation _res) {
		List<LocalDate> reservedDates = this.getDatesBetweenDates(_res.getStartDate(), _res.getEndDate());
		return reservedDates;
	}


	/**
	 * Non start dates are those dates which would cause a gap of 1 to <gap> days after a reservation's endDate
	 * 
	 * @param _res
	 * @return List of LocalDate objects representing days where a reservation may _not_ be started due to *this*
	 * reservation's endDate taking into account the number of gap days defined.
	 */
	private List<LocalDate> findNonStartDates(Reservation _res) {
		LocalDate firstNonStartDay = _res.getEndDate().plusDays(DAYS_TO_GAP);
		LocalDate lastNonStartDay  = firstNonStartDay.plusDays(this.getGapDays() - 1);  // -1 because getDatesBetweenDates is inclusive 
		List<LocalDate> nonStartDates = this.getDatesBetweenDates(firstNonStartDay, lastNonStartDay); 

		return nonStartDates;
	}


	/**
	 * Non end dates are those dates which would cause a gap of 1 to <gap> days before a reservation's startDate
	 * 
	 * @param _res
	 * @return List of LocalDate objects representing days where a reservation may _not_ be started due to *this*
	 * reservation's endDate taking into account the number of gap days defined.
	 */
	private List<LocalDate> findNonEndDates(Reservation _res) {
		LocalDate lastNonEndDay = _res.getStartDate().minusDays(DAYS_TO_GAP);
		LocalDate firstNonEndDay = lastNonEndDay.minusDays(this.getGapDays() - 1); // -1 because getDatesBetweenDates is inclusive
		List<LocalDate> nonEndDates = this.getDatesBetweenDates(firstNonEndDay, lastNonEndDay);

		return nonEndDates;
	}


	/**
	 * Function to return a List of LocalDate objects between _start and _end inclusive of both.
	 *
	 * @param _start
	 * @param _end
	 * @return
	 */
	private List<LocalDate> getDatesBetweenDates(LocalDate _start, LocalDate _end) {
		List<LocalDate> daysBetween = new ArrayList<LocalDate>();
		long numDaysBetween = ChronoUnit.DAYS.between(_start, _end);
		for(int i = 0; i <= numDaysBetween; i++) {
			daysBetween.add(_start.plusDays(i));
		}

		return daysBetween;
	}


}
