package com.demo.precapstone.controller;

import com.demo.precapstone.service.OpenAIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/emoji")
public class testGenerate {

    private final OpenAIService openAIService;

    @Autowired
    public testGenerate(OpenAIService openAIService) {
        this.openAIService = openAIService;
    }

    @PostMapping("/generate")
    public String generateEmoji(@RequestBody String Text) {
        return openAIService.createPromptFromText(Text);
    }
}
