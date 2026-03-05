package com.emperor.warframe.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.emperor.warframe.service.WFParserService;

@Service
public class WFParserServiceImpl implements WFParserService {
    @Autowired
    private RestTemplate restTemplate;

    @Value("${warframe.dynamic.worldstate}")
    private String worldStateApiUrl;

    public String fetchWorldState() throws Exception {
        return restTemplate.getForObject(worldStateApiUrl, String.class);
    }
}
