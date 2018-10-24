package com.orford.gapruletest.util;

import java.io.IOException;
import java.time.LocalDate;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.orford.gapruletest.model.CampsiteSearch;

public class CampsiteSearchDeserializer extends StdDeserializer<CampsiteSearch> {

	private static final long serialVersionUID = 1L;

	public final static String startDateKey  = "startDate";
	public final static String endDateKey		 = "endDate";
	
	public CampsiteSearchDeserializer() {
		this(null);
	}

	public CampsiteSearchDeserializer(Class<?> _vc) {
		super(_vc);
	}

	@Override
	public CampsiteSearch deserialize(JsonParser _p, DeserializationContext _ctxt)
			throws IOException, JsonProcessingException {
		JsonNode resNode = _p.getCodec().readTree(_p);

		String startDateStr = resNode.get(startDateKey).asText();
		String endDateStr = resNode. get(endDateKey).asText();

		CampsiteSearch outSearch = new CampsiteSearch();

		outSearch.setStartDate(LocalDate.parse(startDateStr));
		outSearch.setEndDate(LocalDate.parse(endDateStr));
		
		return outSearch;
	}

}
