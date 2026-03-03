package com.emperor.warframe.service;

import java.util.List;

import com.emperor.warframe.dto.WarframeAlertDto;

public interface AlertsService {
    List<WarframeAlertDto> getAlerts();
}
