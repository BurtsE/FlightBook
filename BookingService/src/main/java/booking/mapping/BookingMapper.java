package booking.mapping;

import booking.dao.Booking;
import booking.dto.CreateBookingRequestDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.Date;

@Mapper
public interface BookingMapper {

    BookingMapper INSTANCE = Mappers.getMapper(BookingMapper.class);

    @Mapping(target = "userId", source = "userId")
    @Mapping(target = "roomId", source = "dto.roomId")
    @Mapping(target = "status", constant = "PENDING")
    @Mapping(target = "createdAt", expression = "java(new java.util.Date())")
    @Mapping(target = "id", ignore = true)
    Booking toEntity(CreateBookingRequestDTO dto, Long userId);
}
