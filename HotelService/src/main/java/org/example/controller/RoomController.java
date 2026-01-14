package org.example.controller;

import org.example.dao.Room;
import org.example.dto.AvailabilityResponseDTO;
import org.example.dto.CreateRoomRequest;
import org.example.dto.RoomIsBookedDTO;
import org.example.mapper.RoomMapper;
import org.example.service.HotelManagementService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/rooms")
public class RoomController {
    private final HotelManagementService hotelManagementService;

    public RoomController(HotelManagementService hotelManagementService) {
        this.hotelManagementService = hotelManagementService;
    }

    @PostMapping("/")
    public ResponseEntity<?> createRoom(@RequestBody CreateRoomRequest request) {
        return new ResponseEntity<>(hotelManagementService.createRoom(request), HttpStatus.CREATED);
    }

    @GetMapping("/")
    public ResponseEntity<?> getAllRooms() {
        return new ResponseEntity<>(hotelManagementService.getAllRooms(), HttpStatus.OK);
    }

    @GetMapping("/recommend")
    public ResponseEntity<?> getAvailableRooms() {
        return new ResponseEntity<>(hotelManagementService.getAvailableRooms(), HttpStatus.OK);
    }

    @PostMapping("/{id}/confirm-availability")
    public ResponseEntity<?> confirmAvailability(@PathVariable Long id) {
        boolean available = hotelManagementService.isRoomAvailable(id);
        if (available) {
            hotelManagementService.setRoomAvailable(id, false);
        }

        return new ResponseEntity<>(new AvailabilityResponseDTO(available), HttpStatus.OK);
    }

    @PostMapping("/{id}/release")
    public ResponseEntity<?> releaseAvailability(
            @PathVariable Long id,
            @RequestBody RoomIsBookedDTO bookedDTO
    ) {
        if (bookedDTO != null && bookedDTO.booked()) {
            hotelManagementService.incrementRoomTimesBooked(id);
        }
        hotelManagementService.setRoomAvailable(id, true);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
