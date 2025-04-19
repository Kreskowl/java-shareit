package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.dto.ShareConstants;
import ru.practicum.shareit.dto.request.ItemRequestCreateDto;
import ru.practicum.shareit.dto.request.ItemRequestDto;

import java.util.List;

@RestController
@RequestMapping("/requests")
@RequiredArgsConstructor
public class ItemRequestController {
    private final ItemRequestService service;

    @PostMapping
    public ItemRequestDto createRequest(@Valid @RequestBody ItemRequestCreateDto dto,
                                        @RequestHeader(value = ShareConstants.USER_ID_HEADER) Long userId) {
        return service.createItemRequest(dto, userId);
    }

    @GetMapping
    public List<ItemRequestDto> findAllUserRequests(@RequestHeader(value = ShareConstants.USER_ID_HEADER) Long userId) {
        return service.findAllUserRequests(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> findAllOtherUsersRequests(@RequestHeader(value = ShareConstants.USER_ID_HEADER) Long userId) {
        return service.findAllOtherRequests(userId);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto findRequestById(@PathVariable long requestId) {
        return service.findById(requestId);
    }
}
