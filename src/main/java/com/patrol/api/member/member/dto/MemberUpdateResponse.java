package com.patrol.api.member.member.dto;



import com.patrol.domain.member.member.entity.Member;
import com.patrol.domain.member.member.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
public class MemberUpdateResponse {

    private String nickname;
    private String phoneNumber;
    private boolean marketingAgree;
    private Gender gender;
    private String profileImageUrl;
    private String address;
    private LocalDate birthDate;

    public static MemberUpdateResponse of(Member member) {
        return MemberUpdateResponse.builder().nickname(member.getNickname())
            .phoneNumber(member.getPhoneNumber()).marketingAgree(member.isMarketingAgree())
            .profileImageUrl(member.getProfileImageUrl()).birthDate(member.getBirthDate())
            .gender(member.getGender()).address(member.getAddress()).build();
    }
}
