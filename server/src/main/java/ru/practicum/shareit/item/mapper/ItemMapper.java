package ru.practicum.shareit.item.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.practicum.shareit.dto.item.ItemCreateDto;
import ru.practicum.shareit.dto.item.ItemResponseDto;
import ru.practicum.shareit.dto.item.ItemUpdateDto;
import ru.practicum.shareit.item.model.Item;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ItemMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "requestId", source = "requestId")
    Item createDtoToItem(ItemCreateDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "ownerId", ignore = true)
    @Mapping(target = "requestId", ignore = true)
    void updateItemFromDto(ItemUpdateDto dto, @MappingTarget Item item);

    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "lastBooking", ignore = true)
    @Mapping(target = "nextBooking", ignore = true)
    ItemResponseDto itemToDto(Item item);

    Item fillItemToDto(ItemResponseDto itemResponseDto);
}
