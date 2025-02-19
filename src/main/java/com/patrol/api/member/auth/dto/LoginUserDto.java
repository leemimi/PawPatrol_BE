package com.patrol.api.member.auth.dto;



import com.patrol.api.member.member.dto.OAuthProviderStatus;
import com.patrol.domain.member.member.entity.Member;
import com.patrol.domain.member.member.enums.Gender;
import com.patrol.domain.member.member.enums.MemberRole;
import com.patrol.domain.member.member.enums.ProviderType;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;


@Builder
public record LoginUserDto(
    @NotNull Long id, @NotNull LocalDateTime createDate, @NotNull String nickname,
    @NotNull String email, LocalDate birthDate, String address, Gender gender,
    String phoneNumber, String profileImageUrl, boolean marketingAgree, ProviderType loginType, MemberRole role,
    Map<ProviderType, OAuthProviderStatus> oAuthProviderStatuses, boolean onlyOAuthAccount
  ) {

  public static LoginUserDto of(Member member) {
    return LoginUserDto.builder()
        .id(member.getId())
        .createDate(member.getCreatedAt())
        .nickname(member.getNickname())
        .email(member.getEmail())
        .birthDate(member.getBirthDate())
        .address(member.getAddress())
        .gender(member.getGender())
        .phoneNumber(member.getPhoneNumber())
        .profileImageUrl(member.getProfileImageUrl())
        .marketingAgree(member.isMarketingAgree())
        .loginType(member.getLoginType())
        .role(member.getRole())
        .oAuthProviderStatuses(member.getOAuthProviderStatuses())
        .onlyOAuthAccount(!member.hasPassword())
        .build();
  }
}
