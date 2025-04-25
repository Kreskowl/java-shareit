package ru.practicum.shareit.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor
public class UserUpdateDto {
    @Size(min = 1, message = "Name must not be empty")
    private String name;
    @Email
    @Pattern(regexp = "\\S+")
    private String email;
}
