//package ru.practicum.shareit.request.mapper;
//
//import org.springframework.jdbc.core.RowMapper;
//import org.springframework.stereotype.Component;
//import ru.practicum.shareit.request.dto.ItemRequestDto;
//import ru.practicum.shareit.request.model.ItemRequest;
//import ru.practicum.shareit.user.model.User;
//
//import java.sql.ResultSet;
//import java.sql.SQLException;
//
//@Component
//public class ItemRequestMapper implements RowMapper<ItemRequest> {
//
//    @Override
//    public ItemRequest mapRow(ResultSet rs, int rowNum) throws SQLException {
//        User requester = User.builder()
//                .id(rs.getLong("owner_id"))
//                .name(rs.getString("owner_name"))
//                .email(rs.getString("owner_email"))
//                .build();
//
//        return ItemRequest.builder()
//                .id(rs.getLong("id"))
//                .description(rs.getString("description"))
//                .requester(requester)
//                .created(rs.getTimestamp("created").toLocalDateTime())
//                .build();
//    }
//
//    public static ItemRequestDto itemRequestToDto(ItemRequest itemRequest) {
//        return new ItemRequestDto(
//                itemRequest.getId(),
//                itemRequest.getDescription(),
//                itemRequest.getRequester(),
//                itemRequest.getCreated()
//        );
//    }
//}
