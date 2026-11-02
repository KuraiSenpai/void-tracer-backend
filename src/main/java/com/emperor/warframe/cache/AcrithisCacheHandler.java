package com.emperor.warframe.cache;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.IsoFields;
import java.util.concurrent.atomic.AtomicReference;

import org.springframework.stereotype.Component;

import com.emperor.warframe.dto.AcrithisRotationDto;

@Component
public class AcrithisCacheHandler {
    private final AtomicReference<AcrithisRotationDto> cachedOffering = new AtomicReference<>();
    private final AtomicReference<String> cachedWeekKey = new AtomicReference<>();

    public String getCurrentWeekKey() {
        LocalDate now = LocalDate.now(ZoneId.of("UTC"));
        int weekNumber = now.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
        int yearNumber = now.get(IsoFields.WEEK_BASED_YEAR);
        return yearNumber + "-W" + weekNumber;
    }

    public boolean isCacheValid(String currentWeekKey) {
        return cachedOffering.get() != null && currentWeekKey.equals(cachedWeekKey.get());
    }

    public AcrithisRotationDto getCachedData() {
        return cachedOffering.get();
    }

    public void updateCache(AcrithisRotationDto newData, String weekKey) {
        cachedOffering.set(newData);
        cachedWeekKey.set(weekKey);
    }
}
