package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final BookingRepository storage;

    public Booking createBooking(Booking booking) {
        return storage.save(booking);
    }

    public Booking findById(long id) {
        return storage.findById(id);
    }

    public List<Booking> findAll() {
        return storage.findAll();
    }

    public void deleteById(long id) {
        storage.deleteById(id);
    }

    public void update(Booking booking) {
        storage.update(booking);
    }
}
