package ru.practicum.shareit.booking.handlers.itemsBooking;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingByItemHandler {
    boolean canHandle(BookingState state);

    List<Booking> handle(List<Long> itemIds, BookingState state, LocalDateTime now);
}
