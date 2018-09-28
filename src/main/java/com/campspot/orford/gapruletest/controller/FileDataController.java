package com.campspot.orford.gapruletest.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;


@Controller
public class FileDataController {
	private static Logger log = LoggerFactory.getLogger(FileDataController.class);

	@Value("${default.gap.days:1}")
	private Integer gapDays;
	
	public void loadData(Integer _gapDays, String _optFilePath) {
		log.info("ZZZZZZZ gapDays: " + gapDays);
		log.info("YYYYYYY param _gapDays: " + _gapDays);
		log.info("XXXXXXX param _optFilePath: " + _optFilePath);
		
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
	
	
}
