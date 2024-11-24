package com.demo.precapstone.controller;

import com.demo.precapstone.service.ColabService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/colab")
public class ColabController {

    @Autowired
    private ColabService colabService;

    // 서버 URL 추가
    @PostMapping("/add-server")
    public String addServerUrl(@RequestBody String serverUrl) {
        colabService.addServerUrl(serverUrl);
        return "Server URL added: " + serverUrl;
    }

    // 서버 URL 제거
    @DeleteMapping("/remove-server")
    public String removeServerUrl(@RequestBody String serverUrl) {
        boolean removed = colabService.removeServerUrl(serverUrl);
        return removed ? "Server URL removed: " + serverUrl : "Server URL not found: " + serverUrl;
    }

    // 서버 URL 목록 반환
    @GetMapping("/list-servers")
    public List<String> listServerUrls() {
        return colabService.getServerUrls();
    }
}
