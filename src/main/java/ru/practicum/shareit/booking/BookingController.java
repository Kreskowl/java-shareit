package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.booking.model.Booking;

@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService service;

    @GetMapping("/{id}")
    public Booking getBookingById(@PathVariable long id) {
        return service.findById(id);
    }
}
