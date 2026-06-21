package com.example.WikiMonitor.service;

import com.example.WikiMonitor.config.WikiMonitorProperties;
import com.example.WikiMonitor.dto.ChangeEvent;
import com.example.WikiMonitor.dto.WikiSearchResponse;
import com.example.WikiMonitor.model.WikiSnapshot;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class WikiMonitorService {

    private final WikiSearchService wikiSearchService;
    private final SnapshotService snapshotService;
    private final DiffService diffService;
    private final KafkaProducerService kafkaProducerService;
    private final WikiMonitorProperties properties;

    //private static final String KEYWORD = "Word";


    @Transactional
    @Scheduled(cron = "${wiki.monitor.cron}") //0 0 0 * * ? - Каждый день в 0:00
    public void checkWikiChanges() {
        //kafkaProducerService.sendMessage("checkWikiChanges пошел работать");
        String KEYWORD = properties.getKeyword();
        log.info("Запуск проверки изменений для ключевого слова: {}", KEYWORD);

        try {
            WikiSearchResponse response = wikiSearchService.getSearchResults2(KEYWORD);
            List<String> currentTitles = response.getTitles();
            log.info("Получено {} статей по запросу '{}'", currentTitles.size(), KEYWORD);

            Optional<WikiSnapshot> lastSnapshot = snapshotService.getLatestSnapshot(KEYWORD);

            if (lastSnapshot.isPresent()) {
                List<String> previousTitles = lastSnapshot.get().getTitles();

                List<ChangeEvent> changes = diffService.compareTitles(previousTitles, currentTitles);

                if (!changes.isEmpty()) {
                    log.info("Обнаружено {} изменений", changes.size());

                    for (ChangeEvent event : changes) {
                        String message = String.format("Ключевое слово: %s, Изменение: %s, Статья: %s",
                                KEYWORD, event.getType(), event.getTitle());
                        kafkaProducerService.sendMessage(message);
                        log.info("Отправлено: {}", message);
                    }
                } else {
                    log.info("Изменений не обнаружено");
                }
            } else {
                log.info("Предыдущий снимок не найден. Это первый запуск по ключевому слову '{}'", KEYWORD);
            }

            snapshotService.saveSnapshot(KEYWORD, currentTitles, response.getUrls());
            log.info("Снимок сохранен успешно");

        } catch (Exception e) {
            log.error("Ошибка при проверке изменений: {}", e.getMessage(), e);
        }
    }
}