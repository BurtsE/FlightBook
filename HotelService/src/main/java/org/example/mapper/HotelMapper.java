package org.example.mapper;

import org.example.dao.Hotel;
import org.example.dto.CreateHotelRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
@Mapper(componentModel = "spring")
public interface HotelMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "rooms", ignore = true)
    Hotel toEntity(CreateHotelRequest dto);
}
