package com.emperor.warframe.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.emperor.warframe.dto.WarframeAlertDto;
import com.emperor.warframe.service.AlertsService;
import com.emperor.warframe.service.WorldStateService;

@RestController
@RequestMapping("${api-prefix}/dynamic")
public class DynamicController {
    @Deprecated
    @Autowired
    private AlertsService alertsService;
    @Autowired
    private WorldStateService worldStateService;

    @GetMapping("/worldState")
    public ResponseEntity<String> getWorldState() {
        try {
            String result = worldStateService.getWorldState();
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Parser Error: " + e.getMessage());
        }
    }

    @Deprecated
    @GetMapping("/alerts")
    public List<WarframeAlertDto> getAlerts() {
        return alertsService.getAlerts();
    }
}
