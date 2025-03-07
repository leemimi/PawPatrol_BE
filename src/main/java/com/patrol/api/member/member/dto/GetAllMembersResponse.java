package com.patrol.api.member.member.dto;

import com.patrol.domain.member.member.enums.MemberRole;
import com.patrol.domain.member.member.enums.MemberStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * packageName    : com.patrol.api.member.member.dto
 * fileName       : GetAllMembersResponse
 * author         : sungjun
 * date           : 2025-03-05
 * description    : 자동 주석 생성
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-03-05        kyd54       최초 생성
 */
@Data
@Builder
public class GetAllMembersResponse {
    private Long id;
    private String email;
    private String nickname;
    private LocalDateTime createdAt;
    private MemberStatus status;
    private MemberRole role;
}
