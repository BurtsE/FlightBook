package booking.dao;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;


@Entity
@Getter
@Setter
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private Long roomId;

    private Date startDate;

    private Date endDate;

    private String Status;

    private Date createdAt;
}
