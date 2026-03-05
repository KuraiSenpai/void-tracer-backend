package com.emperor.warframe.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.emperor.warframe.service.WFParserService;
import com.emperor.warframe.service.WorldStateService;

@Service
public class WorldStateServiceImpl implements WorldStateService {
    @Autowired
    private WFParserService parserService;

    @Override
    public String getWorldState() throws Exception {
        return parserService.fetchWorldState();
    }
}
