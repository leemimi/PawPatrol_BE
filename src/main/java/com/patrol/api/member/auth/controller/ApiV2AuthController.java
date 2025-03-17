package com.patrol.api.member.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.patrol.api.member.auth.dto.BusinessValidationResponse;
import com.patrol.api.member.auth.dto.SearchShelterResponse;
import com.patrol.api.member.auth.dto.request.EmailRequest;
import com.patrol.api.member.auth.dto.request.EmailVerifyRequest;
import com.patrol.api.member.auth.dto.request.SignupRequest;
import com.patrol.api.member.auth.dto.requestV2.*;
import com.patrol.api.member.auth.dto.LoginUserInfoResponse;
import com.patrol.domain.facility.service.ShelterService;
import com.patrol.domain.member.auth.service.EmailService;
import com.patrol.domain.member.auth.service.V2AuthService;
import com.patrol.domain.member.member.entity.Member;
import com.patrol.domain.member.member.service.V2MemberService;
import com.patrol.domain.notification.service.FCMService;
import com.patrol.global.error.ErrorCode;
import com.patrol.global.exception.CustomException;
import com.patrol.global.exceptions.ErrorCodes;
import com.patrol.global.exceptions.ServiceException;
import com.patrol.global.globalDto.GlobalResponse;
import com.patrol.global.rq.Rq;
import com.patrol.global.webMvc.LoginUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * packageName    : com.patrol.api.member.auth.controller
 * fileName       : ApiV2AuthController
 * author         : sungjun
 * date           : 2025-02-19
 * description    : 자동 주석 생성
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-02-19        kyd54       최초 생성
 */
@RestController
@RequestMapping("/api/v2/auth")
@RequiredArgsConstructor
public class ApiV2AuthController {
    private final V2AuthService v2AuthService;
    private final V2MemberService v2MemberService;
    private final EmailService emailService;
    private final FCMService fcmService;
    private final Rq rq;
    private final PasswordEncoder passwordEncoder;
    private final ShelterService shelterService;

    // 회원가입
    @PostMapping("/sign-up")
    public GlobalResponse<String> signUp(@Valid @RequestBody SignupRequest request) {
        Member member = v2AuthService.signUp(request);
        return GlobalResponse.success(member.getNickname());
    }

    // 보호소 회원가입
    @PostMapping("/shelter/sigh-up")
    public GlobalResponse<String> shelterSignUp(@Valid @RequestBody ShelterSignupRequest request) {
        Member member = v2AuthService.shelterSignUp(request);
        return GlobalResponse.success(member.getNickname());
    }

    // 회원가입 - 이메일 인증 코드 발송
    @PostMapping("/email/verification-code")
    public GlobalResponse<Void> sendVerificationEmail(@Valid @RequestBody EmailRequest request) {

        if (!v2MemberService.validateNewEmail(request.email())) {
            emailService.sendVerificationEmail(request.email());
            return GlobalResponse.success();
        }
        return GlobalResponse.error(ErrorCode.DUPLICATE_EMAIL);
    }

    // 회원가입 - 이메일 인증 코드 확인
    @PostMapping("/email/verify")
    public GlobalResponse<Void> verifyEmail(@Valid @RequestBody EmailVerifyRequest request) {

        emailService.verifyCode(request.email(), request.code());

        return GlobalResponse.success();
    }

    @PostMapping("/login")
    public GlobalResponse<String> login(@Valid @RequestBody LoginRequest loginRequest) {
        fcmService.saveToken(loginRequest);
        return GlobalResponse.success(v2AuthService.login(loginRequest));
    }

    @PostMapping("/logout")
    public GlobalResponse<String> logout() {
        rq.deleteCookie("accessToken");
        rq.deleteCookie("apiKey");
        return GlobalResponse.success();
    }

