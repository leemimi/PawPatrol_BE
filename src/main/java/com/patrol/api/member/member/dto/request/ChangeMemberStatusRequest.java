package com.patrol.api.member.member.dto.request;

import com.patrol.domain.member.member.enums.MemberStatus;

/**
 * packageName    : com.patrol.api.member.member.dto.request
 * fileName       : ChangeMemberStatusRequest
 * author         : sungjun
 * date           : 2025-03-05
 * description    : 자동 주석 생성
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-03-05        kyd54       최초 생성
 */
public record ChangeMemberStatusRequest(
        Long userId,
        MemberStatus status
) {
}
