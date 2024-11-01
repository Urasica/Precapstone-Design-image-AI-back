package com.demo.precapstone.service;

import org.springframework.stereotype.Service;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileStorageService {

    private final Path rootLocation = Paths.get("uploads");

    // 생성자를 이용한 초기화
    public FileStorageService() {
        try {
            Files.createDirectories(rootLocation);  // 파일 저장 경로가 없을 경우 생성
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage", e);
        }
    }

    public String storeImage(byte[] imageData) {
        try {
            // 파일명 생성 (UUID)
            String fileName = UUID.randomUUID().toString() + ".png";
            Path destinationFile = rootLocation.resolve(Paths.get(fileName))
                    .normalize().toAbsolutePath();

            // 이미지 데이터를 로컬 파일로 저장
            Files.write(destinationFile, imageData);

            // 이미지 URL 반환 (예: /uploads/{fileName})
            return "/uploads/" + fileName;
        } catch (IOException e) {
            throw new RuntimeException("Failed to store image", e);
        }
    }

    public Path getImagePath(String fileName) {
        return rootLocation.resolve(fileName).normalize().toAbsolutePath();
    }
}