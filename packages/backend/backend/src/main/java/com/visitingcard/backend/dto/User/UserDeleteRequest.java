package com.visitingcard.backend.dto.User;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserDeleteRequest {
    @NotNull(message = "userId cannot be null")
    private long userId;
}
