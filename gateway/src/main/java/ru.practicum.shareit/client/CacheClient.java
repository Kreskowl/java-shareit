package ru.practicum.shareit.client;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class CacheClient {
    private final Map<String, ResponseEntity<Object>> cache = new ConcurrentHashMap<>();

    public ResponseEntity<Object> getCached(String key) {
        return cache.get(key);
    }

    public void put(String key, ResponseEntity<Object> response) {
        cache.put(key, response);
    }
}
