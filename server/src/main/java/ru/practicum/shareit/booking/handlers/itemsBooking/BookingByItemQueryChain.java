package ru.practicum.shareit.booking.handlers.itemsBooking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.dto.booking.BookingState;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BookingByItemQueryChain {
    private final List<BookingByItemHandler> handlers;

    public List<Booking> getBookings(List<Long> itemIds, BookingState state, LocalDateTime now) {
        return handlers.stream()
                .filter(handler -> handler.canHandle(state))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown booking state: " + state))
                .handle(itemIds, state, now);
    }
}
