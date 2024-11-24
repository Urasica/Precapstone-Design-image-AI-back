package com.demo.precapstone.controller;

import com.demo.precapstone.dto.MessageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import com.demo.precapstone.service.MessageService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @PostMapping("/send")
    public ResponseEntity<String> sendMessage(@RequestBody MessageDTO messageDTO) {
        if (messageDTO == null || messageDTO.getToPhoneNumbers() == null || messageDTO.getToPhoneNumbers().length == 0) {
            return ResponseEntity.badRequest().body("Invalid request body: No phone numbers provided");
        }

        // 디버깅용 로그
        System.out.println("Received MessageDTO: " + messageDTO.getPath() + ", " + messageDTO.getContent());
        System.out.println("Sending message from: " + messageDTO.getFromPhoneNumber());

        for (String phoneNumber : messageDTO.getToPhoneNumbers()) {
            try {
                messageService.sendMessage(messageDTO, phoneNumber);
            } catch (Exception e) {
                // Handle error sending to this particular phone number
                return ResponseEntity.status(500).body("Failed to send message to " + phoneNumber + ": " + e.getMessage());
            }
        }
        return ResponseEntity.ok("Message sent successfully to all recipients");
    }
}
