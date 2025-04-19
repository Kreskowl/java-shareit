package ru.practicum.shareit.request;

import ru.practicum.shareit.dto.request.ItemRequestCreateDto;
import ru.practicum.shareit.dto.request.ItemRequestDto;

import java.util.List;


public interface ItemRequestService {

    ItemRequestDto createItemRequest(ItemRequestCreateDto dto, Long userId);

    List<ItemRequestDto> findAllOtherRequests(long userId);

    List<ItemRequestDto> findAllUserRequests(long userId);

    ItemRequestDto findById(long id);
}
