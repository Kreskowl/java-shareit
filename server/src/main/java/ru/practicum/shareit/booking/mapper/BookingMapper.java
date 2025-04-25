package ru.practicum.shareit.booking.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.dto.booking.BookingCreateDto;
import ru.practicum.shareit.dto.booking.BookingDto;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface BookingMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(source = "userId", target = "renterId")
    @Mapping(source = "dto.start", target = "startTime")
    @Mapping(source = "dto.end", target = "endTime")
    @Mapping(source = "dto.itemId", target = "itemId")
    @Mapping(target = "status", ignore = true)
    Booking createDtoToBooking(BookingCreateDto dto, Long userId);

    @Mapping(source = "itemId", target = "item.id")
    @Mapping(source = "renterId", target = "booker.id")
    @Mapping(source = "startTime", target = "start")
    @Mapping(source = "endTime", target = "end")
    BookingDto bookingToDto(Booking booking);
}
