package com.orford.gapruletest.model;

import java.time.LocalDate;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.orford.gapruletest.util.CampsiteSearchDeserializer;

@JsonDeserialize(using = CampsiteSearchDeserializer.class)
public class CampsiteSearch {
	private LocalDate startDate;
	private LocalDate endDate;
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
	
	
}
