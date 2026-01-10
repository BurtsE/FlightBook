package org.example.controller;

import org.example.repository.HotelRepository;
import org.example.service.HotelManagementService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/hotels")
public class HotelManagementController {

    private final HotelManagementService hotelManagementService;


    public HotelManagementController(HotelManagementService hotelManagementService) {
        this.hotelManagementService = hotelManagementService;
    }

    @PostMapping("/")
    public String getRooms() {
        return "rooms";
    }

    @GetMapping("/")
    public String getHotels() {
        return "hotels";
    }

}
