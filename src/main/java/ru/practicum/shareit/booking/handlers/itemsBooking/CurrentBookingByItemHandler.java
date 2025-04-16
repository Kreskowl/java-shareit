package ru.practicum.shareit.booking.handlers.itemsBooking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.repository.BookingRepository;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CurrentBookingByItemHandler implements BookingByItemHandler {
    private final BookingRepository repository;

    @Override
    public boolean canHandle(BookingState state) {
        return state == BookingState.CURRENT;
    }

    @Override
    public List<Booking> handle(List<Long> itemIds, BookingState state, LocalDateTime now) {
        return repository.findAllByItemIdInAndStartTimeBeforeAndEndTimeAfterOrderByStartTimeDesc(itemIds, now, now);
    }
}
