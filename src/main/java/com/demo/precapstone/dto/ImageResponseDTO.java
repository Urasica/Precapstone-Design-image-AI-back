package com.demo.precapstone.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class ImageResponseDTO {
    private String base64Image;
    private String imageUrl;
    private String content;
    private LocalDateTime genAt;

    public ImageResponseDTO(String base64Image, String imageUrl, String content, LocalDateTime genAt) {
        this.base64Image = base64Image;
        this.imageUrl = imageUrl;
        this.content = content;
        this.genAt = genAt;
    }
}