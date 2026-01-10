package org.example.dao;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_id", referencedColumnName = "id")
    private Hotel hotel;

    private Integer roomNumber;

    private boolean available;

    private Long timesBooked;
}
