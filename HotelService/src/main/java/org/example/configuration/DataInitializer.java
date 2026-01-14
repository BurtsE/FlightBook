package org.example.configuration;

import jakarta.transaction.Transactional;
import org.example.dao.Hotel;
import org.example.dao.Room;
import org.example.repository.HotelRepository;
import org.example.repository.RoomRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

public class DataInitializer {
    @Bean
    @Transactional
    public CommandLineRunner run(HotelRepository hotelRepository, RoomRepository roomRepository) throws Exception {
        return args -> {
            Hotel hotel = new Hotel();
            hotel.setName("Example Hotel");
            hotel.setAddress("123 Example St");
            hotelRepository.save(hotel);

            for (int i = 1; i <= 10; i++) {
                Room room = new Room();
                room.setHotel(hotel);
                room.setRoomNumber(i);
                room.setAvailable(true);
                roomRepository.save(room);
            }
        };
    }
}
