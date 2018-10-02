package com.campspot.orford.gapruletest.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.campspot.orford.gapruletest.model.CampsitesByDate;
import com.campspot.orford.gapruletest.model.Reservation;

@Service
public class CampsitesSearchByDateService {
	private static final Logger log = LoggerFactory.getLogger(CampsitesSearchByDateService.class);

	@Value("${default.start.date.str}")
	String startDateStr;

	@Value("${default.gap.days:1}")
	private Integer gapDays;

	@Autowired
	ReservationService resService;

	@Autowired
	CampsiteService siteService;

	CampsitesByDate campsitesByUnreservedDate;
	CampsitesByDate campsitesByAcceptableStartDate;
	CampsitesByDate campsitesByAcceptableEndDate;
	LocalDate latestUnacceptDate;
	
	private final static long DAYS_TO_GAP = 2;

	public CampsitesSearchByDateService() {
		latestUnacceptDate = LocalDate.now();
		campsitesByUnreservedDate = new CampsitesByDate();
		campsitesByAcceptableStartDate = new CampsitesByDate();
		campsitesByAcceptableEndDate = new CampsitesByDate();
	}

	public void mungeReservationData() {
		log.info("PostConstruct mungeReservationData() function called.");

		// Get List of Reservation objects and process it for use in searches
		List<Reservation> reservations = resService.getAllReservations();

		// 1. Set latest unaccept date if later than the current
		this.setLatestUnacceptDate(this.findLatestUnacceptDate(reservations));

		// 2. We have the latest unaccept date, now fill in dates from today in Map
		this.initializeCampsiteIDsByDatesMaps();

		// 3. Based on reservations, remove campsite IDs from dates where they are not acceptable
		for(Reservation res : reservations) {
			List<LocalDate> reservedDates = this.findReservedDates(res);
			campsitesByUnreservedDate.removeCampsiteByDates(res, reservedDates);

			List<LocalDate> nonStartDates = this.findNonStartDates(res);
			nonStartDates.addAll(reservedDates);
			campsitesByAcceptableStartDate.removeCampsiteByDates(res, nonStartDates);
			

			List<LocalDate> nonEndDates = this.findNonEndDates(res);
			nonEndDates.addAll(reservedDates);
			campsitesByAcceptableEndDate.removeCampsiteByDates(res, nonEndDates);

		}
	}


	/**
	 * @return the latestUnacceptDate
	 */
	public LocalDate getLatestUnacceptDate() {
		return this.latestUnacceptDate;
	}

	/**
	 * @param _latestUnacceptDate the latestUnacceptDate to set
	 */
	public void setLatestUnacceptDate(LocalDate _latestUnacceptDate) {
		this.latestUnacceptDate = _latestUnacceptDate;
	}
	
	/**
	 * Loop through all Reservation objects passed in as param and find the latest unacceptable
	 * date among them.
	 * @param List<Reservation> -- list of reservations to be checked for latest unacceptable
	 * date
	 * @return LocalDate indicating the latest unacceptable date among this group of Reservations
	 */
	private LocalDate findLatestUnacceptDate(List<Reservation> _reservations) {
		LocalDate latestUnaccept = LocalDate.parse(startDateStr);
		long gapDays = this.getGapDays().longValue();

		for(Reservation res : _reservations) {
			LocalDate resLatestUnacceptDay = res.getEndDate().plusDays(gapDays + 1);
			if(resLatestUnacceptDay.isAfter(latestUnaccept) ) {
				latestUnaccept = resLatestUnacceptDay;
			}
		}
		
		return latestUnaccept;
	}
	
	/**
	 * Method to initialize campsitesIDs by date map.  The idea is to start from today to the latest unaccept date
	 * and optimistically act as if all campsites are available.  The next step will winnow down the sites available on each date.
	 */
	private void initializeCampsiteIDsByDatesMaps() {
		Set<Integer> campsiteIdSet = siteService.getCampsiteIDs();

		// Start from today and add Set of all campsite IDs for each date between now and latest unaccept date
		LocalDate startDate = LocalDate.parse(startDateStr);
		List<LocalDate> datesOfInterest = this.getDatesBetweenDates(startDate, this.getLatestUnacceptDate());

		for(LocalDate date : datesOfInterest) {
			campsitesByUnreservedDate.put(date, campsiteIdSet.stream().collect(Collectors.toSet()));
			campsitesByAcceptableStartDate.put(date, campsiteIdSet.stream().collect(Collectors.toSet()));
			campsitesByAcceptableEndDate.put(date, campsiteIdSet.stream().collect(Collectors.toSet()));
		}
		
	
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
