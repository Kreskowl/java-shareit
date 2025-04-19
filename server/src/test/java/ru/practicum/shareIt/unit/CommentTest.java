package ru.practicum.shareIt.unit;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.model.Comment;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class CommentTest {

    @Test
    void shouldCreateCommentAndAccessFields() {
        LocalDateTime now = LocalDateTime.now();
        Comment comment = new Comment();
        comment.setId(1L);
        comment.setText("Nice");
        comment.setItemId(2L);
        comment.setAuthorId(1L);
        comment.setCreated(now);

        assertThat(comment.getId()).isEqualTo(1L);
        assertThat(comment.getText()).isEqualTo("Nice");
        assertThat(comment.getItemId()).isEqualTo(2L);
        assertThat(comment.getAuthorId()).isEqualTo(1L);
        assertThat(comment.getCreated()).isEqualTo(now);
    }
}
