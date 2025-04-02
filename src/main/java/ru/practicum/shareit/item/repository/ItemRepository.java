package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository {
    Item save(Item item);

    Item findById(Long id);

    List<Item> findAllByOwnerId(long ownerId);

    List<Item> search(String text);

    void deleteById(Long id);

    Item update(Item item);
}
