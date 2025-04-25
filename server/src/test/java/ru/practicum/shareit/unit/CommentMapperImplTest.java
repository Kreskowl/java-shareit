package ru.practicum.shareit.unit;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.dto.item.CommentCreateDto;
import ru.practicum.shareit.item.mapper.CommentMapperImpl;
import ru.practicum.shareit.item.model.Comment;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class CommentMapperImplTest {

    private final CommentMapperImpl mapper = new CommentMapperImpl();

    @Test
    void toEntity_shouldReturnNull_whenAllParamsAreNull() {
        Comment result = mapper.toEntity(null, null, null, null);
        assertNull(result);
    }

    @Test
    void toEntity_shouldMapFieldsCorrectly_whenDtoIsNotNull() {
        CommentCreateDto dto = new CommentCreateDto();
        dto.setText("text");

        Long itemId = 1L;
        Long authorId = 2L;
        LocalDateTime created = LocalDateTime.now();

        Comment result = mapper.toEntity(dto, itemId, authorId, created);

        assertNotNull(result);
        assertEquals("text", result.getText());
        assertEquals(itemId, result.getItemId());
        assertEquals(authorId, result.getAuthorId());
        assertEquals(created, result.getCreated());
    }

    @Test
    void toEntity_shouldWork_whenDtoIsNullButOtherParamsPresent() {
        Long itemId = 1L;
        Long authorId = 2L;
        LocalDateTime created = LocalDateTime.now();

        Comment result = mapper.toEntity(null, itemId, authorId, created);

        assertNotNull(result);
        assertNull(result.getText()); // dto is null
        assertEquals(itemId, result.getItemId());
        assertEquals(authorId, result.getAuthorId());
        assertEquals(created, result.getCreated());
    }
}
