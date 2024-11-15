package com.demo.precapstone.service;

import com.demo.precapstone.dao.Image;
import com.demo.precapstone.dao.User;
import com.demo.precapstone.dto.ImageDTO;
import com.demo.precapstone.repository.ImageRepository;
import com.demo.precapstone.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ImageGenerationServiceDemo {

    @Autowired
    private OpenAIService openAIService;

    @Autowired
    private ColabService colabService;

    @Autowired
    private FileStorageService fileStorageService;

    public ImageDTO generateImage(String content) {
        String promptText = openAIService.createPromptFromText(content);
        String imageData = colabService.generateImage(promptText);
        String imageUrl = fileStorageService.storeImage(imageData);

        ImageDTO imageDTO = new ImageDTO(imageUrl, imageData);

        return imageDTO;
    }
}
