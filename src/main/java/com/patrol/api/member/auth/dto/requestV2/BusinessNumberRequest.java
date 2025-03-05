package com.patrol.api.member.auth.dto.requestV2;

import lombok.NonNull;

/**
 * packageName    : com.patrol.api.member.auth.dto.requestV2
 * fileName       : BusinessNumberRequest
 * author         : sungjun
 * date           : 2025-03-04
 * description    : 자동 주석 생성
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-03-04        kyd54       최초 생성
 */
public record BusinessNumberRequest(
        @NonNull
        String businessNumber,
        @NonNull
        String startDate,
        @NonNull
        String owner
) {
}
