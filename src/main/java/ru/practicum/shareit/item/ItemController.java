package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping("/items")
@Slf4j
public class ItemController {
    private final ItemService service;
    private final ItemMapper itemMapper;

    @PostMapping
    public ItemResponseDto createItem(@RequestHeader(value = UserIdHeader.USER_ID_HEADER) Long ownerId,
                                      @RequestBody @Valid ItemCreateDto dto) {
        Item item = itemMapper.createDtoToItem(dto);
        item.setOwnerId(ownerId);
        Item savedItem = service.createItem(item);

        return itemMapper.itemToDto(savedItem);
    }

    @GetMapping("/{itemId}")
    public ItemResponseDto getItem(@PathVariable long itemId) {
        return itemMapper.itemToDto(service.findById(itemId));
    }

    @GetMapping
    public List<ItemResponseDto> getAllUserItems(@RequestHeader(value = UserIdHeader.USER_ID_HEADER) Long ownerId) {
        return service.findAllByOwnerId(ownerId)
                .stream()
                .map(itemMapper::itemToDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/search")
    public List<ItemResponseDto> searchItem(@RequestParam(value = "text", required = false) String text) {
        if (text == null || text.trim().isEmpty()) {
            return Collections.emptyList();
        }
        return service.search(text).stream()
                .map(itemMapper::itemToDto)
                .collect(Collectors.toList());
    }

    @PatchMapping("/{itemId}")
    public ItemResponseDto update(@RequestHeader(value = UserIdHeader.USER_ID_HEADER) Long ownerId,
                       @PathVariable long itemId, @RequestBody @Valid ItemUpdateDto updatedDto) {
        Item updated = service.update(ownerId, itemId, updatedDto);
        return itemMapper.itemToDto(updated);
    }
}
