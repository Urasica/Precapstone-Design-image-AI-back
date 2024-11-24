package com.demo.precapstone.service;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class OpenAIService {

    public String createPromptFromText(String message, String prompt) {
        List<String> command = buildCommand(message + prompt);

        try {
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();

            StringBuilder output = new StringBuilder();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                }
            }

            int exitCode = process.waitFor();
            if (exitCode != 0) {
                return "Error occurred while executing the Python script: " + output.toString().trim();
            }

            return output.toString().trim(); // 결과 반환

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return "An error occurred: " + e.getMessage();
        }
    }

    private List<String> buildCommand(String koreanText) {
        List<String> command = new ArrayList<>();
        String os = System.getProperty("os.name").toLowerCase();
        String pythonScript;

        if (os.contains("win")) {
            pythonScript = "src/main/resources/python/openai/prompt.py";
            command.add("cmd.exe");
            command.add("/c");
            command.add("python");
        } else {
            pythonScript = "/home/ubuntu/pre/project/src/main/resources/python/openai/prompt.py";
            command.add("python3");
        }

        command.add(pythonScript);
        command.add(koreanText);

        return command;
    }
}