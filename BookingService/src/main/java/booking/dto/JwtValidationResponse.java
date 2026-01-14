package booking.dto;

// Response: what your user service returns
public record JwtValidationResponse(
        Long id,
        String username,
        String role
//        boolean valid
) {}
