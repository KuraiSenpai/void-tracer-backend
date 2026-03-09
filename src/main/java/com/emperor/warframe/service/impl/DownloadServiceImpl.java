package com.emperor.warframe.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.emperor.warframe.service.DownloadService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DownloadServiceImpl implements DownloadService {
    @Autowired
    private RestTemplate restTemplate;

    @Override
    public boolean downloadIndex(String indexUrl, String warframeIndexFileName, String warframeIndexDownloadPath) {
        byte[] indexBytes = restTemplate.getForObject(indexUrl, byte[].class);
        return saveToResources(indexBytes, warframeIndexFileName, warframeIndexDownloadPath);
    }

    private boolean saveToResources(byte[] indexBytes, String fileName, String downloadPath) {
        if (indexBytes == null || indexBytes.length == 0) {
            log.error("Failed to save file '{}': Byte array is empty or null.", fileName);
            return false;
        }

        Path indexPath = Paths.get(downloadPath + fileName);

        try {
            Files.createDirectories(indexPath.getParent());

            Files.write(indexPath, indexBytes);

            log.info("Successfully saved file to resources: {}", indexPath.toAbsolutePath());
        } catch (IOException e) {
            log.error("IOException occurred while saving file '{}' to disk: {}", fileName, e.getMessage());
        }
        return true;
    }

}
