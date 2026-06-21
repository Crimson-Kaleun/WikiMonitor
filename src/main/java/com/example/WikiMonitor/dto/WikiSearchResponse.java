package com.example.WikiMonitor.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@RequiredArgsConstructor
public class WikiSearchResponse {
    private String keyword;      // 0 элемент: ключевое слово
    private List<String> titles; // 1 элемент: названия статей
    private List<String> descriptions; // 2 элемент: описания (?) (пустует обычно)
    private List<String> urls;   // 3 элемент: ссылки


}