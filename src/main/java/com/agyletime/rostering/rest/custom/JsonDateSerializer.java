package com.agyletime.rostering.rest.custom;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

@Component
public class JsonDateSerializer extends JsonSerializer<Date>{

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");

	public void serialize(Date date, JsonGenerator gen, SerializerProvider provider) throws IOException{
		String formattedDate = dateFormat.format(date);
		gen.writeString(formattedDate);
	}
}
