package com.demo.precapstone.service;

import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Base64;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;

@Service
public class FileStorageService {

    private final String storageDirectory = "src/main/resources/static/upload"; // 이미지가 저장될 디렉터리 경로

    public String storeImage(String base64ImageData) {
        try {
            // base64 문자열을 디코딩하여 바이트 배열로 변환
            byte[] imageBytes = Base64.getDecoder().decode(base64ImageData);

            // 고유한 파일 이름 생성 (예: UUID 사용)
            String fileName = "image_" + System.currentTimeMillis() + ".jpg";
            Path filePath = Paths.get(storageDirectory, fileName);

            // 바이트 배열을 파일로 저장
            Files.createDirectories(filePath.getParent()); // 저장 디렉터리가 없으면 생성
            try (OutputStream os = new FileOutputStream(filePath.toFile())) {
                os.write(imageBytes);
            }

            // 저장된 파일의 경로를 문자열로 반환
            return "/upload/" + fileName;
        } catch (Exception e) {
            throw new RuntimeException("Failed to store image", e);
        }
    }
}
