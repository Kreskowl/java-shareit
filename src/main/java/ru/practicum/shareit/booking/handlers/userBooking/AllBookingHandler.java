package ru.practicum.shareit.booking.handlers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.repository.BookingRepository;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AllBookingHandler implements BookingHandler {
    private final BookingRepository repository;

    @Override
    public boolean canHandle(BookingState state) {
        return state == BookingState.ALL;
    }

    @Override
    public List<Booking> handle(long userId, BookingState state, LocalDateTime now) {
        return repository.findByRenterIdOrderByStartTimeDesc(userId);
    }
}
