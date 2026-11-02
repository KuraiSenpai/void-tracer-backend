package com.emperor.warframe.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.emperor.warframe.service.ProfileService;

@RestController
@RequestMapping("${api-prefix}/profile")
public class ProfileController {
    private final ProfileService profileService;

    ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    // TODO: add daily rate limit for call !IMPORTANT
    @GetMapping()
    public String getProfile(@RequestParam String accountId) {
        return profileService.getProfile(accountId);
    }
}
