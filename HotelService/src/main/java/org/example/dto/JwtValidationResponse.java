package org.example.dto;

// Response: what your user service returns
public record JwtValidationResponse(
        Long userId,
        String username,
        String role
//        boolean valid
) {}
