package ru.practicum.shareit.dto.item;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PUBLIC)
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
