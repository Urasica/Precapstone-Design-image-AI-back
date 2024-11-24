package com.demo.precapstone.dto;

import lombok.Data;

@Data
public class MessageDTO {
    private String content;
    private String path;
    private String fromPhoneNumber;
    private String[] toPhoneNumbers;
}
