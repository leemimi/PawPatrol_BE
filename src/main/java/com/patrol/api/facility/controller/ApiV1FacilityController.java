package com.patrol.api.facility.controller;

import com.patrol.api.facility.dto.FacilitiesResponse;
import com.patrol.api.lostFoundPost.dto.lostFoundPostResponseDto;
import com.patrol.domain.facility.service.FacilityService;
import com.patrol.global.globalDto.GlobalResponse;
import com.patrol.global.rsData.RsData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/facilities")
@Tag(name = "보호소/병원 시설 API", description = "시설(Facility)")
public class ApiV1FacilityController {

  private final List<FacilityService> facilityServices;

  @GetMapping
  @Operation(summary = "보호소/병원 목록")
  public GlobalResponse<List<FacilitiesResponse>> createPost() {
    List<FacilitiesResponse> facilitiesResponseList = facilityServices.stream()
        .flatMap(service -> service.findAll().stream())
        .collect(Collectors.toList());
    return GlobalResponse.success(facilitiesResponseList);
  }

  @GetMapping("/map")
  @Operation(summary = "반경 내의 모든 보호소/병원 게시글 조회")
  public RsData<List<FacilitiesResponse>> getAllPosts(
          @RequestParam(name = "latitude") double latitude,
          @RequestParam(name = "longitude") double longitude,
          @RequestParam(name = "radius") double radius) {
    List<FacilitiesResponse> posts = facilityServices.stream()
            .flatMap(service -> service.getFacilitiesWithinRadius(latitude, longitude, radius).stream())
            .collect(Collectors.toList());
    return new RsData<>("200", "반경 내의 제보 게시글을 성공적으로 호출했습니다.", posts);
  }
}
