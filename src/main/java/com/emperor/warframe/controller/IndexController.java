package com.emperor.warframe.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.emperor.warframe.service.IndexService;

@RestController
@RequestMapping("${api-prefix}/index")
public class IndexController {
    final IndexService indexService;

    IndexController(IndexService indexService) {
        this.indexService = indexService;
    }

    @GetMapping()
    public String getIndex() {
        return indexService.getIndex() ? "Index successfully downloaded." : "Failed to download Index.";
    }

}
