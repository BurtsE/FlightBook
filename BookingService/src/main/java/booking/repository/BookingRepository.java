package booking.repository;

import booking.dao.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUserId(Long userId);

    @Query("SELECT CASE WHEN COUNT(b) > 0 THEN true ELSE false END " +
            "FROM Booking b " +
            "WHERE b.roomId = :roomId " +
            "AND b.startDate < :endDate " +
            "AND :startDate < b.endDate " +
            "AND b.status = 'CONFIRMED'"
    )
    boolean roomIsBusy(@Param("roomId") Long roomId,
                       @Param("startDate") Date startDate,
                       @Param("endDate") Date endDate);
}
