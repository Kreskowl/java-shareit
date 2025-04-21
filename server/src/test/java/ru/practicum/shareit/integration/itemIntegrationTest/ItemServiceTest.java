package ru.practicum.shareit.integration.itemIntegrationTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import ru.practicum.shareit.integration.IntegrationTestBase;
import ru.practicum.shareit.ShareItServer;
import ru.practicum.shareit.dto.item.CommentCreateDto;
import ru.practicum.shareit.dto.item.CommentDto;
import ru.practicum.shareit.dto.item.ItemCreateDto;
import ru.practicum.shareit.dto.item.ItemResponseDto;
import ru.practicum.shareit.dto.item.ItemUpdateDto;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.ItemService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ContextConfiguration(classes = ShareItServer.class)
public class ItemServiceTest extends IntegrationTestBase {
    @Autowired
    private ItemService service;
    ItemCreateDto newItem;

    @Test
    void shouldSaveToDB() {
        newItem = new ItemCreateDto("screwdriver", "used 2 times",
                getSecondUser().getId(), false, null);
        ItemResponseDto result = service.createItem(newItem);

        assertThat(itemRepository.findAll()).hasSize(2);
        assertThat(result.getName()).isEqualTo("screwdriver");
        assertThat(result.getDescription()).isEqualTo("used 2 times");
        assertThat(result.getOwnerId()).isEqualTo(getSecondUser().getId());
        assertThat(result.getAvailable()).isFalse();
    }

    @Test
    void shouldFindByIdWithBookInfo() {
        ItemResponseDto result = service.findById(getItem().getId(), getUser().getId());

        assertThat(result.getName()).isEqualTo("Hammer");
        assertThat(result.getDescription()).isEqualTo("Just a red hammer");
        assertThat(result.getOwnerId()).isEqualTo(getUser().getId());
        assertThat(result.getAvailable()).isTrue();
        assertThat(result.getLastBooking()).isNotNull();
        assertThat(result.getNextBooking()).isNull();
    }

    @Test
    void shouldFindByIdWithoutBookInfo() {
        ItemResponseDto result = service.findById(getItem().getId(), getSecondUser().getId());

        assertThat(result.getName()).isEqualTo("Hammer");
        assertThat(result.getDescription()).isEqualTo("Just a red hammer");
        assertThat(result.getOwnerId()).isEqualTo(getUser().getId());
        assertThat(result.getAvailable()).isTrue();
        assertThat(result.getLastBooking()).isNull();
        assertThat(result.getNextBooking()).isNull();
    }

    @Test
    void shouldDeleteFromDB() {
        newItem = new ItemCreateDto("screwdriver", "used 2 times",
                getSecondUser().getId(), false, null);
        ItemResponseDto result = service.createItem(newItem);
        service.deleteById(getItem().getId());

        assertThat(itemRepository.findAll()).hasSize(1);
        assertThat(result.getName()).isEqualTo("screwdriver");
        assertThat(result.getDescription()).isEqualTo("used 2 times");
        assertThat(result.getOwnerId()).isEqualTo(getSecondUser().getId());
        assertThat(result.getAvailable()).isFalse();
    }

    @Test
    void shouldGetAllItemsByOwnerId() {
        newItem = new ItemCreateDto("screwdriver", "used 2 times",
                getUser().getId(), false, null);
        service.createItem(newItem);
        List<ItemResponseDto> result = service.findAllByOwnerId(1);
        System.out.println("Returned items: " + result.size());
        System.out.println("Owner id: " + getUser().getId());
        assertThat(itemRepository.findAll()).hasSize(2);

        assertThat(result)
                .extracting(ItemResponseDto::getName)
                .containsExactlyInAnyOrder("Hammer", "screwdriver");
    }

    @Test
    void shouldAddCommentToItemInDB() {
        CommentCreateDto comment = new CommentCreateDto("Pretty good");
        CommentDto result = service.addComment(getItem().getId(), getSecondUser().getId(), comment);

        assertThat(result.getItemId()).isEqualTo(getItem().getId());
        assertThat(result.getText()).isEqualTo("Pretty good");
        assertThat(result.getAuthorName()).isEqualTo("integrior");
        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    void shouldUpdateItem() {
        ItemUpdateDto update = new ItemUpdateDto("Updated", "Updated desc", true, getUser().getId());
        ItemResponseDto result = service.update(getUser().getId(), getItem().getId(), update);

        assertThat(result.getName()).isEqualTo("Updated");
        assertThat(result.getDescription()).isEqualTo("Updated desc");
        assertThat(result.getAvailable()).isTrue();
    }

    @Test
    void shouldThrowIfUpdateByNotOwner() {
        ItemUpdateDto update = new ItemUpdateDto("Updated", "Updated desc", true, getSecondUser().getId());

        NotFoundException notFound = assertThrows(
                NotFoundException.class,
                () -> service.update(getSecondUser().getId(), getItem().getId(), update)
        );

        assertThat(notFound.getMessage()).contains("not the owner");
    }

    @Test
    void shouldThrowIfUserDidNotBookItem() {
        CommentCreateDto comment = new CommentCreateDto("Sneaky comment");

        ValidationException ex = assertThrows(
                ValidationException.class,
                () -> service.addComment(getItem().getId(), getUser().getId(), comment)
        );

        assertThat(ex.getMessage()).contains("has not completed a booking");
    }

    @Test
    void shouldSearchAvailableItemsByName() {
        List<ItemResponseDto> results = service.search("Hammer");

        assertThat(results).hasSize(1);
        assertThat(results.getFirst().getName()).isEqualTo("Hammer");
    }

    @Test
    void shouldFindByPartialMatch() {
        List<ItemResponseDto> result = service.search("amm");
        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getName()).containsIgnoringCase("amm");
    }

    @Test
    void shouldSearchAvailableItemsByDescription() {
        List<ItemResponseDto> results = service.search("red");

        assertThat(results).hasSize(1);
        assertThat(results.getFirst().getName()).isEqualTo("Hammer");
    }

    @Test
    void shouldReturnEmptyListWhenSearchTextIsEmpty() {
        List<ItemResponseDto> result = service.search("");
        assertThat(result).isEmpty();
    }

    @Test
    void shouldFindByCaseInsensitiveMatch() {
        List<ItemResponseDto> result = service.search("hAmMeR");
        assertThat(result).hasSize(1);
    }

    @Test
    void shouldReturnEmptyListWhenSearchTextIsNull() {
        List<ItemResponseDto> result = service.search(null);
        assertThat(result).isEmpty();
    }

    @Test
    void shouldReturnOnlyAvailableItems() {
        ItemUpdateDto hammer = new ItemUpdateDto("Hammer", "Just a hammer", false, getUser().getId());
        service.update(getUser().getId(), getItem().getId(), hammer);
        List<ItemResponseDto> result = service.search("Hammer");

        assertThat(result).isEmpty();
    }

}
