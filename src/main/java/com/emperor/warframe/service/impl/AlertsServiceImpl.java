package com.emperor.warframe.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.emperor.warframe.dto.WarframeAlertDto;
import com.emperor.warframe.dto.mapper.WarframeAlertMapper;
import com.emperor.warframe.service.AlertsService;
import com.emperor.warframe.service.utils.RSSFeedReader;

@Deprecated
@Service
public class AlertsServiceImpl implements AlertsService {
    @Value("${warframe.dynamic.alerts}")
    private String warframeDynamicAlertsUrl;

    @Deprecated
    @Override
    public List<WarframeAlertDto> getAlerts() {
        return RSSFeedReader.readFeed(warframeDynamicAlertsUrl)
                .stream()
                .map(WarframeAlertMapper::mapToDto)
                .toList();
    }

}
