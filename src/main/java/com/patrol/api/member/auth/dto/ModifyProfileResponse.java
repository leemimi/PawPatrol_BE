package com.patrol.api.member.auth.dto;

import lombok.Builder;

/**
 * packageName    : com.patrol.api.member.auth.dto.request
 * fileName       : ModifyProfileResponse
 * author         : sungjun
 * date           : 2025-02-26
 * description    : 자동 주석 생성
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-02-26        kyd54       최초 생성
 */
@Builder
public record ModifyProfileResponse (
        String profileImage
){}
