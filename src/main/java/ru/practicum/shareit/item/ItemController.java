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
import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;

import java.util.List;

@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping("/items")
@Slf4j
public class ItemController {
    private final ItemService service;

    @PostMapping
    public ItemResponseDto createItem(@RequestHeader(value = ShareConstants.USER_ID_HEADER) Long ownerId,
                                      @RequestBody @Valid ItemCreateDto dto) {
        dto.setOwnerId(ownerId);
        return service.createItem(dto);
    }

    @GetMapping("/{itemId}")
    public ItemResponseDto getItem(@RequestHeader(value = ShareConstants.USER_ID_HEADER) long userId,
                                   @PathVariable long itemId) {
        return service.findById(itemId, userId);
    }

    @GetMapping
    public List<ItemResponseDto> getAllUserItems(@RequestHeader(value = ShareConstants.USER_ID_HEADER) Long ownerId) {
        return service.findAllByOwnerId(ownerId);
    }

    @GetMapping("/search")
    public List<ItemResponseDto> searchItem(@RequestParam(value = "text", required = false) String text) {
        return service.search(text);
    }

    @PatchMapping("/{itemId}")
    public ItemResponseDto update(@RequestHeader(value = ShareConstants.USER_ID_HEADER) Long ownerId,
                                  @PathVariable long itemId, @RequestBody @Valid ItemUpdateDto updatedDto) {
        return service.update(ownerId, itemId, updatedDto);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@RequestHeader(value = ShareConstants.USER_ID_HEADER) Long authorId,
                                 @PathVariable Long itemId,
                                 @RequestBody @Valid CommentCreateDto dto) {
        return service.addComment(itemId, authorId, dto);
    }
}
