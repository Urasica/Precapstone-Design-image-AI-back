package com.demo.precapstone.service;

import com.demo.precapstone.dao.Image;
import com.demo.precapstone.dao.User;
import com.demo.precapstone.dto.ImageDTO;
import com.demo.precapstone.dto.ImageRequestDTO;
import com.demo.precapstone.repository.ImageRepository;
import com.demo.precapstone.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;

@Service
public class ImageGenerationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private OpenAIService openAIService;

    @Autowired
    private ColabService colabService;

    @Autowired
    private FileStorageService fileStorageService;

    public ImageDTO generateImage(ImageRequestDTO imageRequestDTO) throws IOException {

        User user = userRepository.findByUserName(imageRequestDTO.getUserName());
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        String promptText = openAIService.createPromptFromText(imageRequestDTO.getMessage(), imageRequestDTO.getPrompt());
        String imageData = colabService.generateImage(promptText);
        String imageUrl = fileStorageService.storeImage(imageData);

        Image image = new Image(imageUrl, promptText, LocalDateTime.now(), user);
        imageRepository.save(image);

        ImageDTO imageDTO = new ImageDTO(imageUrl, imageData);

        return imageDTO;
    }
}
