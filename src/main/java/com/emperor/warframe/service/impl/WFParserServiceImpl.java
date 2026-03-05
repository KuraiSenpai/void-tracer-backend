package com.emperor.warframe.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.emperor.warframe.service.WFParserService;

@Service
public class WFParserServiceImpl implements WFParserService {
    @Autowired
    private RestTemplate restTemplate;

    @Value("${warframe.dynamic.worldState}")
    private String worldStateApiUrl;

    public String parseWorldStateData() throws Exception {
        String rawData = fetchRawWorldState();

        return executeNodeParser(rawData);
    }

    private String fetchRawWorldState() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("User-Agent", "Warframe/2026.02.20.08.00 (Android; 14; Pixel 8 Pro)");
        headers.set("X-Requested-With", "com.digitalextremes.warframemobile");
        headers.set("Accept", "application/json");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                worldStateApiUrl,
                HttpMethod.GET,
                entity,
                String.class);
        return response.getBody();
    }

    private String executeNodeParser(String inputData) throws Exception {
        ProcessBuilder pb = new ProcessBuilder("node", "parse.js");
        pb.directory(new File("scripts/parser-node"));
        pb.redirectErrorStream(true);

        Process process = pb.start();

        // Write to Node's stdin
        try (OutputStream os = process.getOutputStream()) {
            os.write(inputData.getBytes(StandardCharsets.UTF_8));
            os.flush();
        }

        // Read from Node's stdout
        StringBuilder output = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line);
            }
        }

        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new RuntimeException("Node Parser Failed: " + output.toString());
        }

        return cleanJsonResponse(output.toString());
    }

    private String cleanJsonResponse(String raw) {
        return raw.contains("{") ? raw.substring(raw.indexOf("{")) : raw;
    }
}
