package ru.practicum.shareIt.integration.bookingIntegrationTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import ru.practicum.shareIt.integration.IntegrationTestBase;
import ru.practicum.shareit.ShareItServer;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.dto.booking.BookingCreateDto;
import ru.practicum.shareit.dto.booking.BookingDto;
import ru.practicum.shareit.dto.booking.BookingState;
import ru.practicum.shareit.dto.booking.Status;
import ru.practicum.shareit.exception.ForbiddenException;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ContextConfiguration(classes = ShareItServer.class)
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
    void shouldBeForbidddenToUpdateStstusIfNotOwner() {
        BookingCreateDto newBooking =
                new BookingCreateDto(getItem().getId(), START, END);
        BookingDto result = service.createBooking(newBooking, getUser().getId());

        service.updateStatusByOwner(getItem().getId(), getUser().getId(), true);
        assertThatThrownBy(() ->
                service.updateStatusByOwner(result.getId(), getSecondUser().getId(), true)
        )
                .isInstanceOf(ForbiddenException.class)
                .hasMessageContaining("not the owner");
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
    void shouldReturnPastBookingsForUser() {
        BookingCreateDto bookingDto = new BookingCreateDto(
                getItem().getId(),
                LocalDateTime.now().minusDays(5),
                LocalDateTime.now().minusDays(2)
        );

        BookingDto saved = service.createBooking(bookingDto, getSecondUser().getId());
        service.updateStatusByOwner(saved.getId(), getUser().getId(), true);

        List<BookingDto> result = service.getAllCurrentBookings(getSecondUser().getId(), BookingState.PAST);

        assertThat(result).hasSize(2);
        assertThat(result.getFirst().getId()).isEqualTo(saved.getId());
        assertThat(result.getFirst().getStatus()).isEqualTo(Status.APPROVED);
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
