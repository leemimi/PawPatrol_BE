package com.patrol.api.member.member.dto;

import com.patrol.domain.member.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberResponseDto {
    private Long id;
    private String nickname;
    private String email;
    private String profileImageUrl;

    public MemberResponseDto(Member member) {
        this.id = member.getId();
        this.nickname = member.getNickname();
        this.email=member.getEmail();
        this.profileImageUrl = member.getProfileImageUrl();
    }
}