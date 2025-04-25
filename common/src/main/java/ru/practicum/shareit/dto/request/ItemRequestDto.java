package ru.practicum.shareit.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import ru.practicum.shareit.dto.item.ItemBookDto;
import ru.practicum.shareit.dto.user.UserBookDto;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor
public class ItemRequestDto {
    private Long id;
    @NotBlank
    private String description;
    @NotNull
    private Long requesterId;
    private LocalDateTime created;
    private UserBookDto owner;
    private List<ItemBookDto> items;
}
