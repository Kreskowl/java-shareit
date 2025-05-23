package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.dto.booking.BookingShortDto;
import ru.practicum.shareit.dto.booking.Status;
import ru.practicum.shareit.dto.item.CommentCreateDto;
import ru.practicum.shareit.dto.item.CommentDto;
import ru.practicum.shareit.dto.item.ItemCreateDto;
import ru.practicum.shareit.dto.item.ItemResponseDto;
import ru.practicum.shareit.dto.item.ItemUpdateDto;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {
    private final ItemRepository repository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final ItemRequestRepository itemRequestRepository;
    private final ItemMapper mapper;

    @Override
    @Transactional
    public ItemResponseDto createItem(ItemCreateDto dto) {
        ifUserExists(dto.getOwnerId());

        if (dto.getRequestId() != null) {
            ifRequestExists(dto.getRequestId());
        }
        Item item = repository.save(mapper.createDtoToItem(dto));

        return mapper.itemToDto(item);
    }

    @Override
    public List<ItemResponseDto> findAllByOwnerId(long ownerId) {
        List<Item> items = repository.findAll().stream()
                .filter(item -> Objects.equals(item.getOwnerId(), ownerId))
                .toList();

        List<Long> itemIds = items.stream().map(Item::getId).toList();

        Map<Long, BookingShortDto> lastBookings = getLastBookingsMap(itemIds);
        Map<Long, BookingShortDto> nextBookings = getNextBookingsMap(itemIds);
        Map<Long, List<CommentDto>> commentsMap = getCommentsMap(itemIds);

        return items.stream().map(item -> {
            ItemResponseDto dto = mapper.itemToDto(item);
            dto.setLastBooking(lastBookings.get(item.getId()));
            dto.setNextBooking(nextBookings.get(item.getId()));
            dto.setComments(commentsMap.getOrDefault(item.getId(), Collections.emptyList()));
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public ItemResponseDto findById(long id, long userId) {
        Item item = ifItemExists(id);

        ItemResponseDto dto = mapper.itemToDto(item);

        dto.setComments(getCommentsMap(List.of(id)).getOrDefault(id, Collections.emptyList()));

        if (Objects.equals(item.getOwnerId(), userId)) {
            dto.setLastBooking(getLastBookingsMap(List.of(id)).get(id));
            dto.setNextBooking(getNextBookingsMap(List.of(id)).get(id));
        }

        return dto;
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        repository.deleteById(id);
    }

    @Override
    @Transactional
    public ItemResponseDto update(long ownerId, long itemId, ItemUpdateDto updatedDto) {
        Item existing = ifItemExists(itemId);
        if (!Objects.equals(existing.getOwnerId(), ownerId)) {
            throw new NotFoundException("User with id " + ownerId + " is not the owner of this item");
        }
        mapper.updateItemFromDto(updatedDto, existing);
        return mapper.itemToDto(existing);
    }

    @Override
    public List<ItemResponseDto> search(String text) {
        if (text == null || text.trim().isEmpty()) {
            return Collections.emptyList();
        }
        List<Item> items = repository.search(text);
        return items.stream()
                .filter(Item::getAvailable)
                .map(mapper::itemToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CommentDto addComment(Long itemId, Long authorId, CommentCreateDto dto) {
        LocalDateTime now = LocalDateTime.now();
        Item item = ifItemExists(itemId);

        User author = ifUserExists(authorId);

        boolean hasPastBooking = bookingRepository
                .findByItemIdAndRenterIdAndEndTimeBefore(itemId, authorId, now)
                .stream()
                .anyMatch(booking -> booking.getStatus() == Status.APPROVED);

        if (!hasPastBooking) {
            throw new ValidationException("User with id " + authorId + " has not completed a booking for item " + itemId);
        }

        Comment comment = new Comment();
        comment.setText(dto.getText());
        comment.setItemId(itemId);
        comment.setAuthorId(authorId);
        comment.setCreated(LocalDateTime.now());

        Comment saved = commentRepository.save(comment);

        return CommentDto.builder()
                .id(saved.getId())
                .text(saved.getText())
                .itemId(saved.getItemId())
                .authorName(author.getName())
                .created(saved.getCreated())
                .build();
    }

    private Map<Long, BookingShortDto> getLastBookingsMap(List<Long> itemIds) {
        return bookingRepository.findLastBookingsForItems(itemIds).stream()
                .collect(Collectors.toMap(
                        Booking::getItemId,
                        booking -> new BookingShortDto(booking.getId(), booking.getRenterId()),
                        (b1, b2) -> b1
                ));
    }

    private Map<Long, BookingShortDto> getNextBookingsMap(List<Long> itemIds) {
        return bookingRepository.findNextBookingsForItems(itemIds).stream()
                .collect(Collectors.toMap(
                        Booking::getItemId,
                        booking -> new BookingShortDto(booking.getId(), booking.getRenterId()),
                        (b1, b2) -> b1
                ));
    }

    private Map<Long, List<CommentDto>> getCommentsMap(List<Long> itemIds) {
        return commentRepository.findByItemIdIn(itemIds).stream()
                .collect(Collectors.groupingBy(
                        Comment::getItemId,
                        Collectors.mapping(comment -> CommentDto.builder()
                                        .id(comment.getId())
                                        .text(comment.getText())
                                        .created(comment.getCreated())
                                        .authorName(userRepository.findById(comment.getAuthorId())
                                                .map(User::getName)
                                                .orElse("Unknown"))
                                        .build(),
                                Collectors.toList()
                        )
                ));
    }

    private Item ifItemExists(long id) {
        Item item = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Item with id " + id + " not found"));
        return item;
    }

    private User ifUserExists(long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Item with id " + id + " not found"));
    }

    private ItemRequest ifRequestExists(long id) {
        return itemRequestRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Request with id " + id + " not found"));
    }
}
