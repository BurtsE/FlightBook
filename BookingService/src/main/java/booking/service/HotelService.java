package booking.service;

import booking.dto.AvailabilityResponseDTO;
import booking.dto.RoomIsBookedDTO;
import booking.dto.RoomResponseDTO;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Service
public class HotelService {
    private final RestClient hotelClient;
    private final EurekaClient eurekaClient;

    public HotelService(@Value("${app.hotel-service.name}") String hotelServiceName, EurekaClient eurekaClient) {
        this.eurekaClient = eurekaClient;

        InstanceInfo service = eurekaClient
                .getApplication(hotelServiceName)
                .getInstances()
                .getFirst();
        String baseUrl = "http://" + service.getHostName() + ":" + service.getPort();
        this.hotelClient = RestClient.create(baseUrl);
    }


    @Retryable(
            value = { RestClientException.class, IOException.class },
            maxAttempts = 3,
            backoff = @Backoff(delay = 1000, multiplier = 2) // 1s, 2s, 4s
    )
    public boolean checkAvailability(Long roomId) {
        AvailabilityResponseDTO response =  hotelClient
                .post()
                .uri("/rooms/{id}/confirm-availability", roomId)
                .retrieve()
                .body(AvailabilityResponseDTO.class);
        return response.available();
    }

    public void release(Long roomId, boolean booked) {
        System.out.println("Releasing room " + roomId + " booked: " + booked);
        hotelClient
                .post()
                .uri("/rooms/{id}/release", roomId)
                .body(new RoomIsBookedDTO(booked))
                .retrieve()
                .body(Void.class);
    }

        public List<RoomResponseDTO> getAvailableRooms() {
            return hotelClient
                    .get()
                    .uri("/rooms/recommend")
                    .retrieve()
                    .body(new ParameterizedTypeReference<List<RoomResponseDTO>>() {});
    }

    @Recover
    public boolean handleException(RestClientException e, Long roomId) {
        System.err.println("Error occurred while checking availability for room " + roomId + ": " + e.getMessage());
        return false;
    }
}
