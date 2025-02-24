package com.patrol.api.member.member.controller;

import com.patrol.api.member.member.dto.request.PetRegisterRequest;
import com.patrol.domain.member.member.entity.Member;
import com.patrol.domain.member.member.service.V2MemberService;
import com.patrol.global.globalDto.GlobalResponse;
import com.patrol.global.webMvc.LoginUser;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    // 마이페이지 > 반려동물 등록
    @PostMapping("/pets/register")
    public GlobalResponse<Void> petRegister(@LoginUser Member member,
                                            @RequestBody PetRegisterRequest petRegisterRequest) {

        v2MemberService.petRegister(member, petRegisterRequest);

        return GlobalResponse.success();
    }
}
