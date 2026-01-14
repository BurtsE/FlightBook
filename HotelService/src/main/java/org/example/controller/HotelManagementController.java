package org.example.controller;

import org.example.dto.CreateHotelRequest;
import org.example.repository.HotelRepository;
import org.example.service.HotelManagementService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/hotels")
public class HotelManagementController {

    private final HotelManagementService hotelManagementService;


    public HotelManagementController(HotelManagementService hotelManagementService) {
        this.hotelManagementService = hotelManagementService;
    }

    @PostMapping("/")
    public ResponseEntity<?> createHotel(@RequestBody CreateHotelRequest request)  {

        return new ResponseEntity<>(hotelManagementService.createHotel(request), HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<?> getHotels() {
        return new ResponseEntity<>(hotelManagementService.getAllHotels(), HttpStatus.OK);
    }

}
