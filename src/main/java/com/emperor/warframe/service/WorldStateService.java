package com.emperor.warframe.service;

import com.emperor.warframe.exception.WorldStateFetchException;

public interface WorldStateService {
    String getWorldState() throws WorldStateFetchException;
}
