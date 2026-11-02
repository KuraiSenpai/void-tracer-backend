package com.emperor.warframe.service;

import com.emperor.warframe.dto.AcrithisRotationDto;
import com.emperor.warframe.exception.AcrithisFetchException;

public interface WikiService {
    AcrithisRotationDto getWeeklyAcrithisOfferings() throws AcrithisFetchException;
}
