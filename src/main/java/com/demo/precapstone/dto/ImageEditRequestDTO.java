package com.demo.precapstone.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ImageEditRequestDTO {
    private String nickname; // 클라이언트가 제공한 닉네임
    private String base64ImageData; // Base64로 인코딩된 이미지 데이터
}
