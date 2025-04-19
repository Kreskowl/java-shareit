package ru.practicum.shareit.booking.handlers.itemsBooking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.dto.booking.BookingState;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PastBookingByItemHandler implements BookingByItemHandler {
    private final BookingRepository repository;

    @Override
    public boolean canHandle(BookingState state) {
        return state == BookingState.PAST;
    }

    @Override
    public List<Booking> handle(List<Long> itemIds, BookingState state, LocalDateTime now) {
        return repository.findAllByItemIdInAndEndTimeBeforeOrderByStartTimeDesc(itemIds, now);
    }
}
