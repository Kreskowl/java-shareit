package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.handlers.itemsBooking.BookingByItemQueryChain;
import ru.practicum.shareit.booking.handlers.userBooking.BookingQueryChain;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.ForbiddenException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemBookDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.dto.UserBookDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {
    private final BookingQueryChain queryChain;
    private final BookingByItemQueryChain bookingByItemQueryChain;
    private final BookingRepository storage;
    private final ItemRepository itemRepository;
    private final BookingMapper bookingMapper;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public BookingDto createBooking(BookingCreateDto dto, Long userId) {
        Item item = ifItemExists(dto.getItemId());
        ifUserExists(userId);

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

        Item item = ifItemExists(booking.getItemId());
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

        List<Booking> bookings = queryChain.getBookings(userId, state, now);

        return bookings.stream()
                .map(booking -> {
                    BookingDto dto = bookingMapper.bookingToDto(booking);
                    Item item = ifItemExists(booking.getItemId());
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
        ifUserExists(ownerId);

        List<Long> itemIds = itemRepository.findAllByOwnerId(ownerId).stream()
                .map(Item::getId)
                .toList();

        if (itemIds.isEmpty()) {
            return Collections.emptyList();
        }

        LocalDateTime now = LocalDateTime.now();
        List<Booking> bookings = bookingByItemQueryChain.getBookings(itemIds, state, now);

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
        Item item = ifItemExists(booking.getItemId());

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

    private Item ifItemExists(long itemId) {
        return Optional.ofNullable(itemRepository.findById(itemId))
                .orElseThrow(() -> new NotFoundException("Item with id " + itemId + " not found"));
    }

    private User ifUserExists(long userId) {
        return Optional.ofNullable(userRepository.findById(userId))
                .orElseThrow(() -> new NotFoundException("User with id " + userId + " not found"));
    }

}
