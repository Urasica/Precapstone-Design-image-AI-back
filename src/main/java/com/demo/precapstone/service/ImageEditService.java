package com.demo.precapstone.service;

import com.demo.precapstone.dao.Image;
import com.demo.precapstone.dao.User;
import com.demo.precapstone.dto.ImageEditRequestDTO;
import com.demo.precapstone.dto.ImageDTO;
import com.demo.precapstone.repository.ImageRepository;
import com.demo.precapstone.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;

@Service
public class ImageEditService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private FileStorageService fileStorageService;

    public ImageDTO editImage(ImageEditRequestDTO imageEditRequestDTO) throws IOException {
        // 닉네임을 사용하여 사용자 조회
        User user = userRepository.findByUserName(imageEditRequestDTO.getNickname());
        if (user == null) {
            throw new RuntimeException("User not found for nickname: " + imageEditRequestDTO.getNickname());
        }

        // Base64 이미지를 파일로 저장
        String storedImageUrl = fileStorageService.storeImage(imageEditRequestDTO.getBase64ImageData());

        // 이미지 엔티티 생성 및 저장
        Image image = new Image(storedImageUrl, "edited by user", LocalDateTime.now(), user);
        imageRepository.save(image);

        // 클라이언트로 반환할 DTO 생성
        return new ImageDTO(storedImageUrl, imageEditRequestDTO.getBase64ImageData()); // 수정한 이미지와 url 전달
    }
}
