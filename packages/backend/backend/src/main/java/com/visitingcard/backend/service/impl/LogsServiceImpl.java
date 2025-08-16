package com.visitingcard.backend.service.impl;

import com.visitingcard.backend.dto.Logs.LoginRequest;
import com.visitingcard.backend.dto.Logs.LogsProfile;
import com.visitingcard.backend.entity.AdminLoginLogs;
import com.visitingcard.backend.exception.BusinessException;
import com.visitingcard.backend.exception.ExceptionCodeEnum;
import com.visitingcard.backend.repository.LogsRepository;
import com.visitingcard.backend.service.LogsService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.logging.Logger;

@Service
@AllArgsConstructor
public class LogsServiceImpl implements LogsService {

    private final Logger logger = Logger.getLogger(LogsServiceImpl.class.getName());
    private final LogsRepository logsRepository;

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
            AdminLoginLogs adminLoginLogs = new AdminLoginLogs();
            adminLoginLogs.setLoginIp(clientIp);
            adminLoginLogs.setLoginTime(new Timestamp(System.currentTimeMillis()));
            adminLoginLogs.setStatus(logsProfile.getStatus());
            adminLoginLogs.setUserAgent(userAgent);
            adminLoginLogs.setAttemptUsername(username);
            logsRepository.save(adminLoginLogs);
            return logsProfile;
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException(ExceptionCodeEnum.USER_PROFILE_ERROR, "管理员登录失败：" + e.getMessage());
        }
    }

    @Override
    public LogsProfile[] getAllLogs() {
        logger.info("[LogsServiceImpl] 获取所有日志");

        try {
            logger.info("[LogsServiceImpl] 调用LogsRepository.getAllLogs()获取所有日志");
            List<AdminLoginLogs> logs = logsRepository.findAllWithLog();
            logger.info("[LogsServiceImpl] 收到所有Log日志" + logs);
            LogsProfile[] logsProfiles = new LogsProfile[logs.size()];
            for (int i = 0; i < logs.size(); i++) {
                AdminLoginLogs log = logs.get(i);
                LogsProfile logsProfile = new LogsProfile();
                logsProfile.setId(log.getId());
                logsProfile.setLoginIp(log.getLoginIp());
                logsProfile.setLoginTime(log.getLoginTime());
                logsProfile.setStatus(log.getStatus());
                logsProfile.setUserAgent(log.getUserAgent());
                logsProfile.setAttemptUsername(log.getAttemptUsername());
                logsProfiles[i] = logsProfile;
            }
            return logsProfiles;
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException(ExceptionCodeEnum.LOGS_NOT_FOUND, "获取所有日志失败：" + e.getMessage());
        }
    }
}
