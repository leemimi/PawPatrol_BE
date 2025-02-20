package com.patrol.domain.protection.facility.service;

import com.patrol.api.protection.facility.dto.FacilitiesResponse;
import com.patrol.domain.protection.facility.entity.Facility;
import com.patrol.domain.protection.facility.entity.Hospital;
import com.patrol.domain.protection.facility.repository.HospitalRepository;
import com.patrol.domain.protection.facility.repository.ShelterRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HospitalService implements FacilityService  {

  private final HospitalRepository hospitalRepository;
  private final CsvParser csvParser;
  private final ShelterService shelterService;
  private final ShelterRepository shelterRepository;

  @PostConstruct
  @Transactional
  public void loadData() {
    try {
      Resource resource = new ClassPathResource("data/hospitals.csv");
      List<CsvParser.HospitalData> hospitalDataList =
          csvParser.parseHospitals(resource.getInputStream());

      List<Hospital> hospitals = hospitalDataList.stream()
          .map(this::_convertToEntity)
          .collect(Collectors.toList());

      hospitalRepository.saveAll(hospitals);
      log.info("병원 데이터 {}개 로드 완료", hospitals.size());

    } catch (Exception e) {
      log.error("병원 데이터 로드 중 에러 발생", e);
    }
  }

  public List<FacilitiesResponse> findAll() {
    return hospitalRepository.findAll().stream()
        .map(FacilitiesResponse::of)
        .collect(Collectors.toList());
  }


  private Hospital _convertToEntity(CsvParser.HospitalData data) {
    log.info(data.getTel());

    return Hospital.builder()
        .name(data.getName())
        .address(data.getAddress())
        .tel(data.getTel())
        .latitude(data.getLatitude())
        .longitude(data.getLongitude())
        .operatingHours(data.getOperatingHours())
        .homepage(data.getHomepage())
        .build();
  }
}
