package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;

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
