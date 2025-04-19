package ru.practicum.shareit.item;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.client.CacheClient;
import ru.practicum.shareit.client.ServerConfigProperties;
import ru.practicum.shareit.dto.item.CommentCreateDto;
import ru.practicum.shareit.dto.item.ItemCreateDto;
import ru.practicum.shareit.dto.item.ItemUpdateDto;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Component
public class ItemClient extends BaseClient {
    private final CacheClient cache;

    public ItemClient(ServerConfigProperties config,
                      RestTemplateBuilder builder, CacheClient cache) {
        super(builder
                .rootUri(config.getUrl())
                .build());
        this.cache = cache;
    }

    public ResponseEntity<Object> createItem(Long ownerId, ItemCreateDto dto) {
        return post("/items", ownerId, dto);
    }

    public ResponseEntity<Object> findItemById(Long itemId, Long ownerId) {
        String key = "/items/" + itemId + "?userId=" + ownerId;
        ResponseEntity<Object> cached = cache.getCached(key);
        if (cached != null) {
            return cached;
        }
        ResponseEntity<Object> response = get("/items/" + itemId, ownerId);
        cache.put(key, response);
        return response;
    }

    public ResponseEntity<Object> findAllByOwnerId(Long ownerId) {
        String key = "/requests?userId=" + ownerId;
        ResponseEntity<Object> cached = cache.getCached(key);
        if (cached != null) {
            return cached;
        }
        ResponseEntity<Object> response = get("/requests", ownerId);
        cache.put(key, response);
        return response;
    }

    public ResponseEntity<Object> addComment(Long itemId, Long ownerId, CommentCreateDto dto) {
        return post("/items/" + itemId + "/comment", ownerId, dto);
    }

    public ResponseEntity<Object> update(Long ownerId, Long itemId, ItemUpdateDto dto) {
        return patch("/items/" + itemId, ownerId, dto);
    }

    public ResponseEntity<Object> search(String text) {
        String encodedText = URLEncoder.encode(text, StandardCharsets.UTF_8);
        String key = "/items/search?text=" + encodedText;

        ResponseEntity<Object> cached = cache.getCached(key);
        if (cached != null) {
            return cached;
        }

        Map<String, Object> params = Map.of("text", text);
        ResponseEntity<Object> response = get("/items/search", null, params);
        cache.put(key, response);
        return response;
    }
}
