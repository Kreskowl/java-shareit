package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;

@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService service;

    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable long id) {
        return service.findById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable long id) {
        service.deleteById(id);
    }

    @PatchMapping("/{id}")
    public UserDto updateUser(@PathVariable long id, @Valid @RequestBody UserUpdateDto dto) {
        return service.update(id, dto);
    }

    @PostMapping
    public UserDto createUser(@Valid @RequestBody UserDto dto) {
        return service.createUser(dto);
    }
}
