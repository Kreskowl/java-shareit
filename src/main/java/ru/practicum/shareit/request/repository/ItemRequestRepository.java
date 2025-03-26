package ru.practicum.shareit.request.repository;


import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

public interface ItemRequestRepository {
    ItemRequest save(ItemRequest itemRequest);

    ItemRequest findById(long id);

    List<ItemRequest> findAll();

    void deleteById(long id);

    void update(ItemRequest itemRequest);
}
