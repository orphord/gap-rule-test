package com.orford.gapruletest.util;

import java.io.IOException;
import java.time.LocalDate;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.orford.gapruletest.model.SiteSearch;

public class SiteSearchDeserializer extends StdDeserializer<SiteSearch> {

	private static final long serialVersionUID = 1L;

	public final static String startDateKey  = "startDate";
	public final static String endDateKey		 = "endDate";
	
	public SiteSearchDeserializer() {
		this(null);
	}

	public SiteSearchDeserializer(Class<?> _vc) {
		super(_vc);
	}

	@Override
	public SiteSearch deserialize(JsonParser _p, DeserializationContext _ctxt)
			throws IOException, JsonProcessingException {
		JsonNode resNode = _p.getCodec().readTree(_p);

		String startDateStr = resNode.get(startDateKey).asText();
		String endDateStr = resNode. get(endDateKey).asText();

		SiteSearch outSearch = new SiteSearch(LocalDate.parse(startDateStr), LocalDate.parse(endDateStr));

		return outSearch;
	}

}
