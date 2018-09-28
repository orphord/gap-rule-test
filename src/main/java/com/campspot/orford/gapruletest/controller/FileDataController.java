package com.campspot.orford.gapruletest.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

import com.campspot.orford.gapruletest.exception.GapRuleException;
import com.campspot.orford.gapruletest.model.Campsite;
import com.campspot.orford.gapruletest.model.Reservation;
import com.campspot.orford.gapruletest.model.CampsiteSearch;
import com.campspot.orford.gapruletest.util.DataObjectsFromFileUtil;


@Controller
public class FileDataController {
	private static Logger log = LoggerFactory.getLogger(FileDataController.class);

	@Value("${default.gap.days:1}")
	private Integer gapDays;

	/**
	 * loadData function gets optional parameters _optGapDays and _optFilePath parameters
	 * representing the command-line arguments which may be passed in indicating:
	 * 1) The operational gap in days that will be used to make the decision as to whether
	 * 		to return sites for a particular search request.
	 * 2) The path on the local filesystem to a json file (*assumed to be valid for this request*) 
	 * @param _optGapDays - gap in days
	 * @param _optFilePath - path on the local filesystem to a json file
	 *	(*assumed to be valid for this request*)
	 */
	public void loadData(Integer _optGapDays, String _optFilePath) {
		try {
			DataObjectsFromFileUtil dataFromFile = new DataObjectsFromFileUtil();
			List<Reservation> reservationList = dataFromFile.getReservationListFromFile(_optFilePath);
			List<Campsite> campsiteList = dataFromFile.getCampsiteListFromFile(_optFilePath);
			CampsiteSearch search = dataFromFile.getSearchFromFile(_optFilePath);

		} catch(GapRuleException ex) {
			log.error("-------------------------------");
			log.error("A GapRuleException was thrown with the following message:");
			log.error(ex.getMessage());
			log.error("--------------------------------");
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
	
	
}
