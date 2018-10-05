package com.campspot.orford.gapruletest.service;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.campspot.orford.gapruletest.model.Reservation;

@Service
public class ReservationService {
	private final static Logger log = LoggerFactory.getLogger(ReservationService.class);

	List<Reservation> allReservations = new LinkedList<Reservation>();
	LocalDate latestUnacceptDate = LocalDate.MIN;
	long gapDays = 1; // default gap to 1

	/**
	 * Function to add a single reservation, checking that it meets acceptability criteria.
	 * 
	 * @param _inRes
	 */
	public void addReservation(Reservation _inRes) {
		allReservations.add(_inRes);
	}


	public List<Reservation> getAllReservations() {
		return allReservations;
	}
	
	public void setAllReservations(List<Reservation> _inReservations) {
		allReservations = _inReservations;
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
	 * @return the gapDays
	 */
	public long getGapDays() {
		return this.gapDays;
	}


	/**
	 * @param _gapDays the gapDays to set
	 */
	public void setGapDays(long _gapDays) {
		this.gapDays = _gapDays;
	}


	/**
	 * Loop through all Reservation objects passed in as param and find the latest unacceptable
	 * date among them.
	 * @param List<Reservation> -- list of reservations to be checked for latest unacceptable
	 * date
	 * @return LocalDate indicating the latest unacceptable date among this group of Reservations
	 */
	public LocalDate findLatestUnacceptDate(Integer _gapDays) {
		log.debug("findLatestUnacceptDate() called.");
		gapDays = _gapDays.longValue();

		for(Reservation res : this.getAllReservations()) {
			LocalDate resLatestUnacceptDate = res.getEndDate().plusDays(this.getGapDays() + 1);
			this.updateLatestUnaccept(resLatestUnacceptDate);
		}
		
		return this.latestUnacceptDate;
	}
	
	public LocalDate updateLatestUnaccept(LocalDate _dateToCheck) {
		if(_dateToCheck.isAfter(this.latestUnacceptDate) ) {
			this.latestUnacceptDate = _dateToCheck;
		}
		return this.latestUnacceptDate;
	}
}
