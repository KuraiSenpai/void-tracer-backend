package com.emperor.warframe.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("${apiPrefix}/base")
public class BaseController {
    private static final String MESSAGE = "Welcome, operator.";

    @GetMapping
    String getBase() {
        return MESSAGE;
    }

    @GetMapping("/test-ip")
    public String getMyIp() {
        // This fetches your own outbound IP from a 3rd party service
        return new RestTemplate().getForObject("https://api.ipify.org", String.class);
    }
}
