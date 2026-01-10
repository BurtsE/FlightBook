package org.example.mapper;

import org.example.dao.Hotel;
import org.example.dao.Room;
import org.example.dto.CreateRoomRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface RoomMapper {
    RoomMapper INSTANCE = Mappers.getMapper(RoomMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "hotelId", target = "hotel")
    Room toEntity(CreateRoomRequest request);

    default Hotel mapHotelIdToHotel(Long hotelId) {
        if (hotelId == null) {
            return null;
        }
        Hotel hotel = new Hotel();
        hotel.setId(hotelId);
        return hotel;
    }
}
