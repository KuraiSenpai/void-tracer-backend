package com.emperor.warframe.service.impl;

import org.springframework.stereotype.Service;

import com.emperor.warframe.entity.WorldStateCache;
import com.emperor.warframe.repo.WorldStateCacheRepository;
import com.emperor.warframe.service.WorldStateCacheService;

@Service
public class WorldStateCacheServiceImpl implements WorldStateCacheService {
    private final WorldStateCacheRepository repo;
    private final Long SINGLE_ID = 1L;

    public WorldStateCacheServiceImpl(WorldStateCacheRepository repository) {
        this.repo = repository;
    }

    public WorldStateCache get() {
        return repo.findById(SINGLE_ID)
                .orElse(new WorldStateCache(SINGLE_ID, "{}"));
    }

    public String getJsonData() {
        return repo.findById(SINGLE_ID)
                .map(WorldStateCache::getJsonData)
                .orElse("");
    }

    public void saveWorldStateCache(String json) {
        repo.save(new WorldStateCache(SINGLE_ID, json));
    }
}
