package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.booking.dto.BookingShortDto;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class ItemResponseDto {
    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    private Long ownerId;
    @NotNull
    private Boolean available;
    private Long requestId;
    @Builder.Default
    private List<CommentDto> comments = new ArrayList<>();
    private BookingShortDto lastBooking;
    private BookingShortDto nextBooking;
}
