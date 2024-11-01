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
    MessageService messageService;

    @PostMapping("/send")
    public ResponseEntity<String> sendMessage(@RequestBody MessageDTO messageDTO) {
        if (messageDTO == null) {
            return ResponseEntity.badRequest().body("Invalid request body");
        }
        System.out.println("Received MessageDTO: " + messageDTO.getPath() + ", " + messageDTO.getContent());
        messageService.sendMessage(messageDTO);
        return ResponseEntity.ok("Message sent successfully");
    }
}
