package com.demo.precapstone.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class ImageResponseDTO {

    private String imageUrl;
    private String content;
    private LocalDateTime genAt;

    public ImageResponseDTO(String imageUrl, String content, LocalDateTime genAt) {
        this.imageUrl = imageUrl;
        this.content = content;
        this.genAt = genAt;
    }
}