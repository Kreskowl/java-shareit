package ru.practicum.shareit.user;

import ru.practicum.shareit.dto.user.UserCreateDto;
import ru.practicum.shareit.dto.user.UserDto;
import ru.practicum.shareit.dto.user.UserUpdateDto;

import java.util.List;


public interface UserService {

    UserDto createUser(UserCreateDto dto);

    UserDto findById(long id);

    List<UserDto> getAllUsers();

    void deleteById(long id);

    UserDto update(long id, UserUpdateDto dto);
}
