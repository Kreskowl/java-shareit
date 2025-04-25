package ru.practicum.shareit.dto.item;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor
public class ItemCreateDto {
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    private Long ownerId;
    @NotNull
    private Boolean available;
    private Long requestId;
}
