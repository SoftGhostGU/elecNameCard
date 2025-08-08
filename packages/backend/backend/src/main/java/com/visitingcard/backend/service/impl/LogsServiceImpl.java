package com.visitingcard.backend.service.impl;

import com.visitingcard.backend.dto.Logs.LoginRequest;
import com.visitingcard.backend.dto.Logs.LogsProfile;
import com.visitingcard.backend.exception.BusinessException;
import com.visitingcard.backend.exception.ExceptionCodeEnum;
import com.visitingcard.backend.service.LogsService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.logging.Logger;

@Service
@AllArgsConstructor
public class LogsServiceImpl implements LogsService {

    private final Logger logger = Logger.getLogger(LogsServiceImpl.class.getName());

    /**
     * Method to login
     * @param loginRequest
     * @return
     */
    @Override
    public LogsProfile login(LoginRequest loginRequest, String clientIp, String userAgent) {
        logger.info("[LogsServiceImpl] 处理管理员登录日志");

        try {
            String username = loginRequest.getUsername();
            String password = loginRequest.getPassword();
            logger.info("[LogsServiceImpl] 管理员登录日志: 用户名: " + username + " 密码: " + password + " 登录IP: " + clientIp + " 用户代理: " + userAgent);

            LogsProfile logsProfile = new LogsProfile();
            logsProfile.setLoginIp(clientIp);
            logsProfile.setLoginTime(new Timestamp(System.currentTimeMillis()));
            logsProfile.setUserAgent(userAgent);
            logsProfile.setAttemptUsername(username);
            String adminUsername = "admin";
            String adminPassword = "admin";
            if (username.equals(adminUsername) && password.equals(adminPassword)) {
                logsProfile.setStatus(1);
                logger.info("[LogsServiceImpl] 管理员登录成功");
            } else {
                logsProfile.setStatus(0);
                logger.warning("[LogsServiceImpl] 管理员登录失败");
            }
            return logsProfile;
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException(ExceptionCodeEnum.USER_PROFILE_ERROR, "管理员登录失败：" + e.getMessage());
        }
    }
}
