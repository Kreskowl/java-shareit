package ru.practicum.shareit.booking.handlers.userBooking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.dto.booking.BookingState;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CurrentBookingHandler implements BookingHandler {
    private final BookingRepository repository;

    @Override
    public boolean canHandle(BookingState state) {
        return state == BookingState.CURRENT;
    }

    @Override
    public List<Booking> handle(long userId, BookingState state, LocalDateTime now) {
        return repository.findByRenterIdAndStartTimeBeforeAndEndTimeAfterOrderByStartTimeDesc(userId, now, now);
    }
}
