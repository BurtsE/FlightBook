package org.example.dao;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class Hotel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String address;

    @OneToMany(targetEntity = Room.class, cascade = CascadeType.ALL)
    private List<Room> rooms;
}

