package ru.practicum.shareit.dto.item;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemUpdateDto {
    private String name;
    private String description;
    private Boolean available;
    private Long ownerId;
}
