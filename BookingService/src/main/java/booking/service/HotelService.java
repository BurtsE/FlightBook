package booking.service;

import booking.dto.AvailabilityResponseDTO;
import booking.dto.RoomResponseDTO;
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

    public HotelService(@Value("${app.hotel-service.base-url}") String hotelServiceBaseUrl) {
        this.hotelClient = RestClient.create(hotelServiceBaseUrl);
    }


    @Retryable(
            value = { RestClientException.class, IOException.class },
            maxAttempts = 3,
            backoff = @Backoff(delay = 1000, multiplier = 2) // 1s, 2s, 4s
    )
    public boolean checkAvailability(Long roomId) {
        AvailabilityResponseDTO response =  hotelClient
                .post()
                .uri("/{id}/confirm-availability", roomId)
                .retrieve()
                .body(AvailabilityResponseDTO.class);
        return response.available();
    }

    public void release(Long roomId) {
        hotelClient
                .post()
                .uri("/{id}/release", roomId)
                .retrieve()
                .body(Void.class);
    }

        public List<RoomResponseDTO> getAvailableRooms() {
            return hotelClient
                    .post()
                    .uri("/recommend")
                    .retrieve()
                    .body(new ParameterizedTypeReference<List<RoomResponseDTO>>() {});
    }

    @Recover()
    public void handleException(RestClientException e) {
        System.err.println("Error occurred while checking availability: " + e.getMessage());
    }
}
