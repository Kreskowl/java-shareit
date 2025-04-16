package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.item.UserIdHeader;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingServiceImpl service;

    @GetMapping("/{bookingId}")
    public BookingDto getBookingById(@PathVariable long bookingId) {
        return service.findById(bookingId);
    }

    @PostMapping
    public BookingDto createBooking(
            @RequestHeader(UserIdHeader.USER_ID_HEADER) Long userId,
            @RequestBody @Valid BookingCreateDto dto) {
        log.info("Received DTO: {}", dto);
        return service.createBooking(dto, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto updateStatus(@RequestHeader(value = UserIdHeader.USER_ID_HEADER) Long ownerId,
                                   @PathVariable long bookingId,
                                   @RequestParam("approved") boolean approved) {
        return service.updateStatusByOwner(bookingId, ownerId, approved);
    }

    @GetMapping
    public List<BookingDto> getAllCurrentBookings(
            @RequestHeader(value = UserIdHeader.USER_ID_HEADER) Long userId,
            @RequestParam(value = "state", required = false, defaultValue = "ALL") String stateParam) {

        BookingState state = parseState(stateParam);
        return service.getAllCurrentBookings(userId, state);
    }

    @GetMapping("/owner")
    public List<BookingDto> getAllBookingsForOwner(
            @RequestHeader(value = UserIdHeader.USER_ID_HEADER) Long ownerId,
            @RequestParam(value = "state", required = false, defaultValue = "ALL") String stateParam) {

        BookingState state = parseState(stateParam);
        return service.getAllBookedItems(ownerId, state);
    }

    private BookingState parseState(String stateParam) {
        try {
            return BookingState.valueOf(stateParam.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unknown state: " + stateParam);
        }
    }
}
