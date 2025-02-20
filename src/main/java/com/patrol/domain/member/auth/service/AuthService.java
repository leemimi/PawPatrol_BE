package com.patrol.domain.member.auth.service;




import com.patrol.api.member.auth.dto.EmailResponse;
import com.patrol.api.member.auth.dto.FindEmailsResponse;
import com.patrol.domain.member.member.entity.Member;
import com.patrol.domain.member.member.enums.MemberRole;
import com.patrol.domain.member.member.enums.MemberStatus;
import com.patrol.domain.member.member.enums.ProviderType;
import com.patrol.domain.member.member.repository.MemberRepository;
import com.patrol.global.exceptions.ErrorCodes;
import com.patrol.global.exceptions.ServiceException;
import com.patrol.standard.util.Ut;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthTokenService authTokenService;
    private final OAuthService oAuthService;
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;


    @Transactional
    public Member signup(
        String email, String password, String nickname,
        ProviderType loginType, String providerId, String providerEmail, String profileImageUrl
    ) {
        _validateEmailAndPassword(email, password, loginType);

        Optional<Member> existingMember = memberRepository.findByEmail(email);
        if (existingMember.isPresent()) {
            throw new ServiceException(ErrorCodes.DUPLICATE_EMAIL);
        }

        Member member = Member.builder()
            .email(email)
            .password(
                loginType.equals(ProviderType.SELF) ? passwordEncoder.encode(password) : null
            )
            .nickname(nickname)
            .loginType(loginType)
            .profileImageUrl(profileImageUrl)
            .apiKey(UUID.randomUUID().toString())
            .role(MemberRole.ROLE_USER)
            .status(MemberStatus.ACTIVE)
            .build();

        if (providerId != null) {
            oAuthService.connectProvider(member, loginType, providerId, providerEmail);
        }

        return memberRepository.save(member);
    }


    public Optional<Member> findByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

    public Optional<Member> findByApiKey(String apiKey) {
        return memberRepository.findByApiKey(apiKey);
    }

    public String genAccessToken(Member member) {
        return authTokenService.genAccessToken(member);
    }

    public String genAuthToken(Member member) {
        return member.getApiKey() + " " + genAccessToken(member);
    }

    public Member getMemberFromAccessToken(String accessToken) {
        Map<String, Object> payload = authTokenService.payload(accessToken);
        if (payload == null) {
            return null;
        }
        long id = (long) payload.get("id");
        String email = (String) payload.get("email");
        String nickname = (String) payload.get("nickname");
        String profileImageUrl = (String) payload.get("profileImageUrl");
        String role = (String) payload.get("role");

        return new Member(id, email, nickname, profileImageUrl, MemberRole.valueOf(role));
    }


    public FindEmailsResponse findEmailsByPhoneNumber(String phoneNumber) {
        List<Member> members = memberRepository.findAllByPhoneNumber(phoneNumber);
        if (members.isEmpty()) {
            throw new ServiceException(ErrorCodes.INVALID_PHONE_NUMBER);
        }

        List<EmailResponse> emailResponses = members.stream()
            .map(member -> new EmailResponse(
                Ut.str.maskEmail(member.getEmail()),
                member.getCreatedAt()
            ))
            .toList();

        return new FindEmailsResponse(emailResponses);
    }

    
    @Transactional
    public Member handleSocialLogin(    // 소셜 로그인 시, 사이트 자체 계정의 유무에 따른 처리
        String email, String nickname,
        ProviderType loginType, String profilePath, String providerId
    ) {
        Member connectedMember = oAuthService.findByProviderId(loginType, providerId);
        if (connectedMember != null) {
            connectedMember.setLoginType(loginType);
            return connectedMember;  // 연결된 계정이 있다면 그 계정으로 로그인
        }

        Optional<Member> existingMember = findByEmail(email); // 이메일이 사용중인지 확인
        if (existingMember.isPresent()) {
            Member member = existingMember.get();
            oAuthService.connectProvider(member, loginType, providerId, email);  // 기존 계정에 소셜 계정 연결
            return member;
        }

        return signup(email, null, nickname, loginType, providerId, email, profilePath);
    }


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


    private void _validateEmailAndPassword(String email, String password, ProviderType loginType) {
        if (email == null || email.trim().isEmpty()) {
            throw new ServiceException(ErrorCodes.EMAIL_REQUIRED);
        }

        if (ProviderType.SELF.equals(loginType) && (password == null || password.trim().isEmpty())) {
            throw new ServiceException(ErrorCodes.PASSWORD_REQUIRED);
        }
    }

}
