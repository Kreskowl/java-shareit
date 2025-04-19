package ru.practicum.shareIt.unit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.dto.booking.BookingCreateDto;
import ru.practicum.shareit.dto.booking.BookingDto;
import ru.practicum.shareit.dto.booking.Status;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class BookingMapperTest {

    private final BookingMapper mapper = Mappers.getMapper(BookingMapper.class);

    private static final long BOOKING_ID = 1L;
    private static final long ITEM_ID = 2L;
    private static final long USER_ID = 3L;
    private static final LocalDateTime START = LocalDateTime.of(2030, 1, 1, 12, 0);
    private static final LocalDateTime END = LocalDateTime.of(2030, 1, 1, 13, 0);

    @Test
    void shouldMapToBookingDto() {
        Booking booking = Booking.builder().id(BOOKING_ID).startTime(START).endTime(END).itemId(ITEM_ID).renterId(USER_ID).status(Status.WAITING).build();

        BookingDto dto = mapper.bookingToDto(booking);

        assertThat(dto.getId()).isEqualTo(BOOKING_ID);
        assertThat(dto.getStart()).isEqualTo(START);
        assertThat(dto.getEnd()).isEqualTo(END);
        assertThat(dto.getItem().getId()).isEqualTo(ITEM_ID);
        assertThat(dto.getBooker().getId()).isEqualTo(USER_ID);
        assertThat(dto.getStatus()).isEqualTo(Status.WAITING);
    }

    @Test
    void shouldMapFromBookingCreateDto() {
        BookingCreateDto dto = new BookingCreateDto(ITEM_ID, START, END);

        Booking booking = mapper.createDtoToBooking(dto, USER_ID);

        assertThat(booking.getItemId()).isEqualTo(ITEM_ID);
        assertThat(booking.getStartTime()).isEqualTo(START);
        assertThat(booking.getEndTime()).isEqualTo(END);
    }
}
