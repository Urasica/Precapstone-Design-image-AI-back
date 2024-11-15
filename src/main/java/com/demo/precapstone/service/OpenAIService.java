package com.demo.precapstone.service;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class OpenAIService {
    public String createPromptFromText(String koreanText) {
        List<String> command = buildCommand(koreanText);

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
        String pythonScript = "src/main/java/com/demo/precapstone/service/openai/prompt.py"; // 상대 경로 사용

        if (os.contains("win")) {
            command.add("cmd.exe");
            command.add("/c");
            command.add("python");
        } else {
            command.add("python3"); // Linux/Mac에서는 보통 python3를 사용
        }

        command.add(pythonScript);
        command.add(koreanText);

        return command;
    }
}