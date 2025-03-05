package com.patrol.domain.member.member.repository;

import com.patrol.domain.member.member.entity.Member;
import com.patrol.domain.member.member.entity.ShelterMember;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * packageName    : com.patrol.domain.member.member.repository
 * fileName       : ShelterMemberRepository
 * author         : sungjun
 * date           : 2025-03-05
 * description    : 자동 주석 생성
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-03-05        kyd54       최초 생성
 */
public interface ShelterMemberRepository extends JpaRepository<ShelterMember, Long> {
    Boolean existsByBusinessRegistrationNumber(String businessRegistrationNumber);
}
