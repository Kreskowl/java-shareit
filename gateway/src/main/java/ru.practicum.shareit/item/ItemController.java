package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
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
import ru.practicum.shareit.dto.ShareConstants;
import ru.practicum.shareit.dto.item.CommentCreateDto;
import ru.practicum.shareit.dto.item.ItemCreateDto;
import ru.practicum.shareit.dto.item.ItemUpdateDto;

@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping("/items")
@Slf4j
public class ItemController {
    private final ItemClient client;

    @PostMapping
    public ResponseEntity<Object> createItem(@RequestHeader(value = ShareConstants.USER_ID_HEADER) Long ownerId,
                                             @RequestBody @Valid ItemCreateDto dto) {
        dto.setOwnerId(ownerId);
        return client.createItem(ownerId, dto);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItem(@RequestHeader(value = ShareConstants.USER_ID_HEADER) long userId,
                                          @PathVariable long itemId) {
        return client.findItemById(itemId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllUserItems(@RequestHeader(value = ShareConstants.USER_ID_HEADER) Long ownerId) {
        return client.findAllByOwnerId(ownerId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItem(@RequestParam(value = "text", required = false) String text) {
        return client.search(text);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> update(@RequestHeader(value = ShareConstants.USER_ID_HEADER) Long ownerId,
                                         @PathVariable long itemId, @RequestBody @Valid ItemUpdateDto updatedDto) {
        return client.update(ownerId, itemId, updatedDto);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@RequestHeader(value = ShareConstants.USER_ID_HEADER) Long authorId,
                                             @PathVariable Long itemId,
                                             @RequestBody @Valid CommentCreateDto dto) {
        return client.addComment(itemId, authorId, dto);
    }

}
