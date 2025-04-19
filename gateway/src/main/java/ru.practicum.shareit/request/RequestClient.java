package ru.practicum.shareit.request;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.client.CacheClient;
import ru.practicum.shareit.client.ServerConfigProperties;
import ru.practicum.shareit.dto.request.ItemRequestCreateDto;

@Component
public class RequestClient extends BaseClient {
    private final CacheClient cache;

    public RequestClient(ServerConfigProperties config,
                         RestTemplateBuilder builder, CacheClient cache) {
        super(builder
                .rootUri(config.getUrl())
                .build());
        this.cache = cache;
    }

    public ResponseEntity<Object> createItemRequest(ItemRequestCreateDto dto, Long userId) {
        return post("/requests", userId, dto);
    }

    public ResponseEntity<Object> findAllUserRequests(Long userId) {
        String key = "/requests?userId=" + userId;
        ResponseEntity<Object> cached = cache.getCached(key);
        if (cached != null) {
            return cached;
        }
        ResponseEntity<Object> response = get("/requests", userId);
        cache.put(key, response);
        return response;
    }

    public ResponseEntity<Object> findAllOtherRequests(Long userId) {
        String key = "/requests/all?userId=" + userId;
        ResponseEntity<Object> cached = cache.getCached(key);
        if (cached != null) {
            return cached;
        }
        ResponseEntity<Object> response = get("/requests/all", userId);
        cache.put(key, response);
        return response;
    }

    public ResponseEntity<Object> findById(Long requestId) {
        String key = "/requests/" + requestId;
        ResponseEntity<Object> cached = cache.getCached(key);
        if (cached != null) {
            return cached;
        }
        ResponseEntity<Object> response = get(key);
        cache.put(key, response);
        return response;
    }
}

