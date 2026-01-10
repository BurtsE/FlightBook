package org.example.service;

import jakarta.persistence.EntityNotFoundException;
import org.example.dao.Hotel;
import org.example.dao.Room;
import org.example.dto.CreateRoomRequest;
import org.example.mapper.RoomMapper;
import org.example.repository.HotelRepository;
import org.example.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HotelManagementService {
    public final HotelRepository hotelRepository;
    public final RoomRepository roomRepository;

    public HotelManagementService(HotelRepository hotelRepository, RoomRepository roomRepository) {
        this.hotelRepository = hotelRepository;
        this.roomRepository = roomRepository;
    }

    public Hotel createHotel(Hotel hotel) {
        return hotelRepository.save(hotel);
    }

    public Room createRoom(CreateRoomRequest dto) {
        hotelRepository.findById(dto.hotelId).orElseThrow(() -> new EntityNotFoundException("Hotel not found: " + dto.hotelId));
        Room room = RoomMapper.INSTANCE.toEntity(dto);
        return roomRepository.save(room);
    }

    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    public Boolean isRoomAvailable(Long roomId)  {
        return roomRepository.existsByIdAndAvailableTrue(roomId);
    }

    public List<Room> getAvailableRooms() {
        return roomRepository.findByAvailableTrueOrderByTimesBookedAsc();
    }

    public void setRoomAvailable(Long id, boolean available) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Room not found: " + id));
        room.setAvailable(available);
        roomRepository.save(room);
    }
}
