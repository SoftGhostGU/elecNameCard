package com.visitingcard.backend.service;

import com.visitingcard.backend.dto.Logs.LoginRequest;
import com.visitingcard.backend.dto.Logs.LogsProfile;

public interface LogsService {

    /**
     * Method to login
     * @param request
     * @return
     */
    LogsProfile login(LoginRequest request, String clientIp, String userAgent);

}
