package com.patrol.api.protection.animalCase.controller;

import com.patrol.api.protection.animalCase.dto.AnimalCaseListResponse;
import com.patrol.api.protection.animalCase.dto.AnimalCaseDetailResponse;
import com.patrol.domain.protection.animalCase.service.AnimalCaseService;
import com.patrol.global.globalDto.GlobalResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/animal-cases")
@Tag(name = "케이스 관리 API", description = "동물의 전체 기록 관리")
public class ApiV1AnimalCaseController {

  private final AnimalCaseService animalCaseService;


  @GetMapping
  @Operation(summary = "동물 케이스 목록")
  public GlobalResponse<Page<AnimalCaseListResponse>> getAnimalCases(
      @PageableDefault(size = 5) Pageable pageable
  ) {
    return GlobalResponse.success(animalCaseService.findAll(pageable));
  }


  @GetMapping("/{caseId}")
  @Operation(summary = "동물 케이스 상세조회")
  public GlobalResponse<AnimalCaseDetailResponse> getAnimalCase(@PathVariable Long caseId) {
    return GlobalResponse.success(animalCaseService.findById(caseId));
  }

}
