package com.demo.precapstone.controller;

import com.demo.precapstone.dto.ImageDTO;
import com.demo.precapstone.service.ImageGenerationService;
import com.demo.precapstone.service.ImageGenerationServiceDemo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/images")
public class ImageGenerationController {

    @Autowired
    private ImageGenerationService imageGenerationService;

    @Autowired
    private ImageGenerationServiceDemo demo;

//    @PostMapping("/generate")
//    public ResponseEntity<String> generateImage(@RequestParam String Username, @RequestBody String message) {
//        Image image = imageGenerationService.generateImage(message, Username);
//        String filePath = "경로"; // 이미지 파일 경로 가져오기
//        return ResponseEntity.ok(filePath); // 파일 경로 문자열을 응답으로 반환
//    }

    @PostMapping("/generate")
    public ResponseEntity<ImageDTO> generateImage(@RequestBody String message) {
        ImageDTO imageDTO = demo.generateImage(message);
        return ResponseEntity.ok(imageDTO);
    }

}
