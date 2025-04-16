package ru.practicum.shareit.booking.handlers;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.repository.BookingRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingHandler {
    boolean canHandle(BookingState state);
    List<Booking> handle(long userId, BookingState state, LocalDateTime now);
}
