package com.emperor.warframe.service;

public interface DownloadService {
    boolean downloadIndex(String indexUrl, String warframeIndexFileName, String warframeIndexDownloadPath);
}
