package ru.practicum.shareit.booking;

import ru.practicum.shareit.dto.booking.BookingCreateDto;
import ru.practicum.shareit.dto.booking.BookingDto;
import ru.practicum.shareit.dto.booking.BookingState;

import java.util.List;

public interface BookingService {
    BookingDto createBooking(BookingCreateDto dto, Long userId);

    BookingDto updateStatusByOwner(long bookingId, long ownerId, boolean approved);

    BookingDto findById(long id);

    List<BookingDto> getAllCurrentBookings(Long userId, BookingState status);

    List<BookingDto> getAllBookedItems(Long userId, BookingState status);

    void deleteById(long id);
}
