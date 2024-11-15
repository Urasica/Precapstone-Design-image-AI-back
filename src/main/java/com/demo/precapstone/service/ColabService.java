package com.demo.precapstone.service;

import org.springframework.stereotype.Service;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.json.JSONObject;

@Service
public class ColabService {

    private final String colabApiUrl = "http://localhost:5000/generate";

    public String generateImage(String prompt) {
        try {
            // Colab 서버에 HTTP 요청을 전송
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(colabApiUrl))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(buildRequestBody(prompt)))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                JSONObject jsonResponse = new JSONObject(response.body());
                return jsonResponse.getString("image"); // base64 문자열 반환
            } else {
                throw new RuntimeException("Failed to generate image, status code: " + response.statusCode());
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate image", e);
        }
    }

    private String buildRequestBody(String prompt) {
        return "{"
                + "\"prompt\": \"" + prompt + "\","
                + "\"negative_prompt\": \""+ "blurry, low quality, deformed face, distorted features\""
                + "}";
    }
}