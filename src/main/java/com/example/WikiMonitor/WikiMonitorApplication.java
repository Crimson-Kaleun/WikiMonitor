package com.example.WikiMonitor;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestClient;

@SpringBootApplication
@EnableScheduling
public class WikiMonitorApplication {

	public static void main(String[] args) {
		SpringApplication.run(WikiMonitorApplication.class, args);
		for(int i = 0; i < 5; i++){
			System.out.println("i = " + i);
		}
	}

	@Bean
	public RestClient restClient() {
		return RestClient.builder()
				.defaultHeader("User-Agent", "Wiki New Pages Searcher")
				.defaultHeader("Accept-Encoding", "gzip")
				.build();
	}

	@Bean
	public ObjectMapper objectMapper() {
        return new ObjectMapper();
	}
}
