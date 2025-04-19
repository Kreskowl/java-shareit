package ru.practicum.shareit.bookingIntegrationTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.practicum.shareit.IntegrationTestBase;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.dto.booking.BookingCreateDto;
import ru.practicum.shareit.dto.booking.BookingDto;
import ru.practicum.shareit.dto.booking.BookingState;
import ru.practicum.shareit.dto.booking.Status;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class BookingServiceTests extends IntegrationTestBase {
    @Autowired
    private BookingService service;
    private static final LocalDateTime START = LocalDateTime.of(2030, 1, 1, 10, 0);
    private static final LocalDateTime END = START.plusDays(1);

    @Test
    void shouldSaveInDB() {
        BookingCreateDto newBooking =
                new BookingCreateDto(getItem().getId(), START, END);
        BookingDto result = service.createBooking(newBooking, getSecondUser().getId());
        Long savedId = result.getId();

        BookingDto idCheck = service.findById(savedId);
        assertThat(result.getId()).isEqualTo(savedId);

        assertThat(bookingRepository.findAll()).hasSize(2);
        assertThat(result.getBooker().getId()).isEqualTo(getSecondUser().getId());
        assertThat(result.getItem().getId()).isEqualTo(getItem().getId());
        assertThat(result.getStatus()).isEqualTo(Status.WAITING);
        assertThat(result.getStart()).isEqualTo(START);
        assertThat(result.getEnd()).isEqualTo(END);
    }

    @Test
    void shouldFindById() {
        BookingDto result = service.findById(getBooking().getId());

        assertThat(result.getId()).isEqualTo(getBooking().getId());
        assertThat(result.getBooker().getId()).isEqualTo(getSecondUser().getId());
        assertThat(result.getItem().getId()).isEqualTo(getBooking().getItemId());
        assertThat(result.getStatus()).isEqualTo(Status.APPROVED);
    }

    @Test
    void shouldDeleteFromDB() {
        BookingCreateDto newBooking =
                new BookingCreateDto(getItem().getId(), START, END);
        BookingDto created = service.createBooking(newBooking, getSecondUser().getId());
        BookingDto result = service.findById(created.getId());
        service.deleteById(getBooking().getId());
        Long savedId = result.getId();

        BookingDto idCheck = service.findById(savedId);
        assertThat(result.getId()).isEqualTo(savedId);

        assertThat(bookingRepository.findAll()).hasSize(1);
        assertThat(result.getBooker().getId()).isEqualTo(getSecondUser().getId());
        assertThat(result.getItem().getId()).isEqualTo(getItem().getId());
        assertThat(result.getStatus()).isEqualTo(Status.WAITING);
        assertThat(result.getStart()).isEqualTo(START);
        assertThat(result.getEnd()).isEqualTo(END);
    }

    @Test
    void shouldUpdateBookingStatusIfOwner() {
        BookingCreateDto newBooking =
                new BookingCreateDto(getItem().getId(), START, END);
        BookingDto result = service.createBooking(newBooking, getSecondUser().getId());

        service.updateStatusByOwner(result.getId(), getUser().getId(), true);

        BookingDto updated = service.findById(result.getId());

        assertThat(updated.getStatus()).isEqualTo(Status.APPROVED);
    }

    @Test
    void shouldReturnCurrentBookingsForUser() {
        LocalDateTime start = LocalDateTime.now().minusHours(1);
        LocalDateTime end = LocalDateTime.now().plusHours(1);

        BookingCreateDto dto = new BookingCreateDto(getItem().getId(), start, end);
        BookingDto saved = service.createBooking(dto, getUser().getId());

        List<BookingDto> currentBookings = service.getAllCurrentBookings(getUser().getId(), BookingState.CURRENT);

        assertThat(currentBookings).hasSize(1);
        assertThat(currentBookings.getFirst().getId()).isEqualTo(saved.getId());
        assertThat(currentBookings.getFirst().getItem().getName()).isEqualTo("Hammer");
    }

    @Test
    void shouldReturnFutureBookingsForOwner() {
        LocalDateTime start = LocalDateTime.now().plusHours(1);
        LocalDateTime end = start.plusHours(2);

        BookingCreateDto dto = new BookingCreateDto(getItem().getId(), start, end);
        service.createBooking(dto, getSecondUser().getId());


        List<BookingDto> futureBookings = service.getAllBookedItems(getItem().getOwnerId(), BookingState.FUTURE);

        assertThat(futureBookings).hasSize(1);
        assertThat(futureBookings.getFirst().getItem().getId()).isEqualTo(getItem().getId());
    }

    @Test
    void shouldReturnEmptyListIfNoBookings() {
        List<BookingDto> firstUser = service.getAllCurrentBookings(getUser().getId(), BookingState.PAST);
        List<BookingDto> secondUser = service.getAllCurrentBookings(getSecondUser().getId(), BookingState.WAITING);


        assertThat(firstUser).isEmpty();
        assertThat(secondUser).isEmpty();
    }
}
