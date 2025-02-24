package com.patrol.api.member.auth.dto;

import com.patrol.domain.member.member.enums.ProviderType;
import lombok.Builder;
import lombok.Getter;

/**
 * packageName    : com.patrol.api.member.auth.dto.requestV2
 * fileName       : SocialTokenInfo
 * author         : sungjun
 * date           : 2025-02-21
 * description    : 자동 주석 생성
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-02-21        kyd54       최초 생성
 */
@Getter
@Builder
public class SocialTokenInfo {
    private ProviderType providerType;
    private String providerId;
    private String email;
}
