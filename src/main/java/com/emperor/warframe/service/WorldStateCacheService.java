package com.emperor.warframe.service;

import com.emperor.warframe.entity.WorldStateCache;

public interface WorldStateCacheService {
    public WorldStateCache get();

    public String getJsonData();

    public void saveWorldStateCache(String json);
}
