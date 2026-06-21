package com.example.WikiMonitor.repository;

import com.example.WikiMonitor.model.WikiSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface WikiSnapshotRepository extends JpaRepository<WikiSnapshot, Long> {
    Optional<WikiSnapshot> findTopByKeywordOrderByTimestampDesc(String keyword);
}