package ru.practicum.shareit.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class UserUpdateDto {
    @Size(min = 1, message = "Name must not be empty")
    private String name;
    @Email
    @Pattern(regexp = "\\S+")
    private String email;
}
