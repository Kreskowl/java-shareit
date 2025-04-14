package ru.practicum.shareit.booking.mapper;

import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.Status;

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
