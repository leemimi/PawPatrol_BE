package com.patrol.domain.notification.service;

import com.patrol.api.member.auth.dto.requestV2.LoginRequest;
import com.patrol.api.notification.fcm.dto.FCMTokenDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FCMService {
    private final FCMTokenDao fcmTokenDao;

    public void saveToken(LoginRequest loginRequest) {
        fcmTokenDao.saveToken(loginRequest);
    }

}
