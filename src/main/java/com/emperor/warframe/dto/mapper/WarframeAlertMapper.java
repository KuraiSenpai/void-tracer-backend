package com.emperor.warframe.dto.mapper;

import org.jdom2.Element;

import com.emperor.warframe.dto.WarframeAlertDto;
import com.rometools.rome.feed.synd.SyndEntry;

public class WarframeAlertMapper {
    public static WarframeAlertDto mapToDto(SyndEntry entry) {
        return new WarframeAlertDto(
                entry.getUri(),
                entry.getTitle(),
                entry.getAuthor(),
                entry.getDescription() != null ? entry.getDescription().getValue() : "",
                getForeignValue(entry, "faction"),
                entry.getPublishedDate(),
                getForeignValue(entry, "expiry"));
    }

    private static String getForeignValue(SyndEntry entry, String tagName) {
        return entry.getForeignMarkup().stream()
                .filter(e -> e.getName().equals(tagName))
                .map(Element::getValue)
                .findFirst()
                .orElse("Unknown");
    }

}
