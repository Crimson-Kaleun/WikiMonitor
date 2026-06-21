package com.example.WikiMonitor.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "wiki.monitor")
@Data
public class WikiMonitorProperties {
    private String keyword = "Word";
    private String cron = "0 0 0 * * ?";
}