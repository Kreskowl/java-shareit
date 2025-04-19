package ru.practicum.shareit.userIntegrationTest;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.practicum.shareit.IntegrationTestBase;
import ru.practicum.shareit.dto.user.UserCreateDto;
import ru.practicum.shareit.dto.user.UserDto;
import ru.practicum.shareit.dto.user.UserUpdateDto;
import ru.practicum.shareit.user.UserService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class UserServiceTest extends IntegrationTestBase {

    @Autowired
    private UserService service;

    @Test
    void shouldSaveToDB() {
        UserCreateDto userDto = new UserCreateDto("third", "newby@yandex.ru");
        UserDto result = service.createUser(userDto);

        assertThat(userRepository.findAll()).hasSize(3);
        assertThat(result.getName()).isEqualTo("third");
        assertThat(result.getEmail()).isEqualTo("newby@yandex.ru");
    }

    @Test
    void shouldFindById() {
        UserDto result = service.findById(getUser().getId());

        assertThat(result.getName()).isEqualTo("Test");
        assertThat(result.getEmail()).isEqualTo("test@example.com");
    }

    @Test
    void shouldDeleteFromDB() {
        UserCreateDto userDto = new UserCreateDto("third", "newby@yandex.ru");
        UserDto result = service.createUser(userDto);
        service.deleteById(getSecondUser().getId());

        assertThat(userRepository.findAll()).hasSize(2);
        assertThat(service.findById(getUser().getId()).getName()).isEqualTo("Test");
        assertThat(service.findById(getUser().getId()).getEmail()).isEqualTo("test@example.com");
    }

    @Test
    void shouldGetAllFromDB() {
        List<UserDto> result = service.getAllUsers();

        assertThat(userRepository.findAll()).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("Test");
        assertThat(result.get(0).getEmail()).isEqualTo("test@example.com");
        assertThat(result.get(1).getName()).isEqualTo("integrior");
        assertThat(result.get(1).getEmail()).isEqualTo("waffle@yandex.ru");
    }

    @Test
    void shouldUpdateNameWithoutEmailInDB() {
        UserUpdateDto update = new UserUpdateDto("Felix", null);
        UserDto updated = service.update(getUser().getId(), update);

        assertThat(updated.getName()).isEqualTo("Felix");
        assertThat(updated.getEmail()).isEqualTo("test@example.com");
    }

    @Test
    void shouldUpdateEmailWithoutNameInDB() {
        UserUpdateDto update = new UserUpdateDto(null, "owly@gmail.com");
        UserDto updated = service.update(getUser().getId(), update);

        assertThat(updated.getName()).isEqualTo("Test");
        assertThat(updated.getEmail()).isEqualTo("owly@gmail.com");
    }

    @Test
    void shouldUpdateFullInfoInDB() {
        UserUpdateDto update = new UserUpdateDto("Felix", "owly@gmail.com");
        UserDto updated = service.update(getUser().getId(), update);

        assertThat(updated.getName()).isEqualTo("Felix");
        assertThat(updated.getEmail()).isEqualTo("owly@gmail.com");
    }

}
