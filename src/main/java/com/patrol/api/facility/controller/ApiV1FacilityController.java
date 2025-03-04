package com.patrol.api.facility.controller;

import com.patrol.api.facility.dto.FacilitiesResponse;
import com.patrol.api.facility.dto.ShelterListResponse;
import com.patrol.domain.facility.service.FacilityService;
import com.patrol.domain.facility.service.HospitalService;
import com.patrol.domain.facility.service.ShelterService;
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
  private final ShelterService shelterService;
  private final HospitalService hospitalService;


  @GetMapping
  @Operation(summary = "보호소/병원 목록")
  public RsData<List<FacilitiesResponse>> getFacilities() {
    List<FacilitiesResponse> facilitiesResponseList = facilityServices.stream()
        .flatMap(service -> service.findAll().stream())
        .collect(Collectors.toList());

    return new RsData<>("200", "보호소/병원 목록 가져오기 성공", facilitiesResponseList);
  }

  @GetMapping("/shelters")
  @Operation(summary = "보호소 목록")
  public RsData<List<ShelterListResponse>> getShelters() {
    List<ShelterListResponse> shelterResponseList = shelterService.findAllWithAnimals();
    return new RsData<>("200", "보호소 목록 가져오기 성공", shelterResponseList);
  }

  @GetMapping("/hospitals")
  @Operation(summary = "병원 목록")
  public RsData<List<FacilitiesResponse>> getHospitals() {
    List<FacilitiesResponse> hospitalResponseList = hospitalService.findAll();
    return new RsData<>("200", "병원 목록 가져오기 성공", hospitalResponseList);
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

  @GetMapping("/shelters/map")
  @Operation(summary = "반경 내의 모든 보호소 게시글 조회")
  public RsData<List<ShelterListResponse>> getSheltersWithinRadius(
      @RequestParam(name = "latitude") double latitude,
      @RequestParam(name = "longitude") double longitude,
      @RequestParam(name = "radius") double radius
  ) {
    List<ShelterListResponse> shelters = shelterService.getSheltersWithinRadius(latitude, longitude, radius);
    return new RsData<>("200", "반경 내의 보호소를 성공적으로 호출했습니다.", shelters);
  }

  @GetMapping("/hospitals/map")
  @Operation(summary = "반경 내의 병원만 조회")
  public RsData<List<FacilitiesResponse>> getHospitalsWithinRadius(
      @RequestParam(name = "latitude") double latitude,
      @RequestParam(name = "longitude") double longitude,
      @RequestParam(name = "radius") double radius) {
    List<FacilitiesResponse> hospitals = hospitalService.getFacilitiesWithinRadius(latitude, longitude, radius);
    return new RsData<>("200", "반경 내의 병원을 성공적으로 호출했습니다.", hospitals);
  }
}
