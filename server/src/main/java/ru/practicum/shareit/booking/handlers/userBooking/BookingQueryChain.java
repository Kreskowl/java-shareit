package ru.practicum.shareit.booking.handlers.userBooking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.dto.booking.BookingState;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BookingQueryChain {
    private final List<BookingHandler> handlers;

    public List<Booking> getBookings(Long userId, BookingState state, LocalDateTime now) {
        return handlers.stream()
                .filter(handler -> handler.canHandle(state))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown booking state: " + state))
                .handle(userId, state, now);
    }
}
