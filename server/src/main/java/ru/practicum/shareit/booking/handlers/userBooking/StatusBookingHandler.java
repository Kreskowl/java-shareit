package ru.practicum.shareit.booking.handlers.userBooking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.mapper.BookingStateMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.dto.booking.BookingState;
import ru.practicum.shareit.dto.booking.Status;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class StatusBookingHandler implements BookingHandler {
    private final BookingRepository repository;

    @Override
    public boolean canHandle(BookingState state) {
        return BookingStateMapper.toStatus(state).isPresent();
    }

    @Override
    public List<Booking> handle(long userId, BookingState state, LocalDateTime now) {
        Status status = BookingStateMapper.toStatus(state)
                .orElseThrow(() -> new IllegalArgumentException("Unknown booking state: " + state));
        return repository.findByRenterIdAndStatusOrderByStartTimeDesc(userId, status);
    }
}
