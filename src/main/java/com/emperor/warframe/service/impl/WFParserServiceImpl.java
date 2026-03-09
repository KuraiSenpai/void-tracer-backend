package com.emperor.warframe.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.emperor.warframe.service.WFParserService;
import com.emperor.warframe.service.WorldStateCacheService;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class WFParserServiceImpl implements WFParserService {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private WorldStateCacheService cacheService;

    @Value("${warframe.dynamic.worldstate}")
    private String worldStateApiUrl;

    private String cachedWorldState = "";

    @PostConstruct
    public void initCacheFromDb() {
        this.cachedWorldState = cacheService.getJsonData();

        if (!cachedWorldState.isEmpty()) {
            log.info("Cached WorldState loaded into memory!");
        }
    }

    public String fetchWorldState() throws Exception {
        String worldState = restTemplate.getForObject(worldStateApiUrl, String.class);

        if (worldState != null) {
            if (!worldState.isEmpty() && !worldState.equals(cachedWorldState)) {
                this.cachedWorldState = worldState;
                cacheService.saveWorldStateCache(worldState);
                log.debug("Updated WorldState Cache");
            }
        }

        return cachedWorldState;
    }
}
