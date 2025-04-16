package ru.practicum.shareit.request.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class ItemRequestDto {
    private Long id;
    @NotBlank
    private String description;
    @NotNull
    private Long requesterId;
    @FutureOrPresent
    private LocalDateTime created;
}
