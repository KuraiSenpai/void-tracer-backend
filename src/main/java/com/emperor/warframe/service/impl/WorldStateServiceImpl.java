package com.emperor.warframe.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.emperor.warframe.cache.WorldStateCacheHandler;
import com.emperor.warframe.exception.WorldStateFetchException;
import com.emperor.warframe.service.WorldStateService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class WorldStateServiceImpl implements WorldStateService {
    private final RestTemplate restTemplate;
    private final WorldStateCacheHandler cacheHandler;

    @Value("${warframe.dynamic.worldstate}")
    private String worldStateApiUrl;

    WorldStateServiceImpl(RestTemplate restTemplate, WorldStateCacheHandler cacheHandler) {
        this.restTemplate = restTemplate;
        this.cacheHandler = cacheHandler;
    }

    @Override
    public String getWorldState() throws WorldStateFetchException {
        if (cacheHandler.isCacheValid()) {
            return cacheHandler.getCachedData();
        }

        synchronized (this) {
            try {
                String worldState = restTemplate.getForObject(worldStateApiUrl, String.class);
                cacheHandler.updateCache(worldState);
            } catch (Exception e) {
                log.warn("Community WorldState API failed! Falling back to cached data: {}", e.getMessage());

                String fallback = cacheHandler.getFallbackData();
                if (fallback != null) {
                    return fallback;
                }

                throw new WorldStateFetchException("Failed to fetch WorldState from community API and no backup cache available", e);
            }
        }

        return cacheHandler.getCachedData();
    }
}
