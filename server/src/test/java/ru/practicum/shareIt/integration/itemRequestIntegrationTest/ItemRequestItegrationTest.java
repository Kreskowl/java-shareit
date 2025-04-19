package ru.practicum.shareIt.integration.itemRequestIntegrationTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.practicum.shareIt.integration.IntegrationTestBase;
import ru.practicum.shareit.dto.request.ItemRequestCreateDto;
import ru.practicum.shareit.dto.request.ItemRequestDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequestService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ItemRequestItegrationTest extends IntegrationTestBase {
    @Autowired
    private ItemRequestService service;

    @Test
    void shouldSaveInDB() {
        ItemRequestCreateDto request = new ItemRequestCreateDto("looking for drill");
        ItemRequestDto result = service.createItemRequest(request, getUser().getId());
        Long savedId = result.getId();
        ItemRequestDto checkId = service.findById(savedId);

        assertThat(result.getId()).isEqualTo(savedId);
        assertThat(requestRepository.findAll()).hasSize(2);
        assertThat(result.getRequesterId()).isEqualTo(getUser().getId());
        assertThat(result.getDescription()).contains("drill");

    }

    @Test
    void shouldFindById() {
        ItemRequestDto result = service.findById(getRequest().getId());

        assertThat(result.getId()).isEqualTo(getRequest().getId());
        assertThat(result.getRequesterId()).isEqualTo(getUser().getId());
        assertThat(result.getDescription()).contains("mask");
    }

    @Test
    void shouldFindAllRequestsExceptUser() {
        List<ItemRequestDto> result = service.findAllOtherRequests(getSecondUser().getId());

        assertThat(result).hasSize(1);
        ItemRequestDto dto = result.getFirst();
        assertThat(dto.getDescription()).isEqualTo("looking for a gasmask");
        assertThat(dto.getRequesterId()).isEqualTo(getUser().getId());

    }

    @Test
    void shouldFindAllUsersRequests() {
        Item itemWithRequest = itemRepository.save(Item.builder()
                .name("Gasmask")
                .description("Old army gasmask")
                .available(true)
                .ownerId(getSecondUser().getId())
                .requestId(getRequest().getId())
                .build());

        List<ItemRequestDto> result = service.findAllUserRequests(getUser().getId());
        ItemRequestDto dto = result.getFirst();

        assertThat(result).hasSize(1);
        assertThat(dto.getDescription()).isEqualTo("looking for a gasmask");
        assertThat(dto.getRequesterId()).isEqualTo(getUser().getId());
        assertThat(dto.getItems()).hasSize(1);
        assertThat(dto.getItems().getFirst().getId()).isEqualTo(itemWithRequest.getId());
        assertThat(dto.getItems().getFirst().getName()).isEqualTo("Gasmask");
    }

    @Test
    void shouldReturnEmptyListWhenNoRequestsFound() {
        List<ItemRequestDto> result = service.findAllOtherRequests(getUser().getId());
        assertThat(result).isEmpty();
    }
}
