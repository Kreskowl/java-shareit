package ru.practicum.shareit.booking;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.client.CacheClient;
import ru.practicum.shareit.client.ServerConfigProperties;
import ru.practicum.shareit.dto.booking.BookingCreateDto;

import java.util.Map;

@Component
public class BookingClient extends BaseClient {
    private final CacheClient cache;

    public BookingClient(ServerConfigProperties config,
                         RestTemplateBuilder builder, CacheClient cache) {
        super(builder
                .rootUri(config.getUrl())
                .build());
        this.cache = cache;
    }

    public ResponseEntity<Object> createBooking(Long userId, BookingCreateDto dto) {
        return post("/bookings", userId, dto);
    }

    public ResponseEntity<Object> findBookingById(Long bookingId) {
        String key = "/bookings/" + bookingId;
        ResponseEntity<Object> cached = cache.getCached(key);
        if (cached != null) {
            return cached;
        }
        ResponseEntity<Object> response = get(key);
        cache.put(key, response);
        return response;
    }

    public ResponseEntity<Object> updateStatus(Long ownerId, Long bookingId, boolean approved) {
        String path = "/bookings/" + bookingId + "?approved=" + approved;
        return patch(path, ownerId, null);
    }

    public ResponseEntity<Object> getBookings(Long userId, Map<String, Object> params) {
        String key = buildCacheKey("/bookings", userId, params);

        ResponseEntity<Object> cached = cache.getCached(key);
        if (cached != null) {
            return cached;
        }

        ResponseEntity<Object> response = get("/bookings", userId, params);
        cache.put(key, response);
        return response;
    }

    public ResponseEntity<Object> getBookingsByOwner(Long ownerId, Map<String, Object> params) {
        String key = buildCacheKey("/bookings/owner", ownerId, params);

        ResponseEntity<Object> cached = cache.getCached(key);
        if (cached != null) {
            return cached;
        }

        ResponseEntity<Object> response = get("/bookings/owner", ownerId, params);
        cache.put(key, response);
        return response;
    }

    private String buildCacheKey(String path, Long userId, Map<String, Object> params) {
        StringBuilder key = new StringBuilder(path);
        if (userId != null) {
            key.append("|userId=").append(userId);
        }
        if (params != null && !params.isEmpty()) {
            params.forEach((k, v) -> key.append("|").append(k).append("=").append(v));
        }
        return key.toString();
    }
}
