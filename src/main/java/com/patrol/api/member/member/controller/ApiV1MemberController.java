//package com.patrol.api.member.member.controller;
//
//
//
//import com.patrol.api.member.auth.dto.request.PhoneNumberRequest;
//import com.patrol.api.member.auth.dto.request.PhoneVerificationRequest;
//import com.patrol.api.member.member.dto.MemberDto;
//import com.patrol.api.member.member.dto.request.AddPasswordRequest;
//import com.patrol.api.member.member.dto.request.ChangePasswordRequest;
//import com.patrol.api.member.member.dto.request.CheckPasswordRequest;
//import com.patrol.api.member.member.dto.request.MemberUpdateRequest;
//import com.patrol.domain.member.auth.service.OAuthService;
//import com.patrol.domain.member.auth.service.PhoneVerificationService;
//import com.patrol.domain.member.member.entity.Member;
//import com.patrol.domain.member.member.enums.ProviderType;
//import com.patrol.domain.member.member.service.MemberService;
//import com.patrol.domain.member.member.service.PasswordService;
//import com.patrol.domain.member.member.service.SocialConnectService;
//import com.patrol.global.exceptions.ErrorCodes;
//import com.patrol.global.exceptions.ServiceException;
//import com.patrol.global.rsData.RsData;
//import com.patrol.global.webMvc.LoginUser;
//import com.patrol.standard.base.Empty;
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.UUID;
//import java.util.concurrent.TimeUnit;
//
//@RestController
//@RequiredArgsConstructor
////@RequestMapping("/api/v1/members")
//public class ApiV1MemberController {
//
//    private final MemberService memberService;
//    private final PasswordService passwordService;
//    private final PhoneVerificationService phoneVerificationService;
//    private final PasswordEncoder passwordEncoder;
//    private final SocialConnectService socialConnectService;
//    private final RedisTemplate<String, String> redisTemplate;
//    private final OAuthService oAuthService;
//
//    private static final String PHONE_VERIFICATION_STATUS_PREFIX = "phoneNumber:verify:";
//    private static final int MAX_VERIFICATION_ATTEMPTS = 5;
//
//    public record VerifyResponse(String token) {}
//    public record VerifyTokenRequest(String token) {}
//
//    // [1. 회원 정보 관리]
//    // MEM01_MODIFY01 : 회원정보 수정 전 비밀번호 인증
////    @PostMapping("/me/password")
////    public RsData<VerifyResponse> checkPassword(
////        @Valid @RequestBody CheckPasswordRequest request,
////        @LoginUser Member loginUser
////    ) {
////        if (!passwordEncoder.matches(request.password(), loginUser.getPassword())) {
////            throw new ServiceException(ErrorCodes.INVALID_PASSWORD);
////        }
////
////        String token = UUID.randomUUID().toString();
////        redisTemplate.opsForValue().set(
////            "password-verify:" + loginUser.getEmail(),
////            token,
////            30,
////            TimeUnit.MINUTES
////        );
////
////        return new RsData<>("200-1", "비밀번호가 확인되었습니다.", new VerifyResponse(token));
////    }
//
//
//    // MEM01_MODIFY02 : 비밀번호 인증 정보 확인
////    @PostMapping("/me/password/validate")
////    public RsData<Void> validatePassword(
////        @LoginUser Member loginUser,
////        @RequestBody VerifyTokenRequest request
////    ) {
////        String storedToken = redisTemplate.opsForValue() .get("password-verify:" + loginUser.getEmail());
////
////        if (storedToken == null || !storedToken.equals(request.token())) {
////            throw new ServiceException(ErrorCodes.PASSWORD_VERIFICATION_REQUIRED);
////        }
////
////        redisTemplate.expire("password-verify:" + loginUser.getEmail(), 30, TimeUnit.MINUTES);
////
////        return new RsData<>("200-1", "비밀번호 확인이 유효합니다.");
////    }
//
//
//
//
//    // MEM01_MODIFY03 - 비밀번호 변경
////    @PatchMapping("/me/password")
////    public RsData<Void> changePassword(
////        @Valid @RequestBody ChangePasswordRequest request,
////        @LoginUser Member loginUser
////    ) {
////        passwordService.changePassword(
////            loginUser.getEmail(),
////            request.currentPassword(),
////            request.newPassword()
////        );
////        return new RsData<>("200-1", "비밀번호가 변경되었습니다.");
////    }
//
//
//    // MEM01_MODIFY04 : 회원정보 수정  (성별, 전화번호, 주소, 마케팅 수신여부 ...)
////    @PutMapping("/me/profile")
////    public RsData<MemberDto> updateMember(
////        @LoginUser Member loginUser,
////        @Valid @RequestBody MemberUpdateRequest memberUpdateRequest
////    ) {
////        String phoneNumber = memberUpdateRequest.getPhoneNumber();
////        if (phoneNumber != null && !phoneNumber.trim().isEmpty()) {
////            String normalizedPhoneNumber = phoneNumber.replaceAll("-", "");
////            String verified = redisTemplate.opsForValue().get(PHONE_VERIFICATION_STATUS_PREFIX + normalizedPhoneNumber);
////            if (verified == null) {
////                throw new ServiceException(ErrorCodes.PHONE_NUMBER_NOT_VERIFIED);
////            }
////        }
////
////        Member modifiedMember = memberService.updateInfo(loginUser, memberUpdateRequest);
////        return new RsData<>(
////            "200",
////            "회원 정보 업데이트가 성공하였습니다.",
////            new MemberDto(modifiedMember)
////        );
////    }
//
//
//    // MEM01_MODIFY05 : 전화번호 인증코드 발송 (SMS 인증)
////    @PostMapping("/me/phone/verification-code")
////    public RsData<Void> sendVerificationCode(@Valid @RequestBody PhoneNumberRequest request) {
////        String verificationKey = PHONE_VERIFICATION_STATUS_PREFIX + request.phoneNumber();
////        String verified = redisTemplate.opsForValue().get(verificationKey);
////        if (verified != null) {
////            throw new ServiceException(ErrorCodes.ALREADY_VERIFIED_PHONE_NUMBER);
////        }
////
////        phoneVerificationService.sendVerificationCode(request.phoneNumber());
////        return new RsData<>("200", "입력하신 번호로 인증 코드 발송이 성공하였습니다.");
////    }
////
////    // MEM01_MODIFY06 : 전화번호 인증코드 확인 (SMS 인증)
////    @PostMapping("/me/phone/verify")
////    public RsData<Void> verifyPhoneNumber(@Valid @RequestBody PhoneVerificationRequest request) {
////        if (!phoneVerificationService.verifyCode(request.phoneNumber(), request.code())) {
////            throw new ServiceException(ErrorCodes.INVALID_SNS_VERIFICATION_CODE);
////        }
////
////        redisTemplate.opsForValue().set(
////            PHONE_VERIFICATION_STATUS_PREFIX + request.phoneNumber(),
////            "verified",
////            MAX_VERIFICATION_ATTEMPTS,
////            TimeUnit.MINUTES
////        );
////        return new RsData<>("200", "전화번호 인증이 성공하였습니다.");
////    }
//
//
//    // [3. 소셜 계정 연동 관리]
//    // MEM03_SOCIAL01 : 소셜전용 계정 비번 추가
////    @PostMapping("/me/social/password")
////    public RsData<Void> addPasswordToSocialAccount(
////        @Valid @RequestBody AddPasswordRequest request,
////        @LoginUser Member loginUser
////    ) {
////        if (loginUser.getPassword() != null) {
////            throw new ServiceException(ErrorCodes.ALREADY_HAS_PASSWORD);
////        }
////
////        memberService.addPassword(loginUser.getId(), request.password());
////        return new RsData<>("200", "소셜 계정에 비밀번호가 추가되었습니다.");
////    }
//
//
//    // MEM03_SOCIAL02 : 소셜 계정 연동 가능 여부 검증 후 로그인 유저 정보 저장 => 프론트에서 확인 후 처리
////    @GetMapping("/me/social/{provider}/validate")
////    public RsData<Void> validateSocialConnection(
////        @PathVariable String provider, @LoginUser Member loginUser
////    ) {
////        ProviderType type = ProviderType.of(provider);
////        if (loginUser.hasOAuthProvider(type)) {   // 이미 해당 소셜 계정으로 연동된 경우
////            throw new ServiceException(ErrorCodes.ALREADY_CONNECTED_SOCIAL_ACCOUNT);
////        }
////
////        socialConnectService.storeOrigin(loginUser.getId());
////        return new RsData<>("200", "%s 소셜 계정 연동이 가능합니다.".formatted(provider));
////    }
//
//
//    // MEM03_SOCIAL03 : 소셜 계정 연동 해제
////    @DeleteMapping("/me/social/{provider}")
////    public RsData<Void> disconnectSocialAccount(
////        @PathVariable String provider, @LoginUser Member loginUser
////    ) {
////        ProviderType type = ProviderType.of(provider);
////        if (!loginUser.hasOAuthProvider(type)) {   // 연동되지 않은 소셜 계정인 경우
////            throw new ServiceException(ErrorCodes.NOT_CONNECTED_SOCIAL_ACCOUNT);
////        }
////
////        if (loginUser.getPassword() == null && loginUser.getConnectedOAuthCount() == 1) {  // 마지막 로그인 수단인 경우
////            throw new ServiceException(ErrorCodes.CANNOT_DISCONNECT_LAST_LOGIN_METHOD);
////        }
////
////        oAuthService.disconnectProvider(loginUser, type);
////        return new RsData<>("200", "%s 소셜 계정 연동을 해제하였습니다.".formatted(type.name()));
////    }
//
//
//
//
//    // [4. 회원 상태 관리]
//    // MEM04_DELETE : 회원 탈퇴
////    @PatchMapping("/me/deactivate")
////    public RsData<Void> deactivateMember(@LoginUser Member loginUser) {
////        memberService.deactivateMember(loginUser.getId());
////        return new RsData<>("200", "회원 탈퇴가 성공하였습니다.");
////    }
//
//
//
////    // [5. 관리자 회원 관리]
////    // ADMIN01_MEMBER01 : 회원 목록 (관리자)
////    @GetMapping
////    public List<MemberDto> getMembers() {
////        List<Member> members = memberService.findAll();
////        return members.stream().map(MemberDto::new).toList();
////    }
////
////
////    // ADMIN01_MEMBER02 : 회원 상세 조회 (관리자)
////    @GetMapping("/{memberId}")
////    public MemberDto getMember(@PathVariable Long memberId) {
////        Member member = memberService.findById(memberId).orElse(null);
////        return new MemberDto(member);
////    }
////
////
////    // ADMIN01_MEMBER03 : 회원 탈퇴 복구 (관리자)
////    @PatchMapping("/{memberId}/restore")
////    public RsData<Void> restoreMember(@PathVariable Long memberId) {
////        memberService.restoreMember(memberId);
////        return new RsData<>("200", "회원 복구가 성공하였습니다.");
////    }
////
////
////    // ADMIN01_MEMBER04 : 회원 차단 처리 (관리자)
////    @PatchMapping("/{memberId}/block")
////    @PreAuthorize("hasRole('ROLE_ADMIN')")
////    public RsData<Empty> banMember(@PathVariable Long memberId) {
////        memberService.banMember(memberId);
////        return new RsData("200-1", "%d번 회원을 차단했습니다.".formatted(memberId));
////    }
//
//}
