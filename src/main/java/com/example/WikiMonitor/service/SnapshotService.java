package com.example.WikiMonitor.service;

import com.example.WikiMonitor.model.WikiSnapshot;
import com.example.WikiMonitor.repository.WikiSnapshotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SnapshotService {
    private final WikiSnapshotRepository repository;

    @Transactional
    public WikiSnapshot saveSnapshot(String keyword, List<String> titles, List<String> urls) {
        WikiSnapshot snapshot = WikiSnapshot.builder()
                .keyword(keyword)
                .timestamp(LocalDateTime.now())
                .titles(titles)
                .urls(urls)
                .build();
        return repository.save(snapshot);
    }

    public Optional<WikiSnapshot> getLatestSnapshot(String keyword) {
        return repository.findTopByKeywordOrderByTimestampDesc(keyword);
    }
}