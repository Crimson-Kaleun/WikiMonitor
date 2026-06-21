# WikiMonitor 🔍

Сервис для отслеживания изменений статей в Wikipedia. Автоматически проверяет появление или удаление статей по заданному ключевому слову и отправляет уведомления в Apache Kafka.

## 📋 Возможности

- 🔄 **Автоматический мониторинг** — ежедневная проверка изменений по расписанию (cron)
- 📡 **REST API** — ручное управление и проверка
- 📨 **Kafka-уведомления** — события о новых и удалённых статьях
- 💾 **Хранение истории** — база данных H2 для отслеживания изменений
- ⚙️ **Гибкая конфигурация** — изменение ключевого слова на лету без перезапуска
- 🎯 **Сравнение снимков** — определяет добавленные и удалённые статьи между проверками

## 🚀 Быстрый старт

### Предварительные требования

- Java 17+
- Maven 3.8+
- Docker (для Kafka)

## 📡 API Endpoints

### Мониторинг

#### Ручная вызов API

```http
GET /api/events/getwiki?name=Word
```

Запускает поиск статей по указанному слову и возвращает результат.

#### Структура ответа:

```json
[
  "Word",
  [
    "Word",
    "WordPress",
    "Word Up!",
    "...",
  ],
  [
    "", "", "", "", "", "", "", "", "", ""
  ],
  [
    "https://ru.wikipedia.org/wiki/Word",
    "https://ru.wikipedia.org/wiki/WordPress",
    "https://ru.wikipedia.org/wiki/Word_Up!",
    "...",
  ]
]
```
### Управление конфигурацией

#### Ручной запуск проверки

```http
POST /api/events/check-now
```

#### Получить текущее ключевое слово

```http
GET /api/events/keyword
```

#### Изменить ключевое слово
```http
POST /api/events/keyword?keyword=Java
```

#### Проверить с произвольным ключевым словом (ключевое слово меняется)
```http
POST /api/events/check-now?keyword=Python
```


### 📊 Kafka и Kafka UI

#### Топики

Сервис отправляет сообщения в топик my_topic.

#### Формат сообщений:

```text
Ключевое слово: Word, Изменение: ADDED, Статья: WordPress 6.0
Ключевое слово: Word, Изменение: REMOVED, Статья: WordPerfect
```

#### Kafka UI

Открыть http://localhost:8081 для просмотра:

    Выбрать кластер local в интерфейсе

    Перейти в раздел Topics → my_topic

    Нажать Messages для просмотра сообщений



## 📊 Схема взаимодействия

```
Scheduler (@Scheduled)
  │
  ├─→ WikiMonitorService.checkWikiChanges()
  │     │
  │     ├─→ properties.getKeyword()  // По умолчанию ключевое слово "Word"
  │     │
  │     ├─→ WikiSearchService.getSearchResults("Word")
  │     │     └─→ RestClient → Wikipedia API
  │     │           └─→ WikiSearchResponse { keyword, titles[], urls[] }
  │     │
  │     ├─→ SnapshotService.getLatestActiveSnapshot("Word")
  │     │     └─→ WikiSnapshotRepository.findTopByKeyword...()
  │     │           └─→ WikiSnapshot (предыдущий снимок) или пусто
  │     │
  │     ├─→ DiffService.compareTitles(oldTitles, newTitles)
  │     │     └─→ List<ChangeEvent> [{ADDED, "WordPress 6.0"}, {REMOVED, "WordPerfect"}]
  │     │
  │     ├─→ для каждого ChangeEvent:
  │     │     KafkaProducerService.sendMessage(formattedMessage)
  │     │       └─→ KafkaTemplate.send("my_topic", message)
  │     │             └─→ Apache Kafka
  │     │
  │     └─→ SnapshotService.saveSnapshot("Word", titles, urls)
  │           └─→ WikiSnapshotRepository.save(newSnapshot)
  │                 └─→ H2 Database

```

Ручной запуск через REST
```text
POST /api/check?keyword=Java
  │
  └─→ WikiController.checkNow("Java")
        │
        └─→ WikiMonitorService.checkWikiChanges("Java")
              │
              └─→ (дальше аналогичный поток, но с "Java")

```


Поток Kafka
```text

Отправка:
WikiMonitorService → KafkaProducerService → KafkaTemplate → Kafka Broker (topic: my_topic)

Получение:
Kafka Broker (topic: my_topic) → KafkaConsumerService.consumeMessage()
  └─→ System.out.println("Получено сообщение: ...")
```

Изменение конфигурации
```text

POST /api/config/keyword?value=Python
  │
  └─→ WikiController.setKeyword("Python")
        │
        └─→ properties.setKeyword("Python")  // в памяти
              │
              └─→ при следующем поиске Scheduler подхватит новое значение
```

Поиск без сохранения
```text
GET /api/search?query=Spring
  │
  └─→ WikiController.search("Spring")
        │
        └─→ WikiSearchService.getSearchResults("Spring")
              └─→ RestClient → Wikipedia API
                    └─→ сразу возвращаем JSON пользователю
```

Краткая суть связей
```text

Controller → WikiMonitorService → WikiSearchService → Wikipedia
                               → SnapshotService → H2
                               → DiffService → List<ChangeEvent>
                               → KafkaProducerService → Kafka

Kafka → KafkaConsumerService → sout

```
