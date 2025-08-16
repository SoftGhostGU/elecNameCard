package com.visitingcard.backend.controller;

import com.visitingcard.backend.dto.Logs.LoginRequest;
import com.visitingcard.backend.dto.Logs.LogsProfile;
import com.visitingcard.backend.exception.BusinessException;
import com.visitingcard.backend.exception.ExceptionCodeEnum;
import com.visitingcard.backend.service.LogsService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/admin")
@AllArgsConstructor
public class LoginLogsController {

    private final Logger logger = Logger.getLogger(LoginLogsController.class.getName());
    private final LogsService logsService;

    /**
     * 管理员登录接口
     * @param loginRequest
     * @param userAgent
     * @param request
     * @return
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(
            @RequestBody LoginRequest loginRequest,
            @RequestHeader(value = "User-Agent", required = false) String userAgent,
            HttpServletRequest request
    ) {

        // 获取并处理客户端信息
        String clientIp = getClientIp(request);
        String safeIp = anonymizeIp(clientIp);
        String safeUA = sanitizeUserAgent(userAgent);

        logger.info(String.format(
                "[LoginLogsController] 登录请求 - 用户名: %s, IP: %s, User-Agent: %s",
                loginRequest.getUsername(),
                clientIp,
                safeUA
        ));

        try {
            LogsProfile logsProfile = logsService.login(
                    loginRequest,
                    clientIp, // 传递真实IP给service层
                    safeUA
            );

            if (logsProfile.getStatus() == 0) {
                throw new BusinessException(ExceptionCodeEnum.ADMIN_LOGIN_FAILED, "账号密码输入错误");
            }

            Map<String, Object> successResponse = new HashMap<>();
            successResponse.put("code", 200);
            successResponse.put("message", "管理员登录成功");
            successResponse.put("data", logsProfile);

            logger.info(String.format(
                    "[LoginLogsController] 登录成功 - 用户名: %s, IP: %s",
                    loginRequest.getUsername(),
                    safeIp
            ));

            return ResponseEntity.ok(successResponse);

        } catch (BusinessException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("code", e.getCode());
            errorResponse.put("message", e.getMessage());
            errorResponse.put("data", null);

            logger.warning(String.format(
                    "[LoginLogsController] 登录失败 - 用户名: %s, IP: %s, 原因: %s",
                    loginRequest.getUsername(),
                    safeIp,
                    e.getMessage()
            ));

            return ResponseEntity.ok(errorResponse);

        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("code", 500);
            errorResponse.put("message", "服务器内部错误");

            logger.severe(String.format(
                    "[LoginLogsController] 系统错误 - 用户名: %s, IP: %s, 异常: %s",
                    loginRequest.getUsername(),
                    safeIp,
                    e.toString()
            ));

            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    @GetMapping("/queryAllLogs")
    public ResponseEntity<Map<String, Object>> getAllLogs() {
        logger.info("[LoginLogsController] 获取所有登录日志");

        try {
            logger.info("[LoginLogsController] 调用logsService.getAllLogs()，查询所有登录日志");
            LogsProfile[] logs = logsService.getAllLogs();
            logger.info("[LoginLogsController] 得到返回结果：" + Arrays.toString(logs));
            Map<String, Object> successResponse = new HashMap<>();
            successResponse.put("code", 200);
            successResponse.put("message", "获取所有登录日志成功");
            successResponse.put("data", logs);

            return ResponseEntity.ok(successResponse);
        } catch (BusinessException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("code", e.getCode());
            errorResponse.put("message", e.getMessage());
            errorResponse.put("data", null);
            return ResponseEntity.ok(errorResponse);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("code", 500);
            errorResponse.put("message", "服务器内部错误");
            errorResponse.put("data", null);
            return ResponseEntity.ok(errorResponse);
        }
    }

    /**
     * 获取客户端真实IP（处理代理情况）
     * @param request 请求对象
     * @return 客户端IP地址
     */
    private String getClientIp(HttpServletRequest request) {
        String[] headers = {"X-Forwarded-For", "Proxy-Client-IP", "WL-Proxy-Client-IP"};
        for (String header : headers) {
            String ip = request.getHeader(header);
            if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
                return ip.split(",")[0]; // 处理多级代理情况
            }
        }
        return request.getRemoteAddr();
    }

    /**
     * IP地址脱敏处理
     * @param ip
     * @return IP地址脱敏
     */
    private String anonymizeIp(String ip) {
        if (ip == null) return "unknown";
        return ip.replaceAll("(\\d+)\\.(\\d+)\\.(\\d+)\\.\\d+", "$1.$2.$3.xxx");
    }

    /**
     * 安全的User-Agent处理
     * @param userAgent
     * @return User-Agent脱敏
     */
    private String sanitizeUserAgent(String userAgent) {
        if (userAgent == null) return "unknown";
        return userAgent.replaceAll("[A-Z0-9]{16,}", "***");
    }
}