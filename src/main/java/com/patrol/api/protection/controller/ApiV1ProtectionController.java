package com.patrol.api.protection.controller;


import com.patrol.api.animalCase.dto.AnimalCaseDetailDto;
import com.patrol.api.animalCase.dto.AnimalCaseListResponse;
import com.patrol.api.protection.dto.*;
import com.patrol.domain.member.member.entity.Member;
import com.patrol.domain.protection.service.ProtectionService;
import com.patrol.global.rsData.RsData;
import com.patrol.global.webMvc.LoginUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/protections")
@Tag(name = "임시보호/입양 관리 API", description = "임시보호/입양 목록에서 신청, 조회 등")
public class ApiV1ProtectionController {

  private final ProtectionService protectionService;

  @GetMapping
  @Operation(summary = "임시보호/입양 대기 중인 동물 목록")
  public RsData<Page<AnimalCaseListResponse>> getPossibleProtections(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size
  ) {
    Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
    Page<AnimalCaseListResponse> response = protectionService.findPossibleAnimalCases(pageable);
    return new RsData<>("200", "임시 보호 대기 중인 동물 목록 조회 성공", response);
  }


  @GetMapping("/{caseId}")
  @Operation(summary = "임시보호/입양 대기 중인 동물 상세 조회")
  public RsData<AnimalCaseDetailResponse> getPossibleAnimalCase(
      @PathVariable Long caseId, @LoginUser Member loginUser
  ) {
    AnimalCaseDetailResponse response = protectionService.findPossibleAnimalCase(caseId, loginUser.getId());
    return new RsData<>("200", "임시 보호 대기 중인 동물 상세 조회 성공", response);
  }


  @PostMapping
  @Operation(summary = "임시 보호 동물 등록하기")
  public RsData<Void> createAnimalCase(
      @RequestBody CreateAnimalCaseRequest request, @LoginUser Member loginUser
  ) {
    protectionService.createAnimalCase(request, loginUser);
    return new RsData<>("200", "임시 보호 동물 등록 성공");
  }


  @GetMapping("/my-cases")
  @Operation(summary = "내가 등록한 임시보호/입양 동물 목록")
  public RsData<Page<MyAnimalCaseResponse>> getMyAnimalCases(
      @LoginUser Member loginUser,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size
  ) {
    Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
    Page<MyAnimalCaseResponse> response = protectionService.findMyAnimalCases(loginUser, pageable);
    return new RsData<>("200", "내가 등록한 임시 보호 동물 목록 조회 성공", response);
  }


  @GetMapping("/my-protections")
  @Operation(summary = "내가 신청한 임시보호/입양 신청 목록")
  public RsData<Page<ProtectionResponse>> getMyProtections(
      @LoginUser Member loginUser,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size
  ) {
    Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
    Page<ProtectionResponse> response = protectionService.findMyProtections(loginUser.getId(), pageable);
    return new RsData<>("200", "내가 신청한 임시 보호 신청 목록 조회 성공", response);
  }


  @PostMapping("/{caseId}/apply")
  @Operation(summary = "임시 보호 신청하기")
  public RsData<ProtectionResponse> applyProtection(
      @PathVariable Long caseId, @RequestBody ProtectionRequest request, @LoginUser Member loginUser
  ) {
    ProtectionResponse
        response = protectionService.applyProtection(caseId, loginUser.getId(), request.reason(), request.protectionType());
    return new RsData<>("200", "임시 보호 신청하기 성공", response);
  }


  @DeleteMapping("/{protectionId}")
  @Operation(summary = "임시 보호 신청 취소")
  public RsData<Void> cancelProtection(
      @PathVariable Long protectionId, @LoginUser Member loginUser
  ) {
    protectionService.cancelProtection(protectionId, loginUser.getId());
    return new RsData<>("200", "임시 보호 신청 취소 완료");
  }


  @PatchMapping("/{protectionId}/accept")
  @Operation(summary = "임시 보호 신청 수락")
  public RsData<Void> acceptProtection(
      @PathVariable Long protectionId, @LoginUser Member loginUser
  ) {
    protectionService.acceptProtection(protectionId, loginUser.getId());
    return new RsData<>("200", "임시 보호 신청 수락 완료");
  }


  @PatchMapping("/{protectionId}/reject")
  @Operation(summary = "임시 보호 신청 거절")
  public RsData<Void> rejectProtection(
      @PathVariable Long protectionId, @LoginUser Member loginUser,
      @RequestBody RejectApplicationRequest request
  ) {
    protectionService.rejectProtection(protectionId, loginUser.getId(), request.rejectReason());
    return new RsData<>("200", "임시 보호 신청 거절 완료");
  }

}
