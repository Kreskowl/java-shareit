package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class BookingDto {
    private Long id;
    @NotNull
    private Item item;
    @NotNull
    private User renter;
    @PastOrPresent
    private LocalDateTime startTime;
    @PastOrPresent
    private LocalDateTime endTime;
    @NotNull
    private Status status;
}
