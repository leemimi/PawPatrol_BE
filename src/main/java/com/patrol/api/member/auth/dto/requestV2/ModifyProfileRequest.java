package com.patrol.api.member.auth.dto.requestV2;

import org.springframework.web.multipart.MultipartFile;

/**
 * packageName    : com.patrol.api.member.auth.dto.requestV2
 * fileName       : ModifyProfileRequest
 * author         : sungjun
 * date           : 2025-02-25
 * description    : 자동 주석 생성
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-02-25        kyd54       최초 생성
 */
public record ModifyProfileRequest (
        String nickname,
        String currentPassword,
        String newPassword,
        String confirmPassword,
        String phoneNumber,
        MultipartFile file
) {}
