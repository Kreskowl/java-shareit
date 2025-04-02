package ru.practicum.shareit.booking.model;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Data
@Builder
public class Booking {
    private Long id;
    @NotNull
    private Item item;
    @NotNull
    private User renter;
    @FutureOrPresent
    private LocalDateTime startTime;
    @FutureOrPresent
    private LocalDateTime endTime;
    @NotNull
    private Status status;
}
