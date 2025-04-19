package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.dto.ShareConstants;
import ru.practicum.shareit.dto.request.ItemRequestCreateDto;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {
    private final RequestClient client;

    @PostMapping
    public ResponseEntity<Object> createRequest(@Valid @RequestBody ItemRequestCreateDto dto,
                                                @RequestHeader(ShareConstants.USER_ID_HEADER) Long userId) {
        return client.createItemRequest(dto, userId);
    }

    @GetMapping
    public ResponseEntity<Object> findAllUserRequests(@RequestHeader(ShareConstants.USER_ID_HEADER) Long userId) {
        return client.findAllUserRequests(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> findAllOtherUsersRequests(@RequestHeader(ShareConstants.USER_ID_HEADER) Long userId) {
        return client.findAllOtherRequests(userId);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> findRequestById(@PathVariable long requestId) {
        return client.findById(requestId);
    }
}
