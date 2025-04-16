package ru.practicum.shareit.item.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.model.Comment;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CommentMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "itemId", source = "itemId")
    @Mapping(target = "authorId", source = "authorId")
    Comment toEntity(CommentCreateDto dto, Long itemId, Long authorId, LocalDateTime created);

    @Mapping(source = "authorName", target = "authorName")
    CommentDto toResponse(Comment comment, String authorName);
}
