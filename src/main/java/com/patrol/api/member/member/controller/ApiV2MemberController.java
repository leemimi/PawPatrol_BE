package com.patrol.api.member.member.controller;

import com.patrol.api.animal.dto.MyPetListResponse;
import com.patrol.api.animal.dto.request.DeleteMyPetInfoRequest;
import com.patrol.api.animal.dto.request.ModiPetInfoRequest;
import com.patrol.api.member.auth.dto.ModifyProfileResponse;
import com.patrol.api.member.auth.dto.MyPostsResponse;
import com.patrol.api.member.auth.dto.requestV2.ModifyProfileRequest;
import com.patrol.api.member.member.dto.OAuthConnectInfoResponse;
import com.patrol.api.member.member.dto.request.PetRegisterRequest;
import com.patrol.domain.animal.service.AnimalService;
import com.patrol.domain.member.auth.service.OAuthService;
import com.patrol.domain.member.member.entity.Member;
import com.patrol.domain.member.member.enums.ProviderType;
import com.patrol.domain.member.member.service.V2MemberService;
import com.patrol.global.globalDto.GlobalResponse;
import com.patrol.global.webMvc.LoginUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;


/**
 * packageName    : com.patrol.api.member.member.controller
 * fileName       : ApiV2MemberController
 * author         : sungjun
 * date           : 2025-02-24
 * description    : 자동 주석 생성
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-02-24        kyd54       최초 생성
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/members")
public class ApiV2MemberController {
    private final V2MemberService v2MemberService;
    private final AnimalService animalService;
    private final OAuthService oAuthService;

    // 마이페이지 > 프로필 이미지 수정
    @PatchMapping("/profile/pic")
    public GlobalResponse<ModifyProfileResponse> modifyProfileImage(
            @LoginUser Member member,
            @ModelAttribute ModifyProfileRequest modifyProfileRequest) {
        return GlobalResponse.success(v2MemberService.modifyProfileImage(member, modifyProfileRequest));
    }

    // 마이페이지 > 회원정보 수정
    @PatchMapping("/profile")
    public GlobalResponse<Void> modifyProfile(
            @LoginUser Member member,
            @RequestBody  ModifyProfileRequest modifyProfileRequest) {
        v2MemberService.modifyProfile(member, modifyProfileRequest);
        return GlobalResponse.success();
    }

    // 마이페이지 > 프로필 이미지 삭제
    @PatchMapping("/profile/images")
    public GlobalResponse<Void> resetProfileImage(
            @LoginUser Member member,
            @ModelAttribute  ModifyProfileRequest modifyProfileRequest) {
        v2MemberService.resetProfileImage(member, modifyProfileRequest);
        return GlobalResponse.success();
    }

    // 마이페이지 > 반려동물 등록
    @PostMapping("/pets")
    public GlobalResponse<Void> petRegister(@LoginUser Member member,
                                            @ModelAttribute PetRegisterRequest petRegisterRequest) {

        animalService.myPetRegister(member, petRegisterRequest);

        return GlobalResponse.success();
    }

    // 마이페이지 > 내 반려동물 리스트
    @GetMapping("/pets")
    public GlobalResponse<Page<MyPetListResponse>> myPetList(
            @LoginUser Member member,
            Pageable pageable) {

        Page<MyPetListResponse> list = animalService.myPetList(member, pageable);

        return GlobalResponse.success(list);
    }
    
    // 마이페이지 > 내 반려동물 정보 수정
    @PatchMapping("/pets")
    public GlobalResponse<Void> modifyMyPetInfo(
            @LoginUser Member member,
            @ModelAttribute ModiPetInfoRequest modiPetInfoRequest) {

        animalService.modifyMyPetInfo(member, modiPetInfoRequest);
        return GlobalResponse.success();
    }

    // 마이페이지 > 내 반려동물 정보 삭제
    @DeleteMapping("/pets/{petId}")
    public GlobalResponse<Void> deleteMyPetInfo(
            @LoginUser Member member,
            @PathVariable Long petId) {

        animalService.deleteMyPetInfo(member, petId);
        return GlobalResponse.success();
    }

    // 마이페이지 나의 신고글 리스트 불러오기
    @GetMapping("/posts/reports")
    public GlobalResponse<Page<MyPostsResponse>> myReportPosts(
            @LoginUser Member member,
            @PageableDefault(size = 5) Pageable pageable) {
        return GlobalResponse.success(v2MemberService.myReportPosts(member, pageable));
    }
    
    // 마이페이지 나의 제보글 리스트 불러오기
    @GetMapping("/posts/witnesses")
    public GlobalResponse<Page<MyPostsResponse>> myWitnessPosts(
            @LoginUser Member member,
            @PageableDefault(size = 5) Pageable pageable) {
        return GlobalResponse.success(v2MemberService.myWitnessPosts(member, pageable));
    }
    
    // 소셜 로그인 연결 상태 불러오기
    @GetMapping("/social")
    public GlobalResponse<OAuthConnectInfoResponse> socialInfo(@LoginUser Member member) {
        return GlobalResponse.success(v2MemberService.socialInfo(member));
    }

    // 소셜 로그인 연결 해제
    @DeleteMapping("/social/{provider}")
    public GlobalResponse<Void> socialDisconnect(
            @LoginUser Member member,
            @PathVariable String provider
            ) {
        ProviderType type = ProviderType.of(provider);
        oAuthService.disconnectProvider(member, type);
        return GlobalResponse.success();
    }

    // 회원 탈퇴
    @PatchMapping("/withdraw")
    public GlobalResponse<Void> memberWithdraw(@LoginUser Member member) {
        v2MemberService.memberWithdraw(member);
        return GlobalResponse.success();
    }
}
