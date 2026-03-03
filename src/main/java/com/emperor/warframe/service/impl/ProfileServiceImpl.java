package com.emperor.warframe.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.emperor.warframe.service.ProfileService;
import com.emperor.warframe.service.utils.JsonUtils;

@Service
public class ProfileServiceImpl implements ProfileService {
    @Value("${warframe.profile.url}")
    private String warframeProfileUrl;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public String getProfile(String accountId) {
        return JsonUtils.formatStringAsJson(restTemplate.getForObject(warframeProfileUrl + accountId, String.class));
    }

}
