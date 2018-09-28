package com.campspot.orford.gapruletest.util;

import java.io.IOException;
import java.time.LocalDate;

import com.campspot.orford.gapruletest.model.Reservation;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class ReservationDeserializer extends StdDeserializer<Reservation> {

	private static final long serialVersionUID = 1L;

	public final static String campsiteIDKey = "campsiteId";
	public final static String startDateKey  = "startDate";
	public final static String endDateKey		 = "endDate";
	
	public ReservationDeserializer() {
		this(null);
	}

	public ReservationDeserializer(Class<?> _vc) {
		super(_vc);
	}

	@Override
	public Reservation deserialize(JsonParser _p, DeserializationContext _ctxt)
			throws IOException, JsonProcessingException {
		JsonNode resNode = _p.getCodec().readTree(_p);
		int campsiteIdInt = resNode.get(campsiteIDKey).asInt();
		String startDateStr = resNode.get(startDateKey).asText();
		String endDateStr = resNode. get(endDateKey).asText();

		//DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-DD");
		Reservation outRes = new Reservation();
		outRes.setCampsiteID(new Integer(campsiteIdInt));
		outRes.setStartDate(LocalDate.parse(startDateStr));
		outRes.setEndDate(LocalDate.parse(endDateStr));
		
		return outRes;
	}

}
