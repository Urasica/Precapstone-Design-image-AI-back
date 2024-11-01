package com.demo.precapstone.service;

import org.springframework.stereotype.Service;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class ColabService {

    private final String colabApiUrl = "https://colab.example.com/generate_image";

    public byte[] generateImage(String prompt) {
        try {
            // Colab 서버에 HTTP 요청을 전송
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(colabApiUrl))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(buildRequestBody(prompt)))
                    .build();

            HttpResponse<byte[]> response = client.send(request, HttpResponse.BodyHandlers.ofByteArray());

            if (response.statusCode() == 200) {
                // 이미지 데이터를 반환 (바이트 배열 형태)
                return response.body();
            } else {
                throw new RuntimeException("Failed to generate image, status code: " + response.statusCode());
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate image", e);
        }
    }

    private String buildRequestBody(String prompt) {
        // Colab 서버에 전달할 JSON 요청 바디를 작성
        return "{"
                + "\"prompt\": \"" + prompt + "\""
                + "}";
    }
}

