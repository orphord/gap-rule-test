package com.campspot.orford.gapruletest.service;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.campspot.orford.gapruletest.model.Reservation;

@Service
public class ReservationService {

	List<Reservation> allReservations = new LinkedList<Reservation>();
	LocalDate latestUnacceptDate;

	/**
	 * Function to add a single reservation, checking that it meets acceptability criteria.
	 * 
	 * @param _inRes
	 * @return Reservation indicating that it was successfully added or null if not.
	 */
	public Reservation addReservation(Reservation _inRes) {
		return null;
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
	 * Loop through all Reservation objects passed in as param and find the latest unacceptable
	 * date among them.
	 * @param List<Reservation> -- list of reservations to be checked for latest unacceptable
	 * date
	 * @return LocalDate indicating the latest unacceptable date among this group of Reservations
	 */
	public LocalDate findLatestUnacceptDate(Integer _gapDays) {
		LocalDate latestUnaccept = LocalDate.MIN;
		long gapDays = _gapDays.longValue();

		for(Reservation res : this.getAllReservations()) {
			LocalDate resLatestUnacceptDay = res.getEndDate().plusDays(gapDays + 1);
			if(resLatestUnacceptDay.isAfter(latestUnaccept) ) {
				latestUnaccept = resLatestUnacceptDay;
			}
		}
		
		return latestUnaccept;
	}
}
