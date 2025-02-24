package com.patrol.api.member.auth.dto.requestV2;

import jakarta.validation.constraints.NotBlank;

/**
 * packageName    : com.patrol.api.member.auth.dto.requestV2
 * fileName       : LoginRequest
 * author         : sungjun
 * date           : 2025-02-19
 * description    : 자동 주석 생성
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-02-19        kyd54       최초 생성
 */
public record LoginRequest (
        @NotBlank
        String email,
        @NotBlank
        String password
) {}
