package com.visitingcard.backend.dto.User;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class UserUpdateRequest {

    @NotNull(message = "UserId cannot be null")
    private long userId;

    @NotNull(message = "Username cannot be null")
    private String username;

    @NotNull(message = "Company cannot be null")
    private String company;

    @NotNull(message = "Position cannot be null")
    private String position;

    @NotNull(message = "Phone Number cannot be null")
    private String phoneNumber;

    @NotNull(message = "Email cannot be null")
    private String email;

    private Timestamp updatedAt;
}
