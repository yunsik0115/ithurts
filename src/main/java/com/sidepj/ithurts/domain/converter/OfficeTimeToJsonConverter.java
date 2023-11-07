package com.sidepj.ithurts.domain.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.io.IOException;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

@Converter(autoApply = true)
@Slf4j
public class OfficeTimeToJsonConverter implements AttributeConverter<Map<String, LocalTime>, String> {

    private static final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Override
    public String convertToDatabaseColumn(Map<String, LocalTime> attribute) {
        String jsonConverted = null;
        try{
            jsonConverted = objectMapper.writeValueAsString(attribute);
        } catch (final JsonProcessingException e){
            log.error("Json Writing error {}", e);
        }
        return jsonConverted;
    }

    @Override
    public Map<String, LocalTime> convertToEntityAttribute(String dbData) {
        Map<String, LocalTime> attribute = null;
        try{
            attribute = objectMapper.readValue(dbData, new TypeReference<HashMap<String, LocalTime>>(){});
        } catch (final IOException e){
            log.error("Json Reading Error {}", e);
        }
        return attribute;
    }
}
