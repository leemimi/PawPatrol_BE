package com.patrol.domain.member.auth.service;

import com.patrol.domain.member.member.entity.Member;
import com.patrol.domain.member.member.enums.MemberRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthTokenService authTokenService;

    public String genAccessToken(Member member) {
        return authTokenService.genAccessToken(member);
    }

    public String genAuthToken(Member member) {
        return member.getApiKey() + " " + genAccessToken(member);
    }

    public Member getMemberFromAccessToken(String accessToken) {
        Map<String, Object> payload = authTokenService.payload(accessToken);
        if (payload == null) {
            return null;
        }
        long id = (long) payload.get("id");
        String email = (String) payload.get("email");
        String nickname = (String) payload.get("nickname");
        String profileImageUrl = (String) payload.get("profileImageUrl");
        String role = (String) payload.get("role");

        return new Member(id, email, nickname, profileImageUrl, MemberRole.valueOf(role));
    }

}
