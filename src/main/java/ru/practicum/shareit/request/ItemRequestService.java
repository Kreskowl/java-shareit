package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;

    public ItemRequest createItemRequest(ItemRequest itemRequest) {
        return itemRequestRepository.save(itemRequest);
    }


    public List<ItemRequest> findAll() {
        return itemRequestRepository.findAll();
    }

    public ItemRequest findById(long id) {
        return itemRequestRepository.findById(id);
    }

    public void deleteById(long id) {
        itemRequestRepository.deleteById(id);
    }

    public void updateRequest(ItemRequest itemRequest) {
        itemRequestRepository.update(itemRequest);
    }
}
