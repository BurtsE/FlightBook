package org.example.repository;

import org.example.dao.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {
    boolean existsByIdAndAvailableTrue(Long id);

    List<Room> findByAvailableTrueOrderByTimesBookedAsc();
}
