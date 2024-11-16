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

    public ImageDTO generateImage(String content, String userName) {
        User user = userRepository.findByUserName(userName);
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        String promptText = openAIService.createPromptFromText(content);
        String imageData = colabService.generateImage(promptText);
        String imageUrl = fileStorageService.storeImage(imageData);

        Image image = new Image();
        image.setImageUrl(imageUrl);
        image.setPrompt(promptText);
        image.setGenAt(LocalDateTime.now());
        image.setUser(user);
        imageRepository.save(image);

        ImageDTO imageDTO = new ImageDTO(imageUrl, imageData);

        return imageDTO;
    }
}
