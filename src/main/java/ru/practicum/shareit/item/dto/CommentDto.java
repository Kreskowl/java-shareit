package ru.practicum.shareit.item.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.shareit.item.ShareConstants;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor
public class CommentDto {
    private Long id;
    private String text;
    private Long itemId;
    private String authorName;
    @DateTimeFormat(pattern = ShareConstants.DATE_TIME_PATTERN)
    private LocalDateTime created;
}
