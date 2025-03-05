package com.patrol.api.member.member.controller;

import com.patrol.api.member.member.dto.GetAllMembersResponse;
import com.patrol.api.member.member.dto.request.ChangeMemberStatusRequest;
import com.patrol.domain.member.auth.service.AdminService;
import com.patrol.domain.member.member.service.V2MemberService;
import com.patrol.global.globalDto.GlobalResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

/**
 * packageName    : com.patrol.api.member.member.controller
 * fileName       : ApiV1AdminController
 * author         : sungjun
 * date           : 2025-03-05
 * description    : 자동 주석 생성
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-03-05        kyd54       최초 생성
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/members/admin")
public class ApiV1AdminController {
    private final AdminService adminService;

    // 관리자 > 회원 목록 조회
    @GetMapping
    public GlobalResponse<Page<GetAllMembersResponse>> getAllMembers(Pageable pageable) {
        return GlobalResponse.success(adminService.getAllMembers(pageable));
    }

    // 관리자 > 회원 상태 변경
    @PatchMapping("/status")
    public GlobalResponse<Void> changeMemberStatus(
            @RequestBody ChangeMemberStatusRequest changeMemberStatusRequest) {
        adminService.changeMemberStatus(changeMemberStatusRequest);
        return GlobalResponse.success();
    }
}
