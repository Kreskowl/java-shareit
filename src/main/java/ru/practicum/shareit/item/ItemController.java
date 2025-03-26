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
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.model.Item;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping("/items")
@Slf4j
public class ItemController {
    private final ItemService service;

    @PostMapping
    public Item createItem(@RequestHeader(value = "X-Sharer-User-Id", required = true) Long ownerId,
                           @RequestBody @Valid Item item) {
        return service.createItem(item, ownerId);
    }

    @GetMapping("/{itemId}")
    public Item getItem(@PathVariable long itemId) {
        return service.findById(itemId);
    }

    @GetMapping
    public List<Item> getAllUserItems(@RequestHeader(value = "X-Sharer-User-Id", required = true) Long ownerId) {
        return service.findAllByOwnerId(ownerId);
    }

    @GetMapping("/search")
    public List<Item> searchItem(@RequestParam(value = "text", required = false) String text) {
        if (text == null || text.trim().isEmpty()) {
            return Collections.emptyList();
        }
        return service.search(text);
    }

    @PatchMapping("/{itemId}")
    public Item update(@RequestHeader(value = "X-Sharer-User-Id", required = true) Long ownerId,
                       @PathVariable long itemId, @RequestBody @Valid ItemUpdateDto updatedDto) {
        return service.update(ownerId, itemId, updatedDto);
    }
}
