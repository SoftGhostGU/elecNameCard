package com.visitingcard.backend.exception;

import lombok.Getter;

/**
 * 异常状态码枚举类
 */
@Getter
public enum ExceptionCodeEnum {
    // 个人信息相关异常
    USER_NOT_FOUND(10001, "用户不存在"),
    PROFILE_UPDATE_FAILED(10002, "个人资料更新失败"),
    INVALID_TOKEN(10003, "无效的令牌"),
    TOKEN_EXPIRED(10004, "令牌已过期"),
    PARAM_ERROR(10005, "参数错误"),
    PARAM_NULL_ERROR(10007, "参数不能为空"),
    // 用户相关异常 from enums
    USERNAME_ALREADY_EXISTS(10011, "用户名已存在"),
    PHONE_ALREADY_EXISTS(10014, "手机号已存在"),
    INVALID_PASSWORD(10013, "密码错误"),
    USER_REGISTER_FAILED(10015, "用户注册失败"),
    USER_LOGIN_ERROR(10016, "用户登录异常"),
    USER_PROFILE_ERROR(10017, "用户资料获取异常"),
    USER_UPDATE_ERROR(10018, "用户资料更新异常"),
    // 管理员相关异常 from enums
    ADMIN_LOGIN_FAILED(10021, "管理员登录失败"),
    // 系统通用异常 from enums
    DEFAULT_ERROR(500, "默认错误"),
    SYSTEM_ERROR(99999, "系统异常");


    private final int code;
    private final String message;

    ExceptionCodeEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

}