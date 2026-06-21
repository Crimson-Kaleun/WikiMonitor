package com.example.WikiMonitor.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "wiki_snapshots")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WikiSnapshot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String keyword;
    private LocalDateTime timestamp;

    @ElementCollection
    @CollectionTable(name = "snapshot_titles", joinColumns = @JoinColumn(name = "snapshot_id"))
    @Column(name = "title")
    private List<String> titles;

    @ElementCollection
    @CollectionTable(name = "snapshot_urls", joinColumns = @JoinColumn(name = "snapshot_id"))
    @Column(name = "url")
    private List<String> urls;
}
