package com.emperor.warframe.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.emperor.warframe.dto.AcrithisRotationDto;
import com.emperor.warframe.exception.AcrithisFetchException;
import com.emperor.warframe.exception.WorldStateFetchException;
import com.emperor.warframe.service.WikiService;
import com.emperor.warframe.service.WorldStateService;

@RestController
@RequestMapping("${api-prefix}")
public class DynamicController {
    private final WorldStateService worldStateService;
    private final WikiService wikiService;

    DynamicController(WorldStateService worldStateService, WikiService wikiService) {
        this.worldStateService = worldStateService;
        this.wikiService = wikiService;
    }

    @GetMapping("/worldState")
    public ResponseEntity<String> getWorldState() throws WorldStateFetchException {
        String result = worldStateService.getWorldState();
        return ResponseEntity.ok(result);

    }

    @GetMapping("/acrithis")
    public ResponseEntity<AcrithisRotationDto> getAcrithis() throws AcrithisFetchException {
        AcrithisRotationDto result = wikiService.getWeeklyAcrithisOfferings();
        return ResponseEntity.ok(result);
    }
}
