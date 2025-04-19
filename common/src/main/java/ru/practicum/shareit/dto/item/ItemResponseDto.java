package ru.practicum.shareit.dto.item;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.dto.booking.BookingShortDto;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class ItemResponseDto {
    private Long id;
    private String name;
    private String description;
    private Long ownerId;
    private Boolean available;
    private Long requestId;
    @Builder.Default
    private List<CommentDto> comments = new ArrayList<>();
    private BookingShortDto lastBooking;
    private BookingShortDto nextBooking;
}
