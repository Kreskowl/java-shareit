package ru.practicum.shareit.user;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.client.CacheClient;
import ru.practicum.shareit.client.ServerConfigProperties;
import ru.practicum.shareit.dto.user.UserDto;
import ru.practicum.shareit.dto.user.UserUpdateDto;

@Component
public class UserClient extends BaseClient {
    private final CacheClient cache;

    public UserClient(ServerConfigProperties config,
                      RestTemplateBuilder builder, CacheClient cache) {
        super(builder
                .rootUri(config.getUrl())
                .build());
        this.cache = cache;
    }

    public ResponseEntity<Object> createUser(UserDto dto) {
        return post("/users", dto);
    }

    public ResponseEntity<Object> update(Long id, UserUpdateDto dto) {
        return patch("/users/" + id, dto);
    }

    public ResponseEntity<Object> deleteById(Long id) {
        return delete("/users/" + id);
    }

    public ResponseEntity<Object> findById(Long id) {
        String key = "/users/" + id;
        ResponseEntity<Object> cached = cache.getCached(key);
        if (cached != null) {
            return cached;
        }
        ResponseEntity<Object> response = get(key);
        cache.put(key, response);
        return response;
    }
}

