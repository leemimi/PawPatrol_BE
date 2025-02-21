package com.patrol.api.protection.facility.controller;

import com.patrol.api.protection.facility.dto.FacilitiesResponse;
import com.patrol.domain.protection.facility.entity.Facility;
import com.patrol.domain.protection.facility.entity.Shelter;
import com.patrol.domain.protection.facility.service.FacilityService;
import com.patrol.domain.protection.facility.service.HospitalService;
import com.patrol.domain.protection.facility.service.ShelterService;
import com.patrol.global.globalDto.GlobalResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
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
