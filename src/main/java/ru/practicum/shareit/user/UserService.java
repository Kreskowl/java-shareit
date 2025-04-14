package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;

import java.util.List;


public interface UserService {

    UserDto createUser(UserDto dto);

    UserDto findById(long id);

    List<UserDto> getAllUsers();

    void deleteById(long id);

    UserDto update(long id, UserUpdateDto dto);
}
