package com.visitingcard.backend.service;

import com.visitingcard.backend.dto.User.UserDeleteRequest;
import com.visitingcard.backend.dto.User.UserInfoRequest;
import com.visitingcard.backend.dto.User.UserProfileDTO;
import com.visitingcard.backend.dto.User.UserUpdateRequest;
import com.visitingcard.backend.entity.User;

public interface UserService {

    /**
     * Find user by openid
     * @param openid
     * @return User information
     */
    UserProfileDTO findUserByOpenid(String openid);

    /**
     * Query all user profiles
     * @return User information list
     */
    Iterable<UserProfileDTO> queryAllUserInfo();

    /**
     * Delete user by user id
     * @param userDeleteRequest
     * @return User information
     */
    UserProfileDTO deleteUser(UserDeleteRequest userDeleteRequest);

    /**
     * Query user profile by user id
     * @param userInfoRequest
     * @return User information
     */
    UserProfileDTO queryUserProfile(UserInfoRequest userInfoRequest);

    /**
     * Update user information
     * @param userUpdateRequest
     * @return User information
     */
    User updateUserInfo(UserUpdateRequest userUpdateRequest);
}
