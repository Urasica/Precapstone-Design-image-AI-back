package com.demo.precapstone.service;

import com.demo.precapstone.config.ConfigLoaderPpurio;
import com.demo.precapstone.dto.MessageDTO;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.Base64;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class MessageService {
    private static final Integer TIME_OUT = 5000;
    private static final String PPURIO_ACCOUNT = "dmswnd655";
    private static String from = "01034385841";
    private static String file_path = "/upload/watch.jpg";
    private static final String URI = "https://message.ppurio.com";
    private static String content;
    private static String to;

    private RequestService requestService = new RequestService();

    // sendMessage 메서드: 토큰 발급 후 메시지 전송
    public void sendMessage(MessageDTO messageDTO) {
        String basicAuthorization = ConfigLoaderPpurio.getBasicAuthorization();

        file_path= "src/main/resources/static" + messageDTO.getPath();
        from=messageDTO.getFromPhoneNumber();
        to=messageDTO.getToPhoneNumber();
        content=messageDTO.getContent();

        // 토큰 발급
        Map<String, Object> tokenResponse = requestService.getToken(basicAuthorization);
        String accessToken = (String) tokenResponse.get("token");

        // 메시지 전송
        Map<String, Object> sendResponse = requestService.send(URI, accessToken);

        // 응답 출력
        System.out.println(sendResponse.toString());
    }

    // 내부 클래스: RequestService
    public static class RequestService {
        private static final Integer TIME_OUT = 5000;

        // 토큰 발급 요청
        public Map<String, Object> getToken(String basicAuthorization) {
            HttpURLConnection conn = null;
            try {
                // 요청 파라미터 생성
                Request request = new Request(URI + "/v1/token", "Basic " + basicAuthorization);

                // 요청 객체 생성
                conn = createConnection(request);

                // 응답 코드 확인
                int responseCode = conn.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    // 응답 코드가 200인 경우 (성공)
                    System.out.println("토큰 발급 성공. 응답 코드: " + responseCode);
                    return getResponseBody(conn); // 응답 데이터 변환
                } else {
                    // 200이 아닌 응답 코드일 경우
                    System.err.println("토큰 발급 실패. 응답 코드: " + responseCode);
                    Map<String, Object> errorResponse = getResponseBody(conn); // 오류 응답 처리
                    throw new RuntimeException("API 요청 실패: " + errorResponse);
                }
            } catch (IOException e) {
                throw new RuntimeException("API 요청과 응답 실패", e);
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
            }
        }


        // 메시지 전송 요청
        public Map<String, Object> send(String baseUri, String accessToken) {
            HttpURLConnection conn = null;
            try {
                // 요청 파라미터 생성
                String bearerAuthorization = String.format("%s %s", "Bearer", accessToken);
                Request request = new Request(baseUri + "/v1/message", bearerAuthorization);

                // 요청 객체 생성 및 SMS 발송 테스트
                conn = createConnection(request, createSendParams(content, to));

                // 응답 데이터 객체 변환
                return getResponseBody(conn);
            } catch (IOException e) {
                throw new RuntimeException("API 요청과 응답 실패", e);
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
            }
        }

        // 요청 객체 생성
        private <T> HttpURLConnection createConnection(Request request, T requestObject) throws IOException {
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonInputString = objectMapper.writeValueAsString(requestObject);
            // 요청 객체 생성
            HttpURLConnection connect = createConnection(request);
            connect.setDoOutput(true); // URL 연결을 출력용으로 사용(true)
            // 요청 데이터 처리
            try (OutputStream os = connect.getOutputStream()) {
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return connect;
        }

        // 기본 요청 객체 생성
        private HttpURLConnection createConnection(Request request) throws IOException {
            URL url = new URL(request.getRequestUri());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Authorization", request.getAuthorization()); // Authorization 헤더 입력
            conn.setConnectTimeout(TIME_OUT); // 연결 타임아웃 설정(5초)
            conn.setReadTimeout(TIME_OUT); // 읽기 타임아웃 설정(5초)
            return conn;
        }

        // 응답 처리
        private Map<String, Object> getResponseBody(HttpURLConnection conn) throws IOException {
            InputStream inputStream;
            if (conn.getResponseCode() == 200) { // 요청 성공
                inputStream = conn.getInputStream();
            } else { // 실패
                inputStream = conn.getErrorStream();
            }

            try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                String inputLine;
                StringBuilder responseBody = new StringBuilder();
                while ((inputLine = br.readLine()) != null) {
                    responseBody.append(inputLine);
                }

                // JSON 문자열을 Map으로 변환
                return convertJsonToMap(responseBody.toString());
            }
        }

        private Map<String, Object> convertJsonToMap(String jsonString) throws JsonProcessingException {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(jsonString, new TypeReference<>() {});
        }

        // SMS 발송 테스트 파라미터 생성
        private Map<String, Object> createSendParams(String content, String to) throws IOException {
            HashMap<String, Object> params = new HashMap<>();
            params.put("account", PPURIO_ACCOUNT);
            params.put("messageType", "MMS");
            params.put("from", from);
            params.put("content", content);
            params.put("duplicateFlag", "Y");
            //params.put("rejectType", "AD");
            params.put("targetCount", 1);
            params.put("targets", List.of(
                    Map.of("to", to,
                            "name", "tester",
                            "changeWord", Map.of("var1", "ppurio api world test others"))));
            params.put("files", List.of(createFileTestParams(file_path)));
            params.put("refKey", RandomStringUtils.random(32, true, true));
            return params;
        }

        // 파일 정보 생성
        private Map<String, Object> createFileTestParams(String filePath) throws IOException {
            FileInputStream fileInputStream = null;
            try {
                File file = new File(filePath);
                byte[] fileBytes = new byte[(int) file.length()];
                fileInputStream = new FileInputStream(file);
                int readBytes = fileInputStream.read(fileBytes);

                if (readBytes != file.length()) {
                    throw new IOException();
                }

                String encodedFileData = Base64.getEncoder().encodeToString(fileBytes);

                HashMap<String, Object> params = new HashMap<>();
                params.put("size", file.length());
                params.put("name", file.getName());
                params.put("data", encodedFileData);
                return params;
            } finally {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
            }
        }
    }
}

// Request 클래스
class Request {
    private String requestUri;
    private String authorization;

    public Request(String requestUri, String authorization) {
        this.requestUri = requestUri;
        this.authorization = authorization;
    }

    public String getRequestUri() {
        return requestUri;
    }

    public String getAuthorization() {
        return authorization;
    }
}

