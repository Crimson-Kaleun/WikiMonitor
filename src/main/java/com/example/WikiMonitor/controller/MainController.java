package com.example.WikiMonitor.controller;

import com.example.WikiMonitor.config.WikiMonitorProperties;
import com.example.WikiMonitor.service.WikiMonitorService;
import com.example.WikiMonitor.service.WikiSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class MainController {
    private final WikiSearchService wikiService;

    private final WikiMonitorProperties properties;
    private final WikiMonitorService wikiMonitorService;


    @GetMapping("/getwiki")
    public ResponseEntity<String> getWikiList(@RequestParam String name) {
        try {
            String response = wikiService.getSearchResults(name);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Ошибка: " + e.getMessage());
        }
    }

    @GetMapping("/keyword")
    public ResponseEntity<String> getKeyword() {
        return ResponseEntity.ok(properties.getKeyword());
    }

    @PostMapping("/keyword")
    public ResponseEntity<String> updateKeyword(@RequestParam String keyword) {
        properties.setKeyword(keyword);
        return ResponseEntity.ok("Ключевое слово изменено на: " + keyword);
    }

    @PostMapping("/check-now")
    public ResponseEntity<String> checkNow(@RequestParam(required = false) String keyword) {
        if (keyword != null) {
            properties.setKeyword(keyword);
        }
        wikiMonitorService.checkWikiChanges();
        return ResponseEntity.ok("Проверка запущена с ключевым словом: " + properties.getKeyword());
    }
}
