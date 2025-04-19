package ru.practicum.shareit.dto.booking;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.shareit.dto.ShareConstants;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor
public class BookingCreateDto {
    @NotNull
    private Long itemId;
    @NotNull
    @DateTimeFormat(pattern = ShareConstants.DATE_TIME_PATTERN)
    private LocalDateTime start;
    @NotNull
    @DateTimeFormat(pattern = ShareConstants.DATE_TIME_PATTERN)
    private LocalDateTime end;
}
