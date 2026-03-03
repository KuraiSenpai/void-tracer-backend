package com.emperor.warframe.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.emperor.warframe.service.WFParserService;
import com.emperor.warframe.service.WorldStateService;

@Service
public class WorldStateServiceImpl implements WorldStateService {
    @Value("${warframe.dynamic.worldState}")
    private String worldStateApiUrl;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private WFParserService parserService;

    @Override
    public String getWorldState() throws Exception {
        String rawJson = restTemplate.getForObject(worldStateApiUrl, String.class);
        return parserService.parseWorldStateData(rawJson);
    }
}
