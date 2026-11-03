package com.emperor.warframe.dto.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

import com.emperor.warframe.dto.AcrithisItemDto;
import com.emperor.warframe.dto.AcrithisRotationDto;
import com.emperor.warframe.service.utils.AcrithisCostRegistry;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class AcrithisMapper {
    private static final String ACRITHIS_START_NAME = "AcrithisObserved";
    private static final String ACRITHIS_ITEM_NAME = "AcrithisItem";
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

        String observedDate = extractValue(wikitext, ACRITHIS_START_NAME);
        List<AcrithisItemDto> items = new ArrayList<>();

        for (int i = 1; i <= ITEM_COUNT; i++) {
            String itemName = extractValue(wikitext, ACRITHIS_ITEM_NAME + i);
            if (itemName != null) {
                int cost = AcrithisCostRegistry.getCost(itemName);
                items.add(new AcrithisItemDto(itemName, cost));
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
