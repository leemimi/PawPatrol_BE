package com.patrol.api.member.auth.controller;

import com.patrol.api.member.auth.dto.request.SignupRequest;
import com.patrol.api.member.auth.dto.requestV2.LoginRequest;
import com.patrol.api.member.auth.dto.requestV2.LoginUserInfoResponse;
import com.patrol.domain.member.auth.service.V2AuthService;
import com.patrol.domain.member.member.entity.Member;
import com.patrol.domain.member.member.service.V2MemberService;
import com.patrol.global.globalDto.GlobalResponse;
import com.patrol.global.rq.Rq;
import com.patrol.global.webMvc.LoginUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

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
    private final Logger logger = LoggerFactory.getLogger(ApiV2AuthController.class.getName());
    private final V2AuthService v2AuthService;
    private final V2MemberService v2MemberService;
    private final Rq rq;

    // 회원가입
    @PostMapping("/sign-up")
    public GlobalResponse<String> signUp(@Valid @RequestBody SignupRequest request) {
        Member member = v2AuthService.signUp(request);
        return GlobalResponse.success(member.getNickname());
    }

    // 로그인
    @PostMapping("/login")
    public GlobalResponse<String> login(@Valid @RequestBody LoginRequest loginRequest) {
        Member member = v2MemberService.getMember(loginRequest.email());

        String accessToken = rq.makeAuthCookies(member);

        logger.info("로그인");
        return GlobalResponse.success(accessToken);
    }

    // 로그아웃
    @PostMapping("/logout")
    public GlobalResponse<String> logout() {
        rq.deleteCookie("accessToken");
        rq.deleteCookie("apiKey");
        logger.info("로그아웃");
        return GlobalResponse.success();
    }

    // 현재 로그인 사용자 정보 조회
    @GetMapping("/me")
    public GlobalResponse<LoginUserInfoResponse> loginUserInfo(@LoginUser Member member) {

        LoginUserInfoResponse userInfo = LoginUserInfoResponse.builder()
                .email(member.getEmail())
                .nickname(member.getNickname())
                .build();
        return GlobalResponse.success(userInfo);
    }
}
