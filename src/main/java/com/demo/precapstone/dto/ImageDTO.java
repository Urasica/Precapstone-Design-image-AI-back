package com.demo.precapstone.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class ImageDTO {
    private String imageUrl;
    private String image;

    public ImageDTO(String imageUrl, String image) {
        this.imageUrl = imageUrl;
        this.image = image;
    }
}