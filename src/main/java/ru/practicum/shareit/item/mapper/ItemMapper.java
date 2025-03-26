package ru.practicum.shareit.item.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class ItemMapper implements RowMapper<Item> {

    @Override
    public Item mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Item.builder()
                .id(rs.getLong("id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .ownerId(rs.getLong("ownerId"))
                .available(rs.getBoolean("available"))
                .request(rs.getObject("request", ItemRequest.class))
                .build();
    }

    public static ItemCreateDto itemToDto(Item item) {
        return new ItemCreateDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getOwnerId(),
                item.getAvailable()
        );
    }
}
