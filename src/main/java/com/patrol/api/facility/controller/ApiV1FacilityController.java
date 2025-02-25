package com.patrol.api.facility.controller;

import com.patrol.api.facility.dto.FacilitiesResponse;
import com.patrol.domain.facility.service.FacilityService;
import com.patrol.global.globalDto.GlobalResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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

}
