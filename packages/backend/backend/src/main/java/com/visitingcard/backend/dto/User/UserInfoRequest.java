package com.visitingcard.backend.dto.User;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 获取用户信息DTO
 */
@Data
public class UserInfoRequest {
    @NotBlank(message = "userId不能为空")
    private final long userId;
}
