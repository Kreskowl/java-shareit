package ru.practicum.shareIt.controllers.userMockTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.ShareItServer;
import ru.practicum.shareit.dto.user.UserCreateDto;
import ru.practicum.shareit.dto.user.UserDto;
import ru.practicum.shareit.dto.user.UserUpdateDto;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.UserController;
import ru.practicum.shareit.user.UserService;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@ContextConfiguration(classes = ShareItServer.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private static final long USER_ID = 1;
    private UserDto dto;

    @BeforeEach
    void setUp() {
        dto = UserDto.builder()
                .id(USER_ID)
                .name("John")
                .email("john@example.com")
                .build();
    }

    @Test
    void shouldCreateUser() throws Exception {
        UserCreateDto createDto = new UserCreateDto("John", "john@example.com");

        when(userService.createUser(any())).thenReturn(dto);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(createDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(USER_ID))
                .andExpect(jsonPath("$.name").value("John"))
                .andExpect(jsonPath("$.email").value("john@example.com"));
    }

    @Test
    void shouldGetUserById() throws Exception {
        when(userService.findById(USER_ID)).thenReturn(dto);

        mockMvc.perform(get("/users/{id}", USER_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(USER_ID));
    }

    @Test
    void shouldUpdateUser() throws Exception {
        UserUpdateDto update = new UserUpdateDto("Johnny", null);
        UserDto updated = new UserDto(USER_ID, "Johnny", "john@example.com");

        when(userService.update(eq(USER_ID), any())).thenReturn(updated);

        mockMvc.perform(patch("/users/{id}", USER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Johnny"));
    }

    @Test
    void shouldDeleteUser() throws Exception {
        mockMvc.perform(delete("/users/{id}", USER_ID))
                .andExpect(status().isOk());

        verify(userService).deleteById(USER_ID);
    }

    @Test
    void shouldReturnBadRequestWhenEmailInvalid() throws Exception {
        UserCreateDto invalidDto = new UserCreateDto("John", "not-an-email");

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(invalidDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestForUpdateWhenEmailInvalid() throws Exception {
        UserUpdateDto invalidDto = new UserUpdateDto("John", "not-an-email");

        mockMvc.perform(patch("/users/{id}", USER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(invalidDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestWhenNameInvalid() throws Exception {
        UserCreateDto invalidDto = new UserCreateDto("", "hanabie@gmail.com");

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(invalidDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestForUpdateWhenNameInvalid() throws Exception {
        UserUpdateDto invalidDto = new UserUpdateDto("", "hanabie@gmail.com");

        mockMvc.perform(patch("/users/{id}", USER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(invalidDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnNotFoundIfUserDoesNotExist() throws Exception {
        long userId = 999L;

        when(userService.findById(userId))
                .thenThrow(new NotFoundException("User not found"));

        mockMvc.perform(get("/users/{id}", userId))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("User not found")));
    }

    @Test
    void shouldReturnNotFoundIfDeleteUserNotExists() throws Exception {
        long userId = 999L;

        doThrow(new NotFoundException("User not found"))
                .when(userService).deleteById(userId);

        mockMvc.perform(delete("/users/{id}", userId))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("User not found")));

    }

    private String toJson(Object obj) throws JsonProcessingException {
        return objectMapper.writeValueAsString(obj);
    }
}
