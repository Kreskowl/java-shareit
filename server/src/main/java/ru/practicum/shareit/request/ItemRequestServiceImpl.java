package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.dto.item.ItemBookDto;
import ru.practicum.shareit.dto.request.ItemRequestCreateDto;
import ru.practicum.shareit.dto.request.ItemRequestDto;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository repository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final ItemRequestMapper mapper;
    private static final LocalDateTime SET_CREATE_TIME = LocalDateTime.now();

    @Override
    @Transactional
    public ItemRequestDto createItemRequest(ItemRequestCreateDto itemRequestDto, Long userId) {
        ifUserExists(userId);
        ItemRequest request = mapper.createDtoToRequest(itemRequestDto);
        request.setCreated(SET_CREATE_TIME);
        request.setRequesterId(userId);
        ItemRequest saved = repository.save(request);

        return mapper.requestToDto(saved);
    }

    @Override
    public List<ItemRequestDto> findAllOtherRequests(long userId) {
        ifUserExists(userId);

        return repository.findAllByRequesterIdNotOrderByCreatedDesc(userId).stream()
                .map(mapper::requestToDto)
                .toList();
    }

    @Override
    public List<ItemRequestDto> findAllUserRequests(long userId) {
        ifUserExists(userId);

        return repository.findAllByRequesterIdOrderByCreatedDesc(userId).stream()
                .map(request -> {
                    ItemRequestDto dto = mapper.requestToDto(request);

                    List<Item> items = itemRepository.findAllByRequestId(request.getId());

                    List<ItemBookDto> itemDtos = items.stream()
                            .map(item -> ItemBookDto.builder()
                                    .id(item.getId())
                                    .name(item.getName())
                                    .build())
                            .toList();

                    dto.setItems(itemDtos);
                    return dto;
                })
                .toList();
    }

    @Override
    public ItemRequestDto findById(long id) {
        ItemRequest request = ifRequestExists(id);
        ItemRequestDto dto = mapper.requestToDto(request);

        List<Item> items = itemRepository.findAllByRequestId(id);
        List<ItemBookDto> itemDtos = items.stream()
                .map(item -> ItemBookDto.builder()
                        .id(item.getId())
                        .name(item.getName())
                        .build())
                .toList();

        dto.setItems(itemDtos);
        return dto;
    }

    private User ifUserExists(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id " + userId + " not found"));
    }

    private ItemRequest ifRequestExists(long requestId) {
        return repository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Request with id " + requestId + " not found"));
    }
}
