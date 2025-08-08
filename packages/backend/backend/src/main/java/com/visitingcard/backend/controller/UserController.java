package com.visitingcard.backend.controller;

import com.visitingcard.backend.dto.User.UserDeleteRequest;
import com.visitingcard.backend.dto.User.UserInfoRequest;
import com.visitingcard.backend.dto.User.UserProfileDTO;
import com.visitingcard.backend.entity.User;
import com.visitingcard.backend.exception.BusinessException;
import com.visitingcard.backend.exception.ExceptionCodeEnum;
import com.visitingcard.backend.repository.UserRepository;
import com.visitingcard.backend.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/user")
@AllArgsConstructor
public class UserController {

    private final Logger logger = Logger.getLogger(UserController.class.getName());

    public final UserRepository userRepository;
    public final UserService userService;

    /**
     * 用户登录
     * @param openid
     * @return ResponseEntity<Map<String, Object>>
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestHeader(value = "openid") String openid) {

        logger.info("[UserController] 接受到请求 - " + openid);

        try {
            logger.info("[UserController] 调用UserRepository.findUserByOpenid()方法，用户登录");
            UserProfileDTO user = userService.findUserByOpenid(openid);
            logger.info("[UserController] 正在登录的用户信息：" + user);

            return getMapResponseEntity(user, "登录成功");
        } catch (BusinessException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("code", e.getCode());
            errorResponse.put("message", e.getMessage());
            errorResponse.put("data", null);
            logger.warning("[UserController] 用户信息获取失败，若为首次登录，请填写具体内容。返回结果：" + errorResponse);
            return ResponseEntity.ok(errorResponse);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("code", 500);
            errorResponse.put("message", "服务器内部错误");
            logger.warning("[UserController] 用户登录失败，服务器内部错误：" + errorResponse);
            return ResponseEntity.ok(errorResponse);
        }
    }

    /**
     * 查询所有用户信息
     * @return ResponseEntity<Map<String, Object>>
     */
    @PostMapping("/queryAllUserInfo")
    public ResponseEntity<Map<String, Object>> queryAllUserInfo() {
        logger.info("[UserController] 接受到请求 - 查询所有用户信息");

        try {
            logger.info("[UserController] 调用userService.queryAllUserInfo()方法，查询所有用户信息");
            Iterable<UserProfileDTO> users = userService.queryAllUserInfo();
            logger.info("[UserController] 查询到的所有用户信息：" + users);

            Map<String, Object> response = new HashMap<>();
            response.put("code", 200);
            response.put("message", "查询成功");
            response.put("data", users);
            logger.info("[UserController] 查询所有用户信息成功，返回结果：" + response);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("code", 500);
            errorResponse.put("message", "服务器内部错误");
            logger.warning("[UserController] 查询所有用户信息失败，服务器内部错误：" + errorResponse);
            return ResponseEntity.ok(errorResponse);
        }
    }

    @PostMapping("/deleteUser")
    public ResponseEntity<Map<String, Object>> deleteUser(@RequestBody UserDeleteRequest userDeleteRequest) {
        logger.info("[UserController] 接受到请求 - " + userDeleteRequest.getUserId());
        logger.info("[UserController] 删除用户信息 - 接收到删除请求：" + userDeleteRequest);

        try {
            logger.info("[UserController] 调用userService.deleteUser()方法，删除用户信息");
            UserProfileDTO user = userService.deleteUser(userDeleteRequest);
            logger.info("[UserController] 删除用户信息成功");

            Map<String, Object> response = new HashMap<>();
            response.put("code", 201);
            response.put("message", "删除成功");
            response.put("data", null);
            logger.info("[UserController] 删除用户信息成功，返回结果：" + response);

            return ResponseEntity.ok(response);
        } catch (BusinessException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("code", e.getCode());
            errorResponse.put("message", e.getMessage());
            errorResponse.put("data", null);
            logger.warning("[UserController] 删除用户信息失败，返回结果：" + errorResponse);
            return ResponseEntity.ok(errorResponse);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("code", 500);
            errorResponse.put("message", "服务器内部错误");
            logger.warning("[UserController] 删除用户信息失败，服务器内部错误：" + errorResponse);
            return ResponseEntity.ok(errorResponse);
        }
    }


    /**
     * 查询用户信息
     * @param userInfoRequest
     * @return ResponseEntity<Map<String, Object>>
     */
    @PostMapping("/queryUserInfo")
    public ResponseEntity<Map<String, Object>> queryUserInfo(@RequestBody UserInfoRequest userInfoRequest) {
        logger.info("[UserController] 接受到请求 - " + userInfoRequest.getUserId());
        logger.info("[UserController] 查询用户信息 - 接收到查询请求：" + userInfoRequest);

        try {
            logger.info("[UserController] 调用UserController.queryUserInfo()方法，查询用户信息");
            UserProfileDTO userProfileDTO = userService.queryUserProfile(userInfoRequest);
            logger.info("[UserController] 查询返回的结果：" + userProfileDTO);
            User user = userRepository.findByUserIdWithLog(userProfileDTO.getUserId()).orElseThrow();
            logger.info("[UserController] 查询到的用户信息：" + user);

            UserProfileDTO userProfileDTO1 = new UserProfileDTO(user);
            return getMapResponseEntity(userProfileDTO1, "查询成功");
        } catch (BusinessException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("code", e.getCode());
            errorResponse.put("message", e.getMessage());
            errorResponse.put("data", null);
            logger.warning("[UserController] 查询用户信息失败，返回结果：" + errorResponse);
            return ResponseEntity.ok(errorResponse);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("code", 500);
            errorResponse.put("message", "服务器内部错误");
            logger.warning("[UserController] 查询用户信息失败，服务器内部错误：" + errorResponse);
            return ResponseEntity.ok(errorResponse);
        }
    }

    private ResponseEntity<Map<String, Object>> getMapResponseEntity(UserProfileDTO user, String message) {
        Map<String, Object> userData = new HashMap<>();
        userData.put("userId", user.getUserId());
        userData.put("openid", user.getOpenid());
        userData.put("username", user.getUsername());
        userData.put("companyLogo", user.getCompanyLogo());
        userData.put("company", user.getCompany());
        userData.put("address", user.getAddress());
        userData.put("position", user.getPosition());
        userData.put("phoneNumber", user.getPhoneNumber());
        userData.put("email", user.getEmail());
        userData.put("website", user.getWebsite());
        userData.put("createdAt", user.getCreatedAt());
        userData.put("updatedAt", user.getUpdatedAt());

        Map<String, Object> userInfoResponse = new HashMap<>();
        userInfoResponse.put("code", 200);
        userInfoResponse.put("message", message);
        userInfoResponse.put("data", userData);
        logger.info("[UserController] 查询用户信息成功，返回结果：" + userInfoResponse);

        return ResponseEntity.ok(userInfoResponse);
    }
}
