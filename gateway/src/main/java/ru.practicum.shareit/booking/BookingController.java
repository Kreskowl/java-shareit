package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.dto.ShareConstants;
import ru.practicum.shareit.dto.booking.BookingCreateDto;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingClient client;

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBookingById(@PathVariable long bookingId) {
        return client.findBookingById(bookingId);
    }

    @PostMapping
    public ResponseEntity<Object> createBooking(
            @RequestHeader(ShareConstants.USER_ID_HEADER) Long userId,
            @RequestBody @Valid BookingCreateDto dto) {
        log.info("Received DTO: {}", dto);
        return client.createBooking(userId, dto);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> updateStatus(@RequestHeader(ShareConstants.USER_ID_HEADER) Long ownerId,
                                               @PathVariable long bookingId,
                                               @RequestParam("approved") boolean approved) {
        log.info("PATCH /bookings/{}?approved={}", bookingId, approved);
        return client.updateStatus(ownerId, bookingId, approved);
    }

    @GetMapping
    public ResponseEntity<Object> getAllCurrentBookings(
            @RequestHeader(ShareConstants.USER_ID_HEADER) Long userId,
            @RequestParam(value = "state", defaultValue = "ALL") String stateParam) {
        log.info("Get all bookings for user with id: {} with state: {}", userId, stateParam);

        Map<String, Object> params = Map.of("state", stateParam);
        return client.getBookings(userId, params);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getAllBookingsForOwner(
            @RequestHeader(ShareConstants.USER_ID_HEADER) Long ownerId,
            @RequestParam(value = "state", defaultValue = "ALL") String stateParam) {
        log.info("Get all bookings for owner with id: {} and state: {}", ownerId, stateParam);

        Map<String, Object> params = Map.of("state", stateParam);
        return client.getBookingsByOwner(ownerId, params);
    }

}
