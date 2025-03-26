package ru.practicum.shareit.booking.repository;

import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

public interface BookingRepository {
    Booking save(Booking booking);

    Booking findById(long id);

    List<Booking> findAll();

    void deleteById(long id);

    void update(Booking booking);
}
