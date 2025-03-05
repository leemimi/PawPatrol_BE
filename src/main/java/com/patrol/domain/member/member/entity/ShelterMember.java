package com.patrol.domain.member.member.entity;

import com.patrol.global.jpa.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToOne;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * packageName    : com.patrol.domain.member.member.entity
 * fileName       : ShelterMember
 * author         : sungjun
 * date           : 2025-03-04
 * description    : 자동 주석 생성
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-03-04        kyd54       최초 생성
 */
@Entity
@SuperBuilder
@RequiredArgsConstructor
public class ShelterMember extends BaseEntity {
    @OneToOne(fetch = FetchType.LAZY)
    private Member member;  // 회원과 연결
    private String BusinessName;    // 사업장명
    private String owner;   // 대표자
    private String address; // 사업장 주소
    private String businessRegistrationNumber;  // 사업자 등록번호
}
