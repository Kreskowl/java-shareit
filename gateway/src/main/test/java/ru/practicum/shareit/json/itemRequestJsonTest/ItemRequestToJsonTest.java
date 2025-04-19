package ru.practicum.shareit.json.itemRequestJsonTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.dto.request.ItemRequestCreateDto;
import ru.practicum.shareit.dto.request.ItemRequestDto;
import ru.practicum.shareit.dto.user.UserBookDto;
import ru.practicum.shareit.json.BaseDtoJsonTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class ItemRequestToJsonTest extends BaseDtoJsonTest {
    @Autowired
    private JacksonTester<ItemRequestCreateDto> jsonRequestCreateTesting;
    @Autowired
    private JacksonTester<ItemRequestDto> jsonRequestResponseTesting;

    private static final Long REQUEST_ID = 1L;
    private static final Long USER_ID = 1L;
    private static final LocalDateTime CREATED = LocalDateTime.of(2025, 5, 19, 5, 0);


    @Test
    void shouldSerializeItemRequestCreateDto() throws Exception {
        ItemRequestCreateDto dto = ItemRequestCreateDto.builder()
                .description("test")
                .build();
        JsonContent<ItemRequestCreateDto> json = jsonRequestCreateTesting.write(dto);

        assertThat(json).extractingJsonPathStringValue("$.description").isEqualTo("test");
    }

    @Test
    void shouldSerializeItemRequestDto() throws Exception {
        ItemRequestDto dto = ItemRequestDto.builder()
                .id(REQUEST_ID)
                .description("test")
                .requesterId(USER_ID)
                .created(CREATED)
                .owner(new UserBookDto(USER_ID))
                .items(List.of())
                .build();
        JsonContent<ItemRequestDto> json = jsonRequestResponseTesting.write(dto);

        assertThat(json).extractingJsonPathNumberValue("$.id").isEqualTo(REQUEST_ID.intValue());
        assertThat(json).extractingJsonPathStringValue("$.description").isEqualTo("test");
        assertThat(json).extractingJsonPathNumberValue("$.requesterId").isEqualTo(USER_ID.intValue());
        assertThat(json).extractingJsonPathStringValue("$.created").isEqualTo("2025-05-19T05:00:00");
        assertThat(json).extractingJsonPathNumberValue("$.owner.id").isEqualTo(USER_ID.intValue());
        assertThat(json).extractingJsonPathArrayValue("$.items").isEmpty();
    }

    @Test
    void shouldDeserializeItemRequestCreateDto() throws Exception {
        String json = """
                    { "description": "test" }
                """;

        ItemRequestCreateDto dto = jsonRequestCreateTesting.parseObject(json);
        assertThat(dto.getDescription()).isEqualTo("test");
    }

    @Test
    void shouldDeserializeItemRequestDto() throws Exception {
        String json = """
                    { "id": 1,
                    "description": "test",
                    "requesterId": 1,
                    "created": "2025-05-19T05:00:00",
                    "owner": { "id": 1 },
                    "items": []
                    }
                """;

        ItemRequestDto dto = jsonRequestResponseTesting.parseObject(json);

        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getDescription()).isEqualTo("test");
        assertThat(dto.getRequesterId()).isEqualTo(1L);
        assertThat(dto.getCreated()).isEqualTo(CREATED);
        assertThat(dto.getOwner().getId()).isEqualTo(1L);
        assertThat(dto.getItems()).isEmpty();
    }


}
