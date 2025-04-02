//package ru.practicum.shareit.booking.mapper;
//
//import org.springframework.jdbc.core.RowMapper;
//import org.springframework.stereotype.Component;
//import ru.practicum.shareit.booking.dto.BookingDto;
//import ru.practicum.shareit.booking.model.Booking;
//import ru.practicum.shareit.booking.model.Status;
//import ru.practicum.shareit.item.model.Item;
//import ru.practicum.shareit.user.model.User;
//
//import java.sql.ResultSet;
//import java.sql.SQLException;
//
//@Component
//public class BookingMapper implements RowMapper<Booking> {
//
////    @Override
////    public Booking mapRow(ResultSet rs, int rowNum) throws SQLException {
////        User renter = User.builder()
////                .id(rs.getLong("owner_id"))
////                .name(rs.getString("owner_name"))
////                .email(rs.getString("owner_email"))
////                .build();
////
////        return Booking.builder()
////                .id(rs.getLong("id"))
////                .item(rs.getObject("item", Item.class))
////                .renter(renter)
////                .startTime(rs.getTimestamp("startTime").toLocalDateTime())
////                .endTime(rs.getTimestamp("endTime").toLocalDateTime())
////                .status(rs.getObject("status", Status.class))
////                .build();
////    }
////
////    public static BookingDto bookingToDto(Booking booking) {
////        return new BookingDto(
////                booking.getId(),
////                booking.getItem(),
////                booking.getRenter(),
////                booking.getStartTime(),
////                booking.getEndTime(),
////                booking.getStatus()
////        );
////    }
//}
