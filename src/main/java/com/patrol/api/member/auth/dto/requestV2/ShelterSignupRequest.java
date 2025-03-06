package com.patrol.api.member.auth.dto.requestV2;

/**
 * packageName    : com.patrol.api.member.auth.dto.requestV2
 * fileName       : ShelterSignupRequest
 * author         : sungjun
 * date           : 2025-03-04
 * description    : 자동 주석 생성
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-03-04        kyd54       최초 생성
 */
public record ShelterSignupRequest(
        String email,
        String password,
        String owner,
        String nickname,    // 회사명 들어감
        String address,
        String startDate,
        String businessRegistrationNumber,
        Long shelterId,
        String shelterTel
) {

}
