package com.demo.precapstone.service;

import com.demo.precapstone.dao.Image;
import com.demo.precapstone.dao.User;
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

    public Image generateImage(String content, String phoneNumber) {
        User user = userRepository.findByPhoneNumber(phoneNumber);
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        String promptText = openAIService.createPromptFromText(content);
        byte[] imageData = colabService.generateImage(promptText);
        String imageUrl = fileStorageService.storeImage(imageData);

        Image image = new Image();
        image.setImageUrl(imageUrl);
        image.setPrompt(promptText);
        image.setGenAt(LocalDateTime.now());
        image.setUser(user);

        return imageRepository.save(image);
    }
}
