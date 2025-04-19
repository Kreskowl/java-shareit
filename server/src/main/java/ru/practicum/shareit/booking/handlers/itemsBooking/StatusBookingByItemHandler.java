package ru.practicum.shareit.booking.handlers.itemsBooking;

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
public class StatusBookingByItemHandler implements BookingByItemHandler {
    private final BookingRepository repository;

    @Override
    public boolean canHandle(BookingState state) {
        return BookingStateMapper.toStatus(state).isPresent();
    }

    @Override
    public List<Booking> handle(List<Long> itemIds, BookingState state, LocalDateTime now) {
        Status status = BookingStateMapper.toStatus(state)
                .orElseThrow(() -> new IllegalArgumentException("Unknown booking state: " + state));
        return repository.findAllByItemIdInAndStatusOrderByStartTimeDesc(itemIds, status);
    }

}
