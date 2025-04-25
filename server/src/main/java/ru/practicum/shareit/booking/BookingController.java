package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
import ru.practicum.shareit.dto.booking.BookingDto;
import ru.practicum.shareit.dto.booking.BookingState;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService service;

    @GetMapping("/{bookingId}")
    public BookingDto getBookingById(@PathVariable long bookingId) {
        return service.findById(bookingId);
    }

    @PostMapping
    public BookingDto createBooking(
            @RequestHeader(ShareConstants.USER_ID_HEADER) Long userId,
            @Valid @RequestBody BookingCreateDto dto) {
        return service.createBooking(dto, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto updateStatus(@RequestHeader(value = ShareConstants.USER_ID_HEADER) Long ownerId,
                                   @PathVariable long bookingId,
                                   @RequestParam("approved") boolean approved) {
        return service.updateStatusByOwner(bookingId, ownerId, approved);
    }

    @GetMapping
    public List<BookingDto> getAllCurrentBookings(
            @RequestHeader(value = ShareConstants.USER_ID_HEADER) Long userId,
            @RequestParam(value = "state", required = false, defaultValue = "ALL") String stateParam) {
        BookingState state = parseState(stateParam);
        return service.getAllCurrentBookings(userId, state);
    }

    @GetMapping("/owner")
    public List<BookingDto> getAllBookingsForOwner(
            @RequestHeader(value = ShareConstants.USER_ID_HEADER) Long ownerId,
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
