package com.patrol.domain.member.auth.service;

import com.patrol.api.member.auth.dto.SocialTokenInfo;
import com.patrol.api.member.auth.dto.request.SignupRequest;
import com.patrol.api.member.auth.dto.requestV2.SocialConnectRequest;
import com.patrol.domain.member.member.entity.Member;
import com.patrol.domain.member.member.enums.ProviderType;
import com.patrol.domain.member.member.repository.V2MemberRepository;
import com.patrol.global.exceptions.ErrorCodes;
import com.patrol.global.exceptions.ServiceException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

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
    private final OAuthService oAuthService;
    private final PasswordEncoder passwordEncoder;
    private final AuthTokenService authTokenService;

    // 회원가입
    @Transactional
    public Member signUp(SignupRequest request) {
        logger.info("회원가입");

        Member member = Member.builder()
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .nickname(request.nickname())
                .apiKey(UUID.randomUUID().toString())
                .createdAt(LocalDateTime.now())
                .build();

        v2MemberRepository.save(member);

        return member;
    }

    @Transactional
    public Member handleSocialLogin(    // 소셜 로그인 시, 사이트 자체 계정의 유무에 따른 처리
                                        String email,
                                        ProviderType loginType, String providerId
    ) {
        Member connectedMember = oAuthService.findByProviderId(loginType, providerId);
        if (connectedMember != null) {
            connectedMember.setLoginType(loginType);
            return connectedMember;  // 연결된 계정이 있다면 그 계정으로 로그인
        }
        String tempToken = authTokenService.generateTempSocialToken(email, loginType, providerId);

        // 명확한 에러 메시지와 함께 토큰 전달
        throw new OAuth2AuthenticationException(
                new OAuth2Error("temp_token", tempToken, null)
        );
    }

    // 소셜 계정 연동
    @Transactional
    public void socialConnect(@Valid SocialConnectRequest socialConnectRequest,
                              String accessToken) {

        Map<String, Object> loginUser = authTokenService.payload(accessToken);
        SocialTokenInfo socialTokenInfo = authTokenService.parseSocialToken(socialConnectRequest.tempToken());

        Long userId = (Long)loginUser.get("id");

        // 멤버 ID로 가입된 유저 정보 찾기
        Member member = v2MemberRepository.findById(userId).orElseThrow();

        // 소셜 계정과 연동
        connectOAuthProvider(
                member,
                socialTokenInfo.getProviderType(),
                socialTokenInfo.getProviderId(),
                socialTokenInfo.getEmail()
        );
    }

    // 엑세스 토큰 발행
    public String genAccessToken(Member member) {
        return authTokenService.genAccessToken(member);
    }

    /**
     * 기존 계정(loginUser)에 소셜 계정 정보를 연동
     * @param loginUser 연동할 기존 계정
     * @param loginType 소셜 로그인 제공자 타입 (KAKAO, GOOGLE, NAVER)
     * @param providerId 소셜 계정의 고유 ID
     * @param providerEmail 소셜 계정의 이메일
     * @throws ServiceException 이미 다른 계정과 연동된 소셜 계정인 경우
     */
    @Transactional
    public void connectOAuthProvider(
            Member loginUser, ProviderType loginType, String providerId, String providerEmail
    ) {
        Member connectedMember = oAuthService.findByProviderId(loginType, providerId);
        if (connectedMember != null) {
            throw new ServiceException(ErrorCodes.SOCIAL_ACCOUNT_ALREADY_IN_USE);
        }
        oAuthService.connectProvider(loginUser, loginType, providerId, providerEmail);
    }
}
