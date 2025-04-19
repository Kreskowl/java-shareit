package ru.practicum.shareit.item;

import ru.practicum.shareit.dto.item.CommentCreateDto;
import ru.practicum.shareit.dto.item.CommentDto;
import ru.practicum.shareit.dto.item.ItemCreateDto;
import ru.practicum.shareit.dto.item.ItemResponseDto;
import ru.practicum.shareit.dto.item.ItemUpdateDto;

import java.util.List;


public interface ItemService {

    ItemResponseDto createItem(ItemCreateDto dto);

    List<ItemResponseDto> findAllByOwnerId(long ownerId);

    ItemResponseDto findById(long id, long userId);

    void deleteById(long id);

    ItemResponseDto update(long ownerId, long itemId, ItemUpdateDto updatedDto);

    List<ItemResponseDto> search(String text);

    CommentDto addComment(Long itemId, Long authorId, CommentCreateDto dto);

}
