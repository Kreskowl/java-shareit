package ru.practicum.shareit.user.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class User {
    private Long id;
    @NotBlank
    private String name;
    @Email
    @NotBlank
    @Pattern(regexp = "\\S+")
    private String email;
}
