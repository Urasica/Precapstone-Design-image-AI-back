package com.demo.precapstone.service;

import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.LinkedList;
import java.util.Queue;

import org.json.JSONObject;

@Service
public class ColabService {

    private final Queue<String> colabApiUrls = new LinkedList<>();

    public ColabService() {
        // 초기 서버 주소를 설정 (컨트롤러에서 추가 가능)
    }

    public synchronized void addServerUrl(String serverUrl) {
        serverUrl = serverUrl.replace("\"", "").trim();
        colabApiUrls.add(serverUrl);
        System.out.println("Server URL added: " + serverUrl);
    }

    public synchronized boolean removeServerUrl(String serverUrl) {
        return colabApiUrls.remove(serverUrl);
    }

    public String generateImage(String prompt) {
        System.out.println("Prompt: " + prompt);

        String currentServerUrl = getNextServerUrl();
        if (currentServerUrl == null) {
            throw new RuntimeException("No available servers to process the request.");
        }

        try {
            // Colab 서버에 HTTP 요청을 전송
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(currentServerUrl))
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

    private synchronized String getNextServerUrl() {
        // 현재 서버 주소를 꺼내고 순환 구조로 다시 추가
        if (colabApiUrls.isEmpty()) {
            return null;
        }
        String url = colabApiUrls.poll();
        colabApiUrls.add(url);
        return url;
    }

    private String buildRequestBody(String prompt) {
        return "{"
                + "\"prompt\": \"" + prompt + "\","
                + "\"negative_prompt\": \"" + "blurry, low quality, deformed face, distorted features\""
                + "}";
    }
}
