package ru.practicum.shareIt.json.itemJsonTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareIt.json.BaseDtoJsonTest;
import ru.practicum.shareit.dto.booking.BookingShortDto;
import ru.practicum.shareit.dto.item.CommentCreateDto;
import ru.practicum.shareit.dto.item.CommentDto;
import ru.practicum.shareit.dto.item.ItemCreateDto;
import ru.practicum.shareit.dto.item.ItemResponseDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class ItemToJsonTest extends BaseDtoJsonTest {
    private static final Long USER_ID = 1L;
    private static final Long ITEM_ID = 1L;
    private static final Long COMMENT_ID = 1L;
    private static final Long REQUEST_ID = 1L;
    private static final Long BOOKING_ID = 1L;
    private static final LocalDateTime CREATED = LocalDateTime.of(2025, 5, 19, 5, 0);
    @Autowired
    private JacksonTester<CommentCreateDto> jsonCreateCommentTesting;
    @Autowired
    private JacksonTester<CommentDto> jsonCommentTesting;
    @Autowired
    private JacksonTester<ItemCreateDto> jsonItemCreateTesting;
    @Autowired
    private JacksonTester<ItemResponseDto> jsonItemResponseTesting;
    
    @Test
    void shouldSerializeCommentCreateDto() throws Exception {
        CommentCreateDto dto = CommentCreateDto.builder().text("test").build();
        JsonContent<CommentCreateDto> json = jsonCreateCommentTesting.write(dto);
        assertThat(json).extractingJsonPathStringValue("$.text").isEqualTo("test");
    }
    
    @Test
    void shouldSerializeCommentDto() throws Exception {
        CommentDto dto = CommentDto.builder().id(COMMENT_ID).text("test").itemId(ITEM_ID).authorName("Bob").created(CREATED).build();
        JsonContent<CommentDto> json = jsonCommentTesting.write(dto);
        assertThat(json).extractingJsonPathNumberValue("$.id").isEqualTo(COMMENT_ID.intValue());
        assertThat(json).extractingJsonPathStringValue("$.text").isEqualTo("test");
        assertThat(json).extractingJsonPathNumberValue("$.itemId").isEqualTo(ITEM_ID.intValue());
        assertThat(json).extractingJsonPathStringValue("$.authorName").isEqualTo("Bob");
        assertThat(json).extractingJsonPathStringValue("$.created").isEqualTo("2025-05-19T05:00:00");
    }
    
    @Test
    void shouldSerializeItemCreateDto() throws Exception {
        ItemCreateDto dto = ItemCreateDto.builder().name("drill").description("fancy").ownerId(USER_ID).available(true).requestId(REQUEST_ID).build();
        JsonContent<ItemCreateDto> json = jsonItemCreateTesting.write(dto);
        assertThat(json).extractingJsonPathStringValue("$.name").isEqualTo("drill");
        assertThat(json).extractingJsonPathStringValue("$.description").isEqualTo("fancy");
        assertThat(json).extractingJsonPathNumberValue("$.ownerId").isEqualTo(USER_ID.intValue());
        assertThat(json).extractingJsonPathBooleanValue("$.available").isTrue();
        assertThat(json).extractingJsonPathNumberValue("$.requestId").isEqualTo(REQUEST_ID.intValue());
    }
    
    @Test
    void shouldSerializeItemResponseDto() throws Exception {
        ItemResponseDto dto = ItemResponseDto.builder().id(ITEM_ID).name("drill").description("fancy").ownerId(USER_ID).available(true).requestId(REQUEST_ID).comments(List.of()).lastBooking(new BookingShortDto(BOOKING_ID, USER_ID)).nextBooking(new BookingShortDto(BOOKING_ID, USER_ID)).build();
        JsonContent<ItemResponseDto> json = jsonItemResponseTesting.write(dto);
        assertThat(json).extractingJsonPathNumberValue("$.id").isEqualTo(ITEM_ID.intValue());
        assertThat(json).extractingJsonPathStringValue("$.name").isEqualTo("drill");
        assertThat(json).extractingJsonPathStringValue("$.description").isEqualTo("fancy");
        assertThat(json).extractingJsonPathNumberValue("$.ownerId").isEqualTo(USER_ID.intValue());
        assertThat(json).extractingJsonPathBooleanValue("$.available").isTrue();
        assertThat(json).extractingJsonPathNumberValue("$.requestId").isEqualTo(REQUEST_ID.intValue());
        assertThat(json).extractingJsonPathArrayValue("$.comments").isEmpty();
        assertThat(json).extractingJsonPathNumberValue("$.lastBooking.id").isEqualTo(BOOKING_ID.intValue());
        assertThat(json).extractingJsonPathNumberValue("$.lastBooking.bookerId").isEqualTo(USER_ID.intValue());
        assertThat(json).extractingJsonPathNumberValue("$.nextBooking.id").isEqualTo(BOOKING_ID.intValue());
        assertThat(json).extractingJsonPathNumberValue("$.nextBooking.bookerId").isEqualTo(USER_ID.intValue());
    }
    
    @Test
    void shouldDeserializeCommentCreateDto() throws Exception {
        String json = """
                {
                    "text": "test"
                }
                """;
        CommentCreateDto dto = jsonCreateCommentTesting.parseObject(json);
        assertThat(dto.getText()).isEqualTo("test");
    }
    
    @Test
    void shouldDeserializeCommentDto() throws Exception {
        String json = """
                {
                    "id": "1",
                    "text": "test",
                    "itemId": 2,
                    "authorName": "Bob",
                    "created": "2025-05-19T05:00:00"
                }
                """;
        CommentDto dto = jsonCommentTesting.parseObject(json);
        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getText()).isEqualTo("test");
        assertThat(dto.getItemId()).isEqualTo(2L);
        assertThat(dto.getAuthorName()).isEqualTo("Bob");
        assertThat(dto.getCreated()).isEqualTo(CREATED);
    }
    
    @Test
    void shouldDeserializeItemCreateDto() throws Exception {
        String json = """
                {
                    "name": "drill",
                    "description": "fancy",
                    "ownerId": 1,
                    "available": true,
                    "requestId": 5
                }
                """;
        ItemCreateDto dto = jsonItemCreateTesting.parseObject(json);
        assertThat(dto.getName()).isEqualTo("drill");
        assertThat(dto.getDescription()).isEqualTo("fancy");
        assertThat(dto.getOwnerId()).isEqualTo(1L);
        assertThat(dto.getAvailable()).isTrue();
        assertThat(dto.getRequestId()).isEqualTo(5L);
    }
    
    @Test
    void shouldDeserializeItemResponseDto() throws Exception {
        String json = """
                {
                    "id": 1,
                    "name": "drill",
                    "description": "fancy",
                    "ownerId": 1,
                    "available": true,
                    "requestId": 5,
                    "comments": [],
                    "lastBooking": { "id": 1, "bookerId": 1 },
                    "nextBooking": { "id": 1, "bookerId": 1 }
                }
                """;
        ItemResponseDto dto = jsonItemResponseTesting.parseObject(json);
        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getName()).isEqualTo("drill");
        assertThat(dto.getDescription()).isEqualTo("fancy");
        assertThat(dto.getOwnerId()).isEqualTo(1L);
        assertThat(dto.getAvailable()).isTrue();
        assertThat(dto.getRequestId()).isEqualTo(5L);
        assertThat(dto.getComments()).isEmpty();
        assertThat(dto.getLastBooking().getId()).isEqualTo(1L);
        assertThat(dto.getLastBooking().getBookerId()).isEqualTo(1L);
        assertThat(dto.getNextBooking().getId()).isEqualTo(1L);
        assertThat(dto.getNextBooking().getBookerId()).isEqualTo(1L);
    }
}
