package com.orford.gapruletest.model;

import java.time.LocalDate;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.orford.gapruletest.util.ReservationDeserializer;

@JsonDeserialize(using = ReservationDeserializer.class)
public class Reservation {
	private LocalDate startDate;
	private LocalDate endDate;
	private Integer siteID;
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
	 * @return the siteID
	 */
	public Integer getSiteID() {
		return this.siteID;
	}
	/**
	 * @param _siteID the siteID to set
	 */
	public void setSiteID(Integer _siteID) {
		this.siteID = _siteID;
	}
	
	
}
