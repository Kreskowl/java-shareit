package ru.practicum.shareIt.unit;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.dto.item.CommentDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.model.Comment;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class CommentMapperTest {

    private final CommentMapper mapper = Mappers.getMapper(CommentMapper.class);

    @Test
    void shouldMapToDto() {
        Comment comment = new Comment();
        comment.setId(1L);
        comment.setText("Nice");
        comment.setAuthorId(1L);
        comment.setCreated(LocalDateTime.now());
        comment.setItemId(2L);

        CommentDto dto = mapper.toResponse(comment, "Bob");

        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getText()).isEqualTo("Nice");
        assertThat(dto.getAuthorName()).isEqualTo("Bob");
        assertThat(dto.getItemId()).isEqualTo(2L);
    }
}
