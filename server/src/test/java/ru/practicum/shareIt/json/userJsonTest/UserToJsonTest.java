package ru.practicum.shareIt.json.userJsonTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareIt.json.BaseDtoJsonTest;
import ru.practicum.shareit.dto.user.UserCreateDto;
import ru.practicum.shareit.dto.user.UserDto;
import ru.practicum.shareit.dto.user.UserUpdateDto;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class UserToJsonTest extends BaseDtoJsonTest {
    private static final Long USER_ID = 1L;

    @Autowired
    private JacksonTester<UserCreateDto> jsonUserCreateTesting;

    @Autowired
    private JacksonTester<UserDto> jsonUserResponseTesting;

    @Autowired
    private JacksonTester<UserUpdateDto> jsonUserUpdateTesting;

    @Test
    void shouldSerializeUserCreateDto() throws Exception {
        UserCreateDto dto = UserCreateDto.builder()
                .name("Test")
                .email("test@index.ru")
                .build();

        JsonContent<UserCreateDto> json = jsonUserCreateTesting.write(dto);

        assertThat(json).extractingJsonPathStringValue("$.name").isEqualTo("Test");
        assertThat(json).extractingJsonPathStringValue("$.email").isEqualTo("test@index.ru");
    }

    @Test
    void shouldSerializeUserDto() throws Exception {
        UserDto dto = UserDto.builder()
                .id(USER_ID)
                .name("Test")
                .email("test@index.ru")
                .build();

        JsonContent<UserDto> json = jsonUserResponseTesting.write(dto);

        assertThat(json).extractingJsonPathNumberValue("$.id").isEqualTo(USER_ID.intValue());
        assertThat(json).extractingJsonPathStringValue("$.name").isEqualTo("Test");
        assertThat(json).extractingJsonPathStringValue("$.email").isEqualTo("test@index.ru");
    }

    @Test
    void shouldSerializeUserUpdateDto() throws Exception {
        UserUpdateDto dto = UserUpdateDto.builder()
                .name("Test")
                .email("test@index.ru")
                .build();

        JsonContent<UserUpdateDto> json = jsonUserUpdateTesting.write(dto);

        assertThat(json).extractingJsonPathStringValue("$.name").isEqualTo("Test");
        assertThat(json).extractingJsonPathStringValue("$.email").isEqualTo("test@index.ru");
    }

    @Test
    void shouldDeserializeUserCreateDto() throws Exception {
        String json = """
{
  "name": "Test",
  "email": "test@index.ru"
}
""";

        UserCreateDto dto = jsonUserCreateTesting.parseObject(json);

        assertThat(dto.getName()).isEqualTo("Test");
        assertThat(dto.getEmail()).isEqualTo("test@index.ru");
    }

    @Test
    void shouldDeserializeUserDto() throws Exception {
        String json = """
{
  "id": 1,
  "name": "Test",
  "email": "test@index.ru"
}
""";

        UserDto dto = jsonUserResponseTesting.parseObject(json);

        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getName()).isEqualTo("Test");
        assertThat(dto.getEmail()).isEqualTo("test@index.ru");
    }

    @Test
    void shouldDeserializeUserUpdateDto() throws Exception {
        String json = """
{
  "name": "Test",
  "email": "test@index.ru"
}
""";

        UserUpdateDto dto = jsonUserUpdateTesting.parseObject(json);

        assertThat(dto.getName()).isEqualTo("Test");
        assertThat(dto.getEmail()).isEqualTo("test@index.ru");
    }
}
