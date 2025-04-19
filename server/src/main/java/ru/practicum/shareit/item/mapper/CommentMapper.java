package ru.practicum.shareit.item.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.practicum.shareit.dto.item.CommentCreateDto;
import ru.practicum.shareit.dto.item.CommentDto;
import ru.practicum.shareit.item.model.Comment;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CommentMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "itemId", source = "itemId")
    @Mapping(target = "authorId", source = "authorId")
    Comment toEntity(CommentCreateDto dto, Long itemId, Long authorId, LocalDateTime created);

    default CommentDto toResponse(Comment comment, String authorName) {
        if (comment == null) {
            return null;
        }

        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .itemId(comment.getItemId())
                .authorName(authorName)
                .created(comment.getCreated())
                .build();
    }
}
