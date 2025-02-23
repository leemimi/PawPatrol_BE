package com.patrol.api.member.auth.dto.requestV2;

import jakarta.validation.constraints.NotBlank;

/**
 * packageName    : com.patrol.api.member.auth.dto.requestV2
 * fileName       : SocialConnectRequest
 * author         : sungjun
 * date           : 2025-02-21
 * description    : 자동 주석 생성
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-02-21        kyd54       최초 생성
 */
public record SocialConnectRequest (
        @NotBlank
        String email,
        @NotBlank
        String password,
        @NotBlank
        String tempToken
) {}
