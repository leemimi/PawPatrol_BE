package com.patrol.api.notification.fcm.dto;

import com.patrol.api.member.auth.dto.requestV2.LoginRequest;
import com.patrol.global.webMvc.LoginUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class FCMTokenDao {

    private final StringRedisTemplate tokenRedisTemplate;

    public void saveToken(LoginRequest loginRequest) {
        tokenRedisTemplate.opsForValue()
                .set(loginRequest.email(), loginRequest.token());
    }

    public String getToken(String email) {
        return tokenRedisTemplate.opsForValue().get(email);
    }

    public void deleteToken(String email) {
        tokenRedisTemplate.delete(email);
    }
}