package com.emperor.warframe.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.emperor.warframe.cache.AcrithisCacheHandler;
import com.emperor.warframe.dto.AcrithisRotationDto;
import com.emperor.warframe.dto.mapper.AcrithisMapper;
import com.emperor.warframe.exception.AcrithisFetchException;
import com.emperor.warframe.service.WikiService;

@Service
public class WikiServiceImpl implements WikiService {
    @Value("${warframe.wiki.url}")
    private String warframeWikiUrl;

    private final RestTemplate restTemplate;
    private final AcrithisMapper acrithisMapper;
    private final AcrithisCacheHandler cacheHandler;

    WikiServiceImpl(RestTemplate restTemplate, AcrithisMapper acrithisMapper, AcrithisCacheHandler cacheHandler) {
        this.restTemplate = restTemplate;
        this.acrithisMapper = acrithisMapper;
        this.cacheHandler = cacheHandler;
    }

    @Override
    public AcrithisRotationDto getWeeklyAcrithisOfferings() throws AcrithisFetchException {
        String currentWeekKey = cacheHandler.getCurrentWeekKey();

        if (cacheHandler.isCacheValid(currentWeekKey)) {
            return cacheHandler.getCachedData();
        }

        return fetchAndCacheFreshOfferings(currentWeekKey);
    }

    private synchronized AcrithisRotationDto fetchAndCacheFreshOfferings(String currentWeekKey) {
        if (cacheHandler.isCacheValid(currentWeekKey)) {
            return cacheHandler.getCachedData();
        }

        try {
            String targetUrl = UriComponentsBuilder.fromUriString(
                    warframeWikiUrl)
                    .queryParam("action", "parse")
                    .queryParam("page", "Acrithis/Current_Offerings")
                    .queryParam("prop", "wikitext")
                    .queryParam("format", "json")
                    .toUriString();

            String rawJson = restTemplate.getForObject(targetUrl, String.class);
            AcrithisRotationDto freshData = acrithisMapper.mapToDto(rawJson);

            cacheHandler.updateCache(freshData, currentWeekKey);

            return freshData;
        } catch (Exception e) {
            // Fallback to stale cache if the wiki is down on a new week
            if (cacheHandler.getCachedData() != null) {
                return cacheHandler.getCachedData();
            }
            throw new AcrithisFetchException("Error retrieving Acrithis current wares from MediaWiki API", e);
        }
    }
}
