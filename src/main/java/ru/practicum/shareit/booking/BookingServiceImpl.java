package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.mapper.BookingStateMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.ForbiddenException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemBookDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepositoryImpl;
import ru.practicum.shareit.user.dto.UserBookDto;
import ru.practicum.shareit.user.repository.UserRepositoryImpl;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {
    private final BookingRepository storage;
    private final ItemRepositoryImpl itemRepository;
    private final BookingMapper bookingMapper;
    private final UserRepositoryImpl userRepository;

    @Override
    @Transactional
    public BookingDto createBooking(BookingCreateDto dto, Long userId) {
        Item item;
        try {
            item = (itemRepository.findById(dto.getItemId())
                    .orElseThrow(() -> new NotFoundException("Item with id " + dto.getItemId() + " not found")));

        } catch (DataIntegrityViolationException itemException) {
            throw new NotFoundException("Item with id " + dto.getItemId() + " not found");
        }

        try {
            userRepository.findById(userId)
                    .orElseThrow(() -> new NotFoundException("User with id " + userId + " not found"));
        } catch (DataIntegrityViolationException userException) {
            throw new NotFoundException("User with id " + userId + " not found");
        }

        if (!item.getAvailable()) {
            throw new ValidationException("Item with id " + item.getId() + " is not available for booking");
        }
        Booking booking = bookingMapper.createDtoToBooking(dto, userId);
        booking.setStatus(Status.WAITING);
        Booking saved = storage.save(booking);

        BookingDto result = bookingMapper.bookingToDto(saved);
        result.setItem(ItemBookDto.builder()
                .id(item.getId())
                .name(item.getName())
                .build());

        result.setBooker(UserBookDto.builder()
                .id(booking.getRenterId())
                .build());

        return result;
    }

    @Override
    public BookingDto findById(long id) {
        Booking booking = storage.findById(id)
                .orElseThrow(() -> new NotFoundException("Booking with id " + id + " not found"));

        Item item = (itemRepository.findById(booking.getItemId())
                .orElseThrow(() -> new NotFoundException("Item with id " + id + " not found")));
        Booking updatedBooking = storage.save(booking);
        BookingDto result = bookingMapper.bookingToDto(updatedBooking);
        result.setItem(ItemBookDto.builder()
                .id(item.getId())
                .name(item.getName())
                .build());
        return result;

    }

    @Override
    public List<BookingDto> getAllCurrentBookings(Long userId, BookingState state) {
        LocalDateTime now = LocalDateTime.now();
        List<Booking> bookings;

        switch (state) {
            case CURRENT ->
                    bookings = storage.findByRenterIdAndStartTimeBeforeAndEndTimeAfterOrderByStartTimeDesc(userId, now, now);
            case PAST -> bookings = storage.findByRenterIdAndEndTimeBeforeOrderByStartTimeDesc(userId, now);
            case FUTURE -> bookings = storage.findByRenterIdAndStartTimeAfterOrderByStartTimeDesc(userId, now);
            case ALL -> bookings = storage.findByRenterIdOrderByStartTimeDesc(userId);
            default -> {
                Status status = BookingStateMapper.toStatus(state)
                        .orElseThrow(() -> new IllegalArgumentException("Unknown booking state: " + state));
                bookings = storage.findByRenterIdAndStatusOrderByStartTimeDesc(userId, status);
            }
        }

        return bookings.stream()
                .map(booking -> {
                    BookingDto dto = bookingMapper.bookingToDto(booking);
                    Item item = itemRepository.findById(booking.getItemId())
                            .orElseThrow(() -> new NotFoundException("Item not found"));
                    dto.setItem(ItemBookDto.builder()
                            .id(item.getId())
                            .name(item.getName())
                            .build());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingDto> getAllBookedItems(Long ownerId, BookingState state) {
        try {
            userRepository.findById(ownerId)
                    .orElseThrow(() -> new NotFoundException("User with id " + ownerId + " not found"));
        } catch (DataIntegrityViolationException userException) {
            throw new NotFoundException("User with id " + ownerId + " not found");
        }

        List<Long> itemIds = itemRepository.findAllByOwnerId(ownerId).stream()
                .map(Item::getId)
                .collect(Collectors.toList());

        if (itemIds.isEmpty()) {
            return Collections.emptyList();
        }

        LocalDateTime now = LocalDateTime.now();
        List<Booking> bookings;

        switch (state) {
            case CURRENT ->
                    bookings = storage.findAllByItemIdInAndStartTimeBeforeAndEndTimeAfterOrderByStartTimeDesc(itemIds, now, now);
            case PAST -> bookings = storage.findAllByItemIdInAndEndTimeBeforeOrderByStartTimeDesc(itemIds, now);
            case FUTURE -> bookings = storage.findAllByItemIdInAndStartTimeAfterOrderByStartTimeDesc(itemIds, now);
            case ALL -> bookings = storage.findAllByItemIdInOrderByStartTimeDesc(itemIds);
            default -> {
                Status status = BookingStateMapper.toStatus(state)
                        .orElseThrow(() -> new IllegalArgumentException("Unknown booking state: " + state));
                bookings = storage.findAllByItemIdInAndStatusOrderByStartTimeDesc(itemIds, status);
            }
        }

        return bookings.stream()
                .map(bookingMapper::bookingToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        storage.deleteById(id);
    }

    @Override
    @Transactional
    public BookingDto updateStatusByOwner(long bookingId, long ownerId, boolean approved) {
        Booking booking = storage.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Booking with id " + bookingId + " not found"));

        Item item = itemRepository.findById(booking.getItemId())
                .orElseThrow(() -> new NotFoundException("Item with id " + booking.getItemId() + " not found"));


        if (!Objects.equals(item.getOwnerId(), ownerId)) {
            throw new ForbiddenException("User with id " + ownerId + " is not the owner of this item");
        }

        booking.setStatus(approved ? Status.APPROVED : Status.REJECTED);

        Booking updatedBooking = storage.save(booking);
        BookingDto result = bookingMapper.bookingToDto(updatedBooking);
        result.setItem(ItemBookDto.builder()
                .id(item.getId())
                .name(item.getName())
                .build());
        return result;
    }

}
