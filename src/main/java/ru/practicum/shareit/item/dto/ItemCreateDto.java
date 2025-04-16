package ru.practicum.shareit.item.dto;

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
    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    private Long ownerId;
    @NotNull
    private Boolean available;
}
