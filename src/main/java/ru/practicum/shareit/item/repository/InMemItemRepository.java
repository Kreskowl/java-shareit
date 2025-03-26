package ru.practicum.shareit.item.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
public class InMemItemRepository implements ItemRepository {
    private final Map<Long, Item> storage = new HashMap<>();
    private final AtomicLong generator = new AtomicLong(0);

    @Override
    public Item save(Item item, long ownerId) {
        if (item.getId() == null) {
            item.setId(generator.incrementAndGet());
        }

        item.setOwnerId(ownerId);
        storage.put(item.getId(), item);
        return item;
    }

    @Override
    public Item findById(Long id) {
        return ifExists(id);
    }

    @Override
    public List<Item> findAllByOwnerId(long ownerId) {
        return storage.values().stream()
                .filter(item -> item.getOwnerId() != null && item.getOwnerId().equals(ownerId))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        ifExists(id);
        storage.remove(id);
    }

    @Override
    public Item update(Item updated) {
        Item exist = ifExists(updated.getId());

        if (updated.getName() != null) {
            exist.setName(updated.getName());
        }
        if (updated.getDescription() != null) {
            exist.setDescription(updated.getDescription());
        }
        exist.setAvailable(updated.getAvailable());
        storage.put(exist.getId(), exist);

        return exist;
    }

    public List<Item> search(String text) {
        String lowerText = text.toLowerCase();
        return storage.values().stream()
                .filter(Item::getAvailable)
                .filter(item -> item.getName().toLowerCase().contains(lowerText)
                        || item.getDescription().toLowerCase().contains(lowerText))
                .collect(Collectors.toList());
    }

    private Item ifExists(long id) {
        return Optional.ofNullable(storage.get(id))
                .orElseThrow(() -> new NotFoundException("item with id " + id + " not found"));
    }
}
