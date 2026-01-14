package org.example.service;

import jakarta.persistence.EntityNotFoundException;
import org.example.dao.Hotel;
import org.example.dao.Room;
import org.example.dto.CreateHotelRequest;
import org.example.dto.CreateRoomRequest;
import org.example.mapper.HotelMapper;
import org.example.mapper.RoomMapper;
import org.example.repository.HotelRepository;
import org.example.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HotelManagementService {
    public final HotelRepository hotelRepository;
    public final RoomRepository roomRepository;
    public final HotelMapper hotelMapper;
    private final RoomMapper roomMapper;

    public HotelManagementService(
            HotelRepository hotelRepository,
            RoomRepository roomRepository,
            HotelMapper hotelMapper,
            RoomMapper roomMapper)
    {
        this.hotelRepository = hotelRepository;
        this.roomRepository = roomRepository;
        this.hotelMapper = hotelMapper;
        this.roomMapper = roomMapper;
    }

    public Hotel createHotel(CreateHotelRequest dto) {
        Hotel hotel = hotelMapper.toEntity(dto);
        return hotelRepository.save(hotel);
    }
    public List<Hotel> getAllHotels() {
        return hotelRepository.findAll();
    }

    public Room createRoom(CreateRoomRequest dto) {
        hotelRepository.findById(dto.hotelId).orElseThrow(() -> new EntityNotFoundException("Hotel not found: " + dto.hotelId));
        Room room = roomMapper.toEntity(dto);
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

    public void incrementRoomTimesBooked(Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new EntityNotFoundException("Room not found: " + roomId));
        room.setTimesBooked(room.getTimesBooked() + 1);
        roomRepository.save(room);
    }
}
