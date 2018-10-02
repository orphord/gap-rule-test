package com.campspot.orford.gapruletest.service;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.campspot.orford.gapruletest.model.Reservation;

@Service
public class ReservationService {

	List<Reservation> allReservations = new LinkedList<Reservation>();

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
}
