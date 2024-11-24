package com.demo.precapstone.controller;

import com.demo.precapstone.dto.ImageDTO;
import com.demo.precapstone.dto.ImageRequestDTO;
import com.demo.precapstone.service.ImageGenerationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/images")
public class ImageGenerationController {

    @Autowired
    private ImageGenerationService imageGenerationService;

    @PostMapping("/generate")
    public ResponseEntity<ImageDTO> generateImage(@RequestBody ImageRequestDTO imageRequestDTO) throws IOException {
        ImageDTO imageDTO = imageGenerationService.generateImage(imageRequestDTO);
        return ResponseEntity.ok(imageDTO);
    }

}