    // 현재 로그인 사용자 정보 조회
    @GetMapping("/me")
    public GlobalResponse<LoginUserInfoResponse> loginUserInfo(@LoginUser Member member) {

        LoginUserInfoResponse userInfo = LoginUserInfoResponse.builder()
                .id(member.getId())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .profileImage(member.getProfileImageUrl())
                .role(member.getRole())
                .build();
        return GlobalResponse.success(userInfo);
    }

    // 소셜 계정 연동
    @PostMapping("/connect-social")
    public GlobalResponse<Void> socialConnect(
            @Valid @RequestBody SocialConnectRequest socialConnectRequest) {

            Member member = v2MemberService.getMember(socialConnectRequest.email());

            if (!passwordEncoder.matches(socialConnectRequest.password(), member.getPassword())) {
                throw new ServiceException(ErrorCodes.INVALID_PASSWORD);
            }

            String accessToken = rq.makeAuthCookies(member);

            if (accessToken != null) {
                v2AuthService.socialConnect(socialConnectRequest, accessToken);
                return GlobalResponse.success();
            }

            return GlobalResponse.error(ErrorCode.MEMBER_NOT_FOUND);
    }

    // 비밀번호 찾기 1단계 > 이메일 인증
    @PostMapping("/password/reset")
    public GlobalResponse<Map<String, String>> resetPassword(@Valid @RequestBody EmailRequest request) {
        if (v2MemberService.validateNewEmail(request.email())) {
            emailService.sendVerificationEmail(request.email());

            // 토큰 생성, 저장, 반환 (Redis에 저장하여 나중에 검증)
            Map<String, String> response = v2AuthService.resetToken(request.email());

            return GlobalResponse.success(response);
        } else {
            throw  new CustomException(ErrorCode.EMAIL_NOT_FOUND);
        }

    }

    // 비밀번호 찾기 2단계 > 이메일 인증 코드 확인
    @PostMapping("/password/reset/verify")
    public GlobalResponse<Map<String, String>> verifyResetCode(
            @Valid @RequestBody VerifyResetCodeRequest request) {

        boolean isValidToken = v2AuthService._validateContinuationToken(
                request.email(),
                request.continuationToken()
        );

        if (!isValidToken) {
            return GlobalResponse.error(ErrorCode.VERIFICATION_NOT_FOUND);
        }

        boolean isValidCode = emailService.verifyCode(
                request.email(),
                request.verificationCode()
        );

        if (!isValidCode) {
            return GlobalResponse.error(ErrorCode.VERIFICATION_NOT_FOUND);
        }

        // 토큰 생성, 저장, 반환 (Redis에 저장하여 나중에 검증)
        Map<String, String> response = v2AuthService.resetToken(request.email());

        return GlobalResponse.success(response);
    }

    // 비밀번호 찾기 3단계 > 새 비밀번호 등록 (끝)
    @PostMapping("/password/reset/new")
    public GlobalResponse<Void> setNewPassword(
            @Valid @RequestBody NewPasswordRequest request) {

        boolean isValidToken = v2AuthService._validateContinuationToken(
                request.email(),
                request.continuationToken()
        );

        if (!isValidToken) {
            return GlobalResponse.error(ErrorCode.VERIFICATION_NOT_FOUND);
        }

        v2AuthService.resetPassword(request);

        v2AuthService.deleteToken(request.email());

        return GlobalResponse.success();
    }

    // 사업자 등록번호 검증
    @PostMapping("/validate/business-number")
    public GlobalResponse<BusinessValidationResponse> validateBusinessNumber(
            @Valid @RequestBody BusinessNumberRequest request) throws Exception {
        String jsonResponse = v2AuthService.validateBusinessNumber(request);
        ObjectMapper objectMapper = new ObjectMapper();
        BusinessValidationResponse validationResponse = objectMapper.readValue(jsonResponse, BusinessValidationResponse.class);
        return GlobalResponse.success(validationResponse);
    }

    // 보호소 리스트 검색
    @GetMapping("/shelters")
    public GlobalResponse<List<SearchShelterResponse>> searchShelters(
            @RequestParam String keyword) {
        return GlobalResponse.success(shelterService.searchShelters(keyword));
    }
}
