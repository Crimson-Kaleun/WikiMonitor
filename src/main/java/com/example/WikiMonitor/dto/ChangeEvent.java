package com.example.WikiMonitor.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChangeEvent {
    private String type; // ADDED или REMOVED
    private String title;
    private String timestamp;

    public ChangeEvent(String type, String title) {
        this.type = type;
        this.title = title;
        this.timestamp = java.time.LocalDateTime.now().toString();
    }

    @Override
    public String toString() {
        return String.format("[%s] %s", type, title);
    }
}