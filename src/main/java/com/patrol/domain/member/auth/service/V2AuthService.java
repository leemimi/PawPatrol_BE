package com.patrol.domain.member.auth.service;

import com.patrol.api.member.auth.dto.request.SignupRequest;
import com.patrol.domain.member.member.entity.Member;
import com.patrol.domain.member.member.repository.V2MemberRepository;
import com.patrol.global.exceptions.ErrorCodes;
import com.patrol.global.exceptions.ServiceException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * packageName    : com.patrol.domain.member.auth.service
 * fileName       : V2AuthService
 * author         : sungjun
 * date           : 2025-02-19
 * description    : 자동 주석 생성
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-02-19        kyd54       최초 생성
 */
@Service
@RequiredArgsConstructor
@Transactional
public class V2AuthService {
    private final Logger logger = LoggerFactory.getLogger(V2AuthService.class.getName());
    private final V2MemberRepository v2MemberRepository;
    private final PasswordEncoder passwordEncoder;
    
    // 회원가입
    public Member signUp(SignupRequest request) {
        logger.info("회원가입");

        Member member = Member.builder()
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .nickname(request.nickname())
                .build();

        v2MemberRepository.save(member);

        return member;
    }

    // 로그인
}
