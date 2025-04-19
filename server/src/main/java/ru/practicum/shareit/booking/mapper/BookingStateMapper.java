package ru.practicum.shareit.booking.mapper;

import ru.practicum.shareit.dto.booking.BookingState;
import ru.practicum.shareit.dto.booking.Status;

import java.util.Optional;

public class BookingStateMapper {
    public static Optional<Status> toStatus(BookingState state) {
        return switch (state) {
            case WAITING -> Optional.of(Status.WAITING);
            case APPROVED -> Optional.of(Status.APPROVED);
            case REJECTED -> Optional.of(Status.REJECTED);
            case CANCELED -> Optional.of(Status.CANCELED);
            default -> Optional.empty();
        };
    }
}
