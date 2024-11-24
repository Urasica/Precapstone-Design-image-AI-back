package com.demo.precapstone.controller;

import com.demo.precapstone.service.ColabService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/colab")
public class ColabController {

    @Autowired
    private ColabService colabService;

    @PostMapping("/add-server")
    public String addServerUrl(@RequestBody String serverUrl) {
        colabService.addServerUrl(serverUrl);
        return "Server URL added: " + serverUrl;
    }

    @DeleteMapping("/remove-server")
    public String removeServerUrl(@RequestBody String serverUrl) {
        boolean removed = colabService.removeServerUrl(serverUrl);
        return removed ? "Server URL removed: " + serverUrl : "Server URL not found: " + serverUrl;
    }
}
