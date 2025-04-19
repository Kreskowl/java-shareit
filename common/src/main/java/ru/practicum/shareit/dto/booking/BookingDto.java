package ru.practicum.shareit.dto.booking;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.shareit.dto.ShareConstants;
import ru.practicum.shareit.dto.item.ItemBookDto;
import ru.practicum.shareit.dto.user.UserBookDto;

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
