package com.visitingcard.backend.dto.Logs;

import com.visitingcard.backend.entity.AdminLoginLogs;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class LogsProfile {

    private long id;
    private String loginIp;
    private Timestamp loginTime;
    private Integer status;
    private String userAgent;
    private String attemptUsername;

    public LogsProfile() {}

    public LogsProfile(AdminLoginLogs adminLoginLogs) {
        this.loginIp = adminLoginLogs.getLoginIp();
        this.loginTime = adminLoginLogs.getLoginTime();
        this.status = adminLoginLogs.getStatus();
        this.userAgent = adminLoginLogs.getUserAgent();
        this.attemptUsername = adminLoginLogs.getAttemptUsername();
    }

}
