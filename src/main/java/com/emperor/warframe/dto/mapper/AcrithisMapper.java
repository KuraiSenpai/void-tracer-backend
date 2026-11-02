package com.emperor.warframe.dto.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

import com.emperor.warframe.dto.AcrithisRotationDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class AcrithisMapper {
    private static final String VALUE_START_NAME = "AcrithisObserved";
    private static final String ITEM_NAME = "AcrithisItem";
    private static final int ITEM_COUNT = 5;

    private final ObjectMapper objectMapper;

    public AcrithisMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public AcrithisRotationDto mapToDto(String jsonResponse) throws Exception {
        if (jsonResponse == null || jsonResponse.isBlank()) {
            return new AcrithisRotationDto(null, List.of());
        }

        JsonNode rootNode = objectMapper.readTree(jsonResponse);
        String wikitext = rootNode.path("parse")
                .path("wikitext")
                .path("*")
                .asText();

        String observedDate = extractValue(wikitext, VALUE_START_NAME);
        List<String> items = new ArrayList<>();

        for (int i = 1; i <= ITEM_COUNT; i++) {
            String item = extractValue(wikitext, ITEM_NAME + i);
            if (item != null) {
                items.add(item);
            }
        }

        return new AcrithisRotationDto(observedDate, items);
    }

    private String extractValue(String text, String variableName) {
        Pattern pattern = Pattern.compile("\\{\\{#vardefine:" + variableName + "\\|(.*?)\\}\\}");
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            return matcher.group(1).trim();
        }
        return null;
    }
}
