package com.emperor.warframe.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.emperor.warframe.service.ProfileService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("${apiPrefix}/profile")
public class ProfileController {
    @Autowired
    private ProfileService profileService;

    // TODO: add daily rate limit for call !IMPORTANT
    @GetMapping()
    public String getProfile(@RequestParam String accountId) {
        return profileService.getProfile(accountId);
    }
}
