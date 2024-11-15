package com.demo.precapstone.controller;

import com.demo.precapstone.dao.Image;
import com.demo.precapstone.dto.ImageResponseDTO;
import com.demo.precapstone.service.ImageRetrievalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/images")
public class ImageRetrievalController {

    @Autowired
    private ImageRetrievalService imageRetrievalService;

//    @GetMapping("/recent")
//    public ResponseEntity<List<ImageResponseDTO>> getRecentImages(@RequestParam String userName) {
//        List<Image> images = imageRetrievalService.getRecentImages(userName);
//        List<ImageResponseDTO> response = images.stream()
//                .map(image -> new ImageResponseDTO(image.getBase64Image(), image.getImageUrl(), image.getPrompt(), image.getGenAt()))
//                .collect(Collectors.toList());
//        return ResponseEntity.ok(response);
//    }
}
