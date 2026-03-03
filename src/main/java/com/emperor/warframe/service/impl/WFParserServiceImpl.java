package com.emperor.warframe.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import org.springframework.stereotype.Service;

import com.emperor.warframe.service.WFParserService;

@Service
public class WFParserServiceImpl implements WFParserService {

    public String parseWorldStateData() throws Exception {
        File scriptDir = new File("scripts/parser-node");

        ProcessBuilder pb = new ProcessBuilder("node", "parse.js");
        pb.directory(scriptDir);

        pb.redirectErrorStream(true);

        Process process = pb.start();

        StringBuilder output = new StringBuilder();
        StringBuilder errorOutput = new StringBuilder();

        try (
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8));
                BufferedReader errReader = new BufferedReader(
                        new InputStreamReader(process.getErrorStream(), StandardCharsets.UTF_8));

        ) {
            String line;
            while ((line = reader.readLine()) != null)
                output.append(line);

            while ((line = errReader.readLine()) != null)
                errorOutput.append(line);
        }

        process.waitFor();

        if (process.exitValue() != 0) {
            throw new Exception("Node Parser Script Failed: " + errorOutput.toString());
        }

        String result = output.toString();
        // Cleanup to return the JSON part
        if (result.contains("{")) {
            return result.substring(result.indexOf("{"));
        }

        return result;
    }
}
