package org.example.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UpdateUserDTO {

    @JsonProperty("id")
    public Long id;

    @JsonProperty("new_username")
    public String newUsername;

    @JsonProperty("new_password")
    public String newPassword;

    @JsonProperty("new_role")
    public String newRole;
}
