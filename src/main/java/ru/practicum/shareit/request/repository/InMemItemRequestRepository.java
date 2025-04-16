package ru.practicum.shareit.request.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
@RequiredArgsConstructor
public class InMemItemRequestRepository implements ItemRequestRepository {
    private final Map<Long, ItemRequest> storage;
    private final AtomicLong generator = new AtomicLong(0);

    @Override
    public ItemRequest save(ItemRequest itemRequest) {
        if (itemRequest.getId() == null) {
            itemRequest.setId(generator.incrementAndGet());
        }
        storage.put(itemRequest.getId(), itemRequest);
        return itemRequest;
    }

    @Override
    public ItemRequest findById(long id) {
        return ifExists(id);
    }

    @Override
    public List<ItemRequest> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public void deleteById(long id) {
        ifExists(id);
        storage.remove(id);
    }

    @Override
    public void update(ItemRequest itemRequest) {
        ItemRequest exist = findById(itemRequest.getId());
        exist.setDescription(itemRequest.getDescription());
        exist.setRequesterId(itemRequest.getRequesterId());
        storage.put(exist.getId(), exist);
    }

    private ItemRequest ifExists(long id) {
        return Optional.ofNullable(storage.get(id))
                .orElseThrow(() -> new NotFoundException("Requset with id " + id + " not found"));
    }
}
