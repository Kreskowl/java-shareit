package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.ShareConstants;
import ru.practicum.shareit.item.dto.ItemBookDto;
import ru.practicum.shareit.user.dto.UserBookDto;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor
public class BookingDto {
    private Long id;
    @NotNull
    private ItemBookDto item;
    private UserBookDto booker;
    @FutureOrPresent
    @DateTimeFormat(pattern = ShareConstants.DATE_TIME_PATTERN)
    private LocalDateTime start;
    @FutureOrPresent
    @DateTimeFormat(pattern = ShareConstants.DATE_TIME_PATTERN)
    private LocalDateTime end;
    private Status status;
}
