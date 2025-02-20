package com.patrol.domain.member.member.entity;

import com.patrol.domain.member.member.enums.MemberRole;
import com.patrol.domain.member.member.enums.MemberStatus;
import com.patrol.domain.member.member.enums.ProviderType;
import com.patrol.global.jpa.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

/**
 * packageName    : com.patrol.domain.member.member.entity
 * fileName       : V2Member
 * author         : sungjun
 * date           : 2025-02-19
 * description    : 자동 주석 생성
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-02-19        kyd54       최초 생성
 */
@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class V2Member extends BaseEntity {
    private String email;
    private String password;
    private String phoneNum;
    private String profilePath;
    private String location;
    @Enumerated(EnumType.STRING)
    private MemberStatus status;
    @Enumerated(EnumType.STRING)
    private MemberRole role = MemberRole.ROLE_USER; // 권한(관리자, 사용자)
    private String nickName;
    private LocalDate birth;
    @Enumerated(EnumType.STRING)
    private ProviderType loginType = ProviderType.SELF;  // 현재 로그인 방식
    
    // api key 추가예정
}
