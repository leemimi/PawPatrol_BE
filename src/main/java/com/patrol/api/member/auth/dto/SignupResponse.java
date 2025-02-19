package com.patrol.api.member.auth.dto;



import com.patrol.domain.member.member.entity.Member;
import com.patrol.domain.member.member.enums.Gender;
import com.patrol.domain.member.member.enums.ProviderType;
import lombok.Builder;
import lombok.NonNull;

import java.time.LocalDateTime;


@Builder
public record SignupResponse (
    @NonNull
    Long id,
    @NonNull
    String email,
    @NonNull
    String nickname,
    Gender gender,
    @NonNull
    LocalDateTime createDate,
    @NonNull ProviderType loginType
) {

    public static SignupResponse of(Member member) {
        return SignupResponse.builder()
            .id(member.getId())
            .email(member.getEmail())
            .nickname(member.getNickname())
            .gender(member.getGender())
            .createDate(member.getCreatedAt())
            .loginType(member.getLoginType())
            .build();
    }
}
