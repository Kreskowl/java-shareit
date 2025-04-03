package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final ItemMapper mapper;

    public Item createItem(Item item) {
        userRepository.findById(item.getOwnerId());
        return itemRepository.save(item);
    }

    public List<Item> findAllByOwnerId(long ownerId) {
        return itemRepository.findAllByOwnerId(ownerId);
    }

    public Item findById(long id) {
        return itemRepository.findById(id);
    }

    public void deleteById(long id) {
        itemRepository.deleteById(id);
    }

    public Item update(long ownerId, long itemId, ItemUpdateDto updatedDto) {
        Item item = itemRepository.findById(itemId);
        if (!item.getOwnerId().equals(ownerId)) {
            throw new NotFoundException("User with id " + ownerId + " is not the owner of this item");
        }

        mapper.updateItemFromDto(updatedDto, item);
        return itemRepository.update(item);
    }

    public List<Item> search(String text) {
        return itemRepository.search(text);
    }

}
