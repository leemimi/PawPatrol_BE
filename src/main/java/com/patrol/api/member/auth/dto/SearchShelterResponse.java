package com.patrol.api.member.auth.dto;

import lombok.Builder;

/**
 * packageName    : com.patrol.api.member.auth.dto
 * fileName       : SearchShelterResponse
 * author         : sungjun
 * date           : 2025-03-06
 * description    : 자동 주석 생성
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-03-06        kyd54       최초 생성
 */
@Builder
public record SearchShelterResponse(
        Long id,
        String name,
        String address,
        String tel
) {
}
