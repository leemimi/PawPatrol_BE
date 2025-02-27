package com.patrol.api.member.auth.dto.requestV2;

import jakarta.validation.constraints.NotBlank;

/**
 * packageName    : com.patrol.api.member.auth.dto.requestV2
 * fileName       : VerifyResetCodeRequest
 * author         : sungjun
 * date           : 2025-02-26
 * description    : 자동 주석 생성
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-02-26        kyd54       최초 생성
 */
public record VerifyResetCodeRequest (
        @NotBlank
        String email,
        @NotBlank
        String verificationCode,
        @NotBlank
        String continuationToken
){}
