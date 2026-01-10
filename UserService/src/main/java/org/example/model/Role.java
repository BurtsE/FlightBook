package org.example.model;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;

@Getter
public enum Role {

    USER("user"),
    ADMIN("admin");
    @Enumerated(EnumType.STRING)
    private final String role;

    Role(String role) {
        this.role = role;
    }
}
