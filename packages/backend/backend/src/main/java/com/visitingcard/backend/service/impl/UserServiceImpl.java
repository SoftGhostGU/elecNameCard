package com.visitingcard.backend.service.impl;

import com.visitingcard.backend.dto.User.UserDeleteRequest;
import com.visitingcard.backend.dto.User.UserInfoRequest;
import com.visitingcard.backend.dto.User.UserProfileDTO;
import com.visitingcard.backend.entity.User;
import com.visitingcard.backend.exception.BusinessException;
import com.visitingcard.backend.exception.ExceptionCodeEnum;
import com.visitingcard.backend.repository.UserRepository;
import com.visitingcard.backend.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final Logger logger = Logger.getLogger(UserServiceImpl.class.getName());
    private final UserRepository userRepository;

    /**
     * Find user by openid
     * @param openid
     * @return
     */
    @Override
    public UserProfileDTO findUserByOpenid(String openid) {
        logger.info("[UserServiceImpl] 处理查询用户信息请求：" + openid);
        try {
            User user = userRepository.findUserByOpenidWithLog(openid)
                   .orElseThrow(() -> new BusinessException(ExceptionCodeEnum.USER_NOT_FOUND, "用户不存在"));
            UserProfileDTO userProfileDTO = getUserProfileDTO(user);
            logger.info("[UserServiceImpl] 查询用户信息成功：" + userProfileDTO.toString());
            return userProfileDTO;
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException(ExceptionCodeEnum.USER_PROFILE_ERROR, "获取用户信息失败：" + e.getMessage());
        }
    }

    /**
     * Query all user profiles
     * @return All user profiles
     */
    @Override
    public Iterable<UserProfileDTO> queryAllUserInfo() {
        logger.info("[UserServiceImpl] 处理查询所有用户信息请求");
        try {
            Iterable<User> users = userRepository.findAllWithLog();
            logger.info("[UserServiceImpl] 查询所有用户信息成功" + users.toString());
            return StreamSupport.stream(users.spliterator(), false)
                    .map(UserProfileDTO::new)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new BusinessException(ExceptionCodeEnum.USER_PROFILE_ERROR, "获取用户信息失败：" + e.getMessage());
        }
    }

    /**
     * Delete user by user id
     * @param userDeleteRequest
     * @return Deleted user information
     */
    @Override
    public UserProfileDTO deleteUser(UserDeleteRequest userDeleteRequest) {
        logger.info("[UserServiceImpl] 处理删除用户请求：" + userDeleteRequest.toString());

        try {
            long userId = userDeleteRequest.getUserId();
            User user = userRepository.findByUserIdWithLog(userId)
                   .orElseThrow(() -> new BusinessException(ExceptionCodeEnum.USER_NOT_FOUND, "用户不存在"));
            userRepository.deleteByUserIdWithLog(userId);
            logger.info("[UserServiceImpl] 删除用户成功：" + user.toString());
            return getUserProfileDTO(user);
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException(ExceptionCodeEnum.USER_PROFILE_ERROR, "删除用户失败：" + e.getMessage());
        }
    }

    /**
     * Query user profile by user id
     * @param userInfoRequest
     * @return User information
     */
    @Override
    public UserProfileDTO queryUserProfile(UserInfoRequest userInfoRequest) {
        logger.info("[UserServiceImpl] 处理查询用户信息请求：" + userInfoRequest.toString());
        try {
            long userId = userInfoRequest.getUserId();
            User user = userRepository.findByUserIdWithLog(userId)
                    .orElseThrow(() -> new BusinessException(ExceptionCodeEnum.USER_NOT_FOUND, "用户不存在"));

            UserProfileDTO userProfileDTO = getUserProfileDTO(user);

            logger.info("[UserServiceImpl] 获取用户信息成功：" + userProfileDTO.toString());
            return userProfileDTO;
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException(ExceptionCodeEnum.USER_PROFILE_ERROR, "获取用户信息失败：" + e.getMessage());
        }
    }

    private static UserProfileDTO getUserProfileDTO(User user) {
        UserProfileDTO userProfileDTO = new UserProfileDTO();
        userProfileDTO.setUserId(user.getUserId());
        userProfileDTO.setOpenid(user.getOpenid());
        userProfileDTO.setCompanyLogo(user.getCompanyLogo());
        userProfileDTO.setCompany(user.getCompany());
        userProfileDTO.setAddress(user.getAddress());
        userProfileDTO.setPosition(user.getPosition());
        userProfileDTO.setPhoneNumber(user.getPhoneNumber());
        userProfileDTO.setEmail(user.getEmail());
        userProfileDTO.setWebsite(user.getWebsite());
        userProfileDTO.setCreatedAt(user.getCreatedAt());
        userProfileDTO.setUpdatedAt(user.getUpdatedAt());
        return userProfileDTO;
    }
}
