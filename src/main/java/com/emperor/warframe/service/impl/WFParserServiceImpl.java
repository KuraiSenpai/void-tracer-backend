package com.emperor.warframe.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.emperor.warframe.service.WFParserService;

@Service
public class WFParserServiceImpl implements WFParserService {
    @Autowired
    private RestTemplate restTemplate;

    public String parseWorldStateData() throws Exception {
        String rawData = restTemplate.getForObject("https://api.warframe.com/cdn/worldState.php", String.class);

        File scriptDir = new File("scripts/parser-node");

        ProcessBuilder pb = new ProcessBuilder("node", "parse.js");
        pb.directory(scriptDir);

        pb.redirectErrorStream(true);

        try {
            Process process = pb.start();
            StringBuilder output = new StringBuilder();

            try (OutputStream os = process.getOutputStream()) {
                os.write(rawData.getBytes(StandardCharsets.UTF_8));
                os.flush();
            }

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8));) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println("NODE LOG: " + line);
                    output.append(line);
                }
            }

            int exitCode = process.waitFor();
            System.out.println("Node process exited with code: " + exitCode);

            if (exitCode != 0) {
                throw new Exception("Node Parser Script Failed: " + output.toString());
            }

            String result = output.toString();
            // Cleanup to return the JSON part
            if (result.contains("{")) {
                return result.substring(result.indexOf("{"));
            }

            return result;

        } catch (Exception e) {
            System.err.println("CRITICAL FAILURE in WFParserService: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
}
