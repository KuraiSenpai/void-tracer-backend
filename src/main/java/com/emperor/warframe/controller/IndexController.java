package com.emperor.warframe.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.emperor.warframe.service.IndexService;

import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("${apiPrefix}/index")
public class IndexController {
    @Autowired
    IndexService indexService;

    @GetMapping()
    public String getIndex() {
        return indexService.getIndex() ? "Index successfully downloaded." : "Failed to download Index.";
    }

}
