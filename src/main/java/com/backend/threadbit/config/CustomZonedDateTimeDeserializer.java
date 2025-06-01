package com.backend.threadbit.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.ZonedDateTime;

public class CustomZonedDateTimeDeserializer extends JsonDeserializer<ZonedDateTime> {
    @Override
    public ZonedDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String dateString = p.getText();
        if (dateString == null || dateString.isEmpty()) return null;
        // Assume UTC if no timezone specified
        if (!dateString.contains("Z") && !dateString.contains("+") && !dateString.contains("[")) {
            dateString += "+05:30";
        }
        return ZonedDateTime.parse(dateString);
    }
}