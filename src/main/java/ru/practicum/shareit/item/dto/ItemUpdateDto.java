package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class ItemUpdateDto {
    private String name;
    private String description;
    private Boolean available;
    private Long ownerId;
}
