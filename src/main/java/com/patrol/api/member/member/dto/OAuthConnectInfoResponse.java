package com.patrol.api.member.member.dto;

import lombok.Builder;

/**
 * packageName    : com.patrol.api.member.member.dto
 * fileName       : OAuthConnectInfoResponse
 * author         : sungjun
 * date           : 2025-02-27
 * description    : 자동 주석 생성
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-02-27        kyd54       최초 생성
 */
@Builder
public record OAuthConnectInfoResponse(
        boolean kakao,
        boolean naver,
        boolean google
) {}
