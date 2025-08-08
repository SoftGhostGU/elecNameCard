package com.visitingcard.backend.dto.User;

import com.visitingcard.backend.entity.User;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class UserProfileDTO {
    private long userId;
    private String openid;
    private String username;
    private String companyLogo;
    private String company;
    private String address;
    private String position;
    private String phoneNumber;
    private String email;
    private String website;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public UserProfileDTO() {
    }

    public UserProfileDTO(User user) {
        this.userId = user.getUserId();
        this.openid = user.getOpenid();
        this.username = user.getUsername();
        this.companyLogo = user.getCompanyLogo();
        this.company = user.getCompany();
        this.address = user.getAddress();
        this.position = user.getPosition();
        this.phoneNumber = user.getPhoneNumber();
        this.email = user.getEmail();
        this.website = user.getWebsite();
        this.createdAt = user.getCreatedAt();
        this.updatedAt = user.getUpdatedAt();
    }
}
