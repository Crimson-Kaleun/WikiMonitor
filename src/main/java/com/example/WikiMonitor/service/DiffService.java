package com.example.WikiMonitor.service;

import com.example.WikiMonitor.dto.ChangeEvent;
import com.example.WikiMonitor.model.WikiSnapshot;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
@RequiredArgsConstructor
public class DiffService {

    public List<ChangeEvent> compareTitles(List<String> oldTitles, List<String> newTitles) {
        Set<String> oldSet = new HashSet<>(oldTitles);
        Set<String> newSet = new HashSet<>(newTitles);

        List<ChangeEvent> events = new ArrayList<>();

        //Появившиеся статьи (есть в new, нет в old)
        Set<String> added = new HashSet<>(newSet);
        added.removeAll(oldSet);
        for (String title : added) {
            events.add(new ChangeEvent("ADDED", title));
        }

        //Удаленные статьи (есть в old, нет в new)
        Set<String> removed = new HashSet<>(oldSet);
        removed.removeAll(newSet);
        for (String title : removed) {
            events.add(new ChangeEvent("REMOVED", title));
        }

        return events;
    }
}