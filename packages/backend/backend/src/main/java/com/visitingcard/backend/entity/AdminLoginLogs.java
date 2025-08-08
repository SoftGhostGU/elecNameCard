package com.visitingcard.backend.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;

@Entity
@Table(name = "admin_login_logs")
@Data
public class AdminLoginLogs {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "login_ip", nullable = false)
    private String loginIp;

    @Column(name = "login_time", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp loginTime;

    @Column(name = "status", nullable = false)
    private Integer status;

    @Column(name = "user_agent")
    private String userAgent;

    @Column(name = "attempt_username")
    private String attemptUsername;
}
