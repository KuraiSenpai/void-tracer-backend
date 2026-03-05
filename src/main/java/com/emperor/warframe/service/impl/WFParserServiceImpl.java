package com.emperor.warframe.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.emperor.warframe.service.WFParserService;

@Service
public class WFParserServiceImpl implements WFParserService {
    @Value("${warframe.dynamic.worldState}")
    private String worldStateApiUrl;

    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .followRedirects(HttpClient.Redirect.NORMAL)
            .build();

    public String parseWorldStateData() throws Exception {
        String rawData = fetchRawWorldState();

        return executeNodeParser(rawData);
    }

    private String fetchRawWorldState() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(
                        worldStateApiUrl))
                .header("User-Agent",
                        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36")
                .header("Accept",
                        "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8")
                .header("Accept-Language", "en-US,en;q=0.9")
                .header("Sec-Fetch-Dest", "document")
                .header("Sec-Fetch-Mode", "navigate")
                .header("Sec-Fetch-Site", "none")
                .header("Upgrade-Insecure-Requests", "1")
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 403) {
            throw new RuntimeException("Official API blocked the Koyeb IP address. Anti-bot trigger.");
        }

        return response.body();
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
