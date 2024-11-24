package com.demo.precapstone.controller;

import com.demo.precapstone.dto.ImageDTO;
import com.demo.precapstone.dto.ImageEditRequestDTO;
import com.demo.precapstone.service.ImageEditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/images")
public class ImageEditController {

    @Autowired
    private ImageEditService imageEditService;

    @PostMapping("/edit")
    public ResponseEntity<ImageDTO> uploadImage(@RequestBody ImageEditRequestDTO imageUploadRequestDTO) throws IOException {
        ImageDTO imageDTO = imageEditService.editImage(imageUploadRequestDTO);
        return ResponseEntity.ok(imageDTO);
    }
}
