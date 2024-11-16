package com.demo.precapstone.service;

import com.demo.precapstone.dao.Image;
import com.demo.precapstone.dao.User;
import com.demo.precapstone.dto.ImageResponseDTO;
import com.demo.precapstone.repository.ImageRepository;
import com.demo.precapstone.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ImageRetrievalService {

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private UserRepository userRepository;

    public List<ImageResponseDTO> getRecentImages(String userName) {
        User user = userRepository.findByUserName(userName);
        if (user == null) {
            throw new IllegalArgumentException("User not found: " + userName);
        }

        List<Image> images = imageRepository.findByUserOrderByGenAtDesc(user);

        if (images == null || images.isEmpty()) {
            throw new IllegalArgumentException("No images found for user: " + userName);
        }

        return images.stream()
                .map(image -> new ImageResponseDTO(
                        encodeImageToBase64(image.getImageUrl()),
                        image.getImageUrl(),
                        image.getGenAt()
                ))
                .collect(Collectors.toList());
    }

    private String encodeImageToBase64(String imagePath) {
        try {
            // 절대 경로로 파일 읽기 테스트
            File file = new File("src/main/resources/static" + imagePath);
            byte[] imageBytes = Files.readAllBytes(file.toPath());
            return Base64.getEncoder().encodeToString(imageBytes);
        } catch (IOException e) {
            throw new RuntimeException("Failed to encode image: " + imagePath, e);
        }
    }
}

