package com.campspot.orford.gapruletest.model;

import java.time.LocalDate;


public class Reservation {
	
	private LocalDate startDate;
	private LocalDate endDate;
	private Integer campsiteID;
	/**
	 * @return the startDate
	 */
	public LocalDate getStartDate() {
		return startDate;
	}
	/**
	 * @param _startDate the startDate to set
	 */
	public void setStartDate(LocalDate _startDate) {
		this.startDate = _startDate;
	}
	/**
	 * @return the endDate
	 */
	public LocalDate getEndDate() {
		return this.endDate;
	}
	/**
	 * @param _endDate the endDate to set
	 */
	public void setEndDate(LocalDate _endDate) {
		this.endDate = _endDate;
	}
	/**
	 * @return the campsiteID
	 */
	public Integer getCampsiteID() {
		return this.campsiteID;
	}
	/**
	 * @param _campsiteID the campsiteID to set
	 */
	public void setCampsiteID(Integer _campsiteID) {
		this.campsiteID = _campsiteID;
	}
	
	
}
