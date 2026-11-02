package com.emperor.warframe.cache;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicReference;

import org.springframework.stereotype.Component;

import com.emperor.warframe.service.WorldStateCacheService;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class WorldStateCacheHandler {
    private final WorldStateCacheService cacheService;

    private final AtomicReference<String> cachedWorldState = new AtomicReference<>();
    private final AtomicReference<Instant> lastFetchTime = new AtomicReference<>(Instant.EPOCH);

    private static final long CACHE_TTL_SECONDS = 60;

    public WorldStateCacheHandler(WorldStateCacheService cacheService) {
        this.cacheService = cacheService;
    }

    @PostConstruct
    public void initCacheFromDb() {
        String loadedData = cacheService.getJsonData();

        if (loadedData != null && !loadedData.isEmpty()) {
            cachedWorldState.set(loadedData);
            log.info("Cached WorldState loaded into memory.");
        } else {
            log.info("No cached WorldState found in DB on startup.");
        }

    }

    public boolean isCacheValid() {
        if (cachedWorldState.get() == null) {
            return false;
        }
        Instant expirationTime = lastFetchTime.get().plusSeconds(CACHE_TTL_SECONDS);
        return Instant.now().isBefore(expirationTime);
    }

    public String getCachedData() {
        return cachedWorldState.get();
    }

    public void updateCache(String newData) {
        if (newData != null && !newData.isEmpty()) {
            cachedWorldState.set(newData);
            lastFetchTime.set(Instant.now());
            cacheService.saveWorldStateCache(newData);
            log.debug("Updated WorldState cache and refreshed DB backup.");
        }
    }

    public String getFallbackData() {
        if (cachedWorldState.get() != null) {
            return cachedWorldState.get();
        }

        // Deep DB fallback if memory got cleared somehow
        String dbFallback = cacheService.getJsonData();
        if (dbFallback != null) {
            cachedWorldState.set(dbFallback);
            return dbFallback;
        }

        return null;
    }
}
