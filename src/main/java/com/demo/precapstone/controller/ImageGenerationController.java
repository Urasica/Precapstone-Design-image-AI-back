package com.demo.precapstone.controller;

import com.demo.precapstone.dto.ImageDTO;
import com.demo.precapstone.service.ImageGenerationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/images")
public class ImageGenerationController {

    @Autowired
    private ImageGenerationService imageGenerationService;

    @PostMapping("/generate")
    public ResponseEntity<ImageDTO> generateImage(@RequestParam String userName, @RequestBody String message) {
        ImageDTO imageDTO = imageGenerationService.generateImage(message, userName);
        return ResponseEntity.ok(imageDTO);
    }

}
