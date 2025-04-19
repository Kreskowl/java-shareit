package ru.practicum.shareIt.json.bookingJsonTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareIt.json.BaseDtoJsonTest;
import ru.practicum.shareit.dto.booking.BookingCreateDto;
import ru.practicum.shareit.dto.booking.BookingDto;
import ru.practicum.shareit.dto.booking.Status;
import ru.practicum.shareit.dto.item.ItemBookDto;
import ru.practicum.shareit.dto.user.UserBookDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class BookingToJsonTest extends BaseDtoJsonTest {
    private static final LocalDateTime START = LocalDateTime.of(2024, 5, 1, 12, 0);
    private static final LocalDateTime END = LocalDateTime.of(2024, 5, 1, 15, 0);
    private static final Long USER_ID = 1L;
    private static final Long ITEM_ID = 1L;
    private static final Long BOOKING_ID = 1L;
    @Autowired
    private JacksonTester<BookingDto> jsonBookingTester;
    @Autowired
    private JacksonTester<BookingCreateDto> jsonCreateBookingTester;

    @Test
    void shouldSerializeBookingDto() throws Exception {
        BookingDto dto = BookingDto.builder().id(BOOKING_ID).item(new ItemBookDto(ITEM_ID, "Hammer")).booker(new UserBookDto(USER_ID)).start(START).end(END).status(Status.APPROVED).build();

        JsonContent<BookingDto> json = jsonBookingTester.write(dto);

        assertThat(json).extractingJsonPathNumberValue("$.id").isEqualTo(BOOKING_ID.intValue());
        assertThat(json).extractingJsonPathStringValue("$.item.name").isEqualTo("Hammer");
        assertThat(json).extractingJsonPathStringValue("$.status").isEqualTo("APPROVED");
        assertThat(json).extractingJsonPathStringValue("$.start").isEqualTo("2024-05-01T12:00:00");
        assertThat(json).extractingJsonPathStringValue("$.end").isEqualTo("2024-05-01T15:00:00");
    }

    @Test
    void shouldSerializeBookingCreateDto() throws Exception {
        BookingCreateDto dto = new BookingCreateDto(ITEM_ID, START, END);
        JsonContent<BookingCreateDto> json = jsonCreateBookingTester.write(dto);

        assertThat(json).extractingJsonPathNumberValue("$.itemId").isEqualTo(ITEM_ID.intValue());
        assertThat(json).extractingJsonPathStringValue("$.start").isEqualTo("2024-05-01T12:00:00");
        assertThat(json).extractingJsonPathStringValue("$.end").isEqualTo("2024-05-01T15:00:00");
    }

    @Test
    void shouldDeserializeBookingDto() throws Exception {
        String json = """
                
                {
                    "id": 1,
                    "item": { "id": 2, "name": "Hammer" },
                    "booker": { "id": 7 },
                    "start": "2030-01-01T10:00:00",
                    "end": "2030-01-01T12:00:00",
                    "status": "APPROVED"
                }
                
                """;

        BookingDto dto = jsonBookingTester.parseObject(json);
        assertThat(dto.getStatus()).isEqualTo(Status.APPROVED);
    }

    @Test
    void shouldDeserializeBookingCreateDto() throws Exception {
        String json = """
                
                {
                    "itemId": 1,
                    "start": "2030-01-01T10:00:00",
                    "end": "2030-01-01T12:00:00"
                }
                
                """;

        BookingCreateDto dto = jsonCreateBookingTester.parseObject(json);
        assertThat(dto.getItemId()).isEqualTo(ITEM_ID);
        assertThat(dto.getStart()).isEqualTo(LocalDateTime.of(2030, 1, 1, 10, 0));
    }

    @Test
    void shouldDeserializeStatusEnum() throws Exception {
        String json = """
                
                {
                    "id": 1,
                    "item": { "id": 2, "name": "Hammer" },
                    "booker": { "id": 3 },
                    "start": "2030-01-01T10:00:00",
                    "end": "2030-01-01T12:00:00",
                    "status": "REJECTED"
                }
                
                """;

        BookingDto dto = jsonBookingTester.parseObject(json);
        assertThat(dto.getStatus()).isEqualTo(Status.REJECTED);
    }
}
