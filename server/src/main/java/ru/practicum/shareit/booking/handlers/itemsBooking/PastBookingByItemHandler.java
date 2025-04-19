package ru.practicum.shareit.booking.handlers.itemsBooking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.dto.booking.BookingState;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class PastBookingByItemHandler implements BookingByItemHandler {
    private final BookingRepository repository;

    @Override
    public boolean canHandle(BookingState state) {
        return state == BookingState.PAST;
    }

    @Override
    public List<Booking> handle(List<Long> itemIds, BookingState state, LocalDateTime now) {
        log.debug("Handling PAST bookings for users {}", itemIds);
        return repository.findAllByItemIdInAndEndTimeBeforeOrderByStartTimeDesc(itemIds, now);
    }
}
