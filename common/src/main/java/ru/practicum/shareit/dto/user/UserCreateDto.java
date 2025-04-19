package ru.practicum.shareit.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class UserCreateDto {
    @NotBlank
    private String name;
    @Email
    @NotBlank
    @Pattern(regexp = "\\S+")
    private String email;
}
