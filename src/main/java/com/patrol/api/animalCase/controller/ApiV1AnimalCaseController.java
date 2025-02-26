package com.patrol.api.animalCase.controller;

import com.patrol.api.animalCase.dto.AnimalCaseDetailResponse;
import com.patrol.api.animalCase.dto.AnimalCaseListResponse;
import com.patrol.domain.animalCase.service.AnimalCaseService;
import com.patrol.domain.member.member.entity.Member;
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
@RequestMapping("/api/v1/animal-cases")
@Tag(name = "동물 케이스 관리 API", description = "동물 상태 변화 기록 추적")
public class ApiV1AnimalCaseController {

  private final AnimalCaseService animalCaseService;


  @GetMapping
  @Operation(summary = "동물 케이스 목록 (전체이용)")
  public RsData<Page<AnimalCaseListResponse>> getAnimalCases(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size
  ) {
    Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
    Page<AnimalCaseListResponse> responses = animalCaseService.findAll(pageable);
    return new RsData<>("200", "모든 케이스를 성공적으로 호출했습니다.", responses);
  }


  @GetMapping("/{caseId}")
  @Operation(summary = "동물 케이스 상세조회 (전체이용)")
  public RsData<AnimalCaseDetailResponse> getAnimalCase(@PathVariable Long caseId) {
    AnimalCaseDetailResponse response = animalCaseService.findByIdWithHistories(caseId);
    return new RsData<>("200", "%d번 케이스를 성공적으로 호출했습니다.".formatted(caseId), response);
  }
}
