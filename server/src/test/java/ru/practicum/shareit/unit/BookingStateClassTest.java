package ru.practicum.shareit.unit;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.test.context.ContextConfiguration;
import ru.practicum.shareit.ShareItServer;
import ru.practicum.shareit.booking.mapper.BookingStateMapper;
import ru.practicum.shareit.dto.booking.BookingState;
import ru.practicum.shareit.dto.booking.Status;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ContextConfiguration(classes = ShareItServer.class)
public class BookingStateClassTest {

    @ParameterizedTest
    @EnumSource(value = BookingState.class)
    void shouldMapBookingStateToStatusOrEmpty(BookingState state) {
        Optional<Status> result = BookingStateMapper.toStatus(state);

        switch (state) {
            case WAITING -> assertThat(result).contains(Status.WAITING);
            case APPROVED -> assertThat(result).contains(Status.APPROVED);
            case REJECTED -> assertThat(result).contains(Status.REJECTED);
            case CANCELED -> assertThat(result).contains(Status.CANCELED);
            default -> assertThat(result).isEmpty();
        }
    }
}
