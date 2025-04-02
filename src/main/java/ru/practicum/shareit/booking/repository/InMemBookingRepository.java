package ru.practicum.shareit.booking.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.NotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
@RequiredArgsConstructor
public class InMemBookingRepository implements BookingRepository {
    private final HashMap<Long, Booking> storage;
    private final AtomicLong generator = new AtomicLong(0);

    @Override
    public Booking save(Booking booking) {
        if (booking.getId() == null) {
            booking.setId(generator.incrementAndGet());
        }
        storage.put(booking.getId(), booking);
        return booking;
    }

    @Override
    public List<Booking> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public Booking findById(long id) {
        return ifExists(id);
    }

    @Override
    public void deleteById(long id) {
        ifExists(id);
        storage.remove(id);
    }

    @Override
    public void update(Booking booking) {
        Booking existing = findById(booking.getId());
        existing.setItem(booking.getItem());
        existing.setStatus(booking.getStatus());
        existing.setRenter(booking.getRenter());
        existing.setStartTime(booking.getStartTime());
        existing.setEndTime(booking.getEndTime());
        storage.put(existing.getId(), existing);
    }

    private Booking ifExists(long id) {
        return Optional.ofNullable(storage.get(id))
                .orElseThrow(() -> new NotFoundException("Booking with id " + id + " not found"));
    }
}
