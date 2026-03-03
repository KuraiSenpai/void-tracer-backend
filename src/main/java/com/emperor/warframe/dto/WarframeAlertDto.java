package com.emperor.warframe.dto;

import java.util.Date;

public record WarframeAlertDto(
                String guid,
                String title,
                String author,
                String description,
                String faction,
                Date publishedDate,
                String expiry) {
}
