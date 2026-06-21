package com.example.WikiMonitor.service;

import com.example.WikiMonitor.dto.WikiSearchResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WikiSearchService {
    private final RestClient restClient;

    private final ObjectMapper objectMapper;

    public String getSearchResults(String query) {

        String url = "https://ru.wikipedia.org/w/api.php?action=opensearch&search=" + query;

        String jsonResponse = restClient.get()
                .uri(url)
                .retrieve()
                .body(String.class);

        return jsonResponse;

        /*
        String url = "https://ru.wikipedia.org/w/api.php";

        String jsonResponse = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(url)
                        .queryParam("action", "opensearch")
                        .queryParam("search", query)
                        .build())
                .retrieve()
                .body(String.class);

        return jsonResponse; */
    }

    public WikiSearchResponse getSearchResults2(String query) {
        String url = "https://ru.wikipedia.org/w/api.php?action=opensearch&search=" + query;

        String jsonResponse = restClient.get()
                .uri(url)
                .retrieve()
                .body(String.class);

        try {
            // [0] -> String (ключевое слово)
            // [1] -> List<String> (названия)
            // [2] -> List<String> (описания)
            // [3] -> List<String> (ссылки)
            List<Object> rawResponse = objectMapper.readValue(jsonResponse, List.class);

            WikiSearchResponse response = new WikiSearchResponse();
            response.setKeyword((String) rawResponse.get(0));
            response.setTitles((List<String>) rawResponse.get(1));
            response.setDescriptions((List<String>) rawResponse.get(2));
            response.setUrls((List<String>) rawResponse.get(3));

            return response;
        } catch (Exception e) {
            throw new RuntimeException("Ошибка парсинга ответа от Wikipedia API", e);
        }
    }

}
