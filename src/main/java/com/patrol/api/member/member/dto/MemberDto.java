package com.patrol.api.member.member.dto;



import com.patrol.domain.member.member.entity.Member;
import com.patrol.domain.member.member.enums.Gender;
import com.patrol.domain.member.member.enums.MemberStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class MemberDto {
    @NotNull
    private final Long id;
    private final LocalDateTime createDate;
    @NotNull
    private final String nickname;
    private final LocalDate birthDate;
    private final String address;
    private final Gender gender;
    private final String phoneNumber;
    private final String profileImageUrl;
    private final MemberStatus status;

    public MemberDto(Member member) {
        this.id = member.getId();
        this.nickname = member.getNickname();
        this.birthDate = member.getBirthDate();
        this.address = member.getAddress();
        this.gender = member.getGender();
        this.phoneNumber = member.getPhoneNumber();
        this.profileImageUrl = member.getProfileImageUrl();
        this.status = member.getStatus();
        this.createDate = member.getCreatedAt();
    }
}
