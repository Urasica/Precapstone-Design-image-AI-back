package com.demo.precapstone.service;

import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.json.JSONObject;

@Service
public class ColabService {

    private final Queue<String> colabApiUrls = new ConcurrentLinkedQueue<>();
    private final Set<String> urlSet = new LinkedHashSet<>(); // 중복 방지를 위한 추가 자료구조

    public synchronized void addServerUrl(String serverUrl) {
        serverUrl = serverUrl.replace("\"", "").trim();
        if (urlSet.add(serverUrl)) { // 중복 방지
            colabApiUrls.add(serverUrl);
            System.out.println("Server URL added: " + serverUrl);
        } else {
            System.out.println("Server URL already exists: " + serverUrl);
        }
    }

    public synchronized boolean removeServerUrl(String serverUrl) {
        if (urlSet.remove(serverUrl)) {
            colabApiUrls.remove(serverUrl);
            System.out.println("Server URL removed: " + serverUrl);
            return true;
        }
        System.out.println("Server URL not found: " + serverUrl);
        return false;
    }

    public synchronized List<String> getServerUrls() {
        return new ArrayList<>(urlSet); // 변경 방지를 위해 복사본 반환
    }

    public String generateImage(String prompt) {
        System.out.println("Prompt: " + prompt);

        String currentServerUrl = getNextServerUrl();
        if (currentServerUrl == null) {
            throw new RuntimeException("No available servers to process the request.");
        }

        try {
            // Colab 서버에 HTTP 요청 전송
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
            throw new RuntimeException("No server URLs available.");
        }
        String url = colabApiUrls.poll();
        colabApiUrls.add(url);
        System.out.println("Selected server URL: " + url);
        return url;
    }

    private String buildRequestBody(String prompt) {
        return "{"
                + "\"prompt\": \"" + prompt + "\","
                + "\"negative_prompt\": \"blurry, low quality, deformed face, distorted features\""
                + "}";
    }
}