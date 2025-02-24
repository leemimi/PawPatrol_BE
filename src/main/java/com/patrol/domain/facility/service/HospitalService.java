package com.patrol.domain.facility.service;


import com.patrol.api.facility.dto.FacilitiesResponse;
import com.patrol.domain.facility.entity.Hospital;
import com.patrol.domain.facility.repository.HospitalRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HospitalService implements FacilityService {

  private final HospitalRepository hospitalRepository;
  private final CsvParser csvParser;

  @Transactional
  public void loadData() {
    try {
      Resource resource = new ClassPathResource("data/hospitals.csv");
      List<CsvParser.HospitalData> hospitalDataList =
          csvParser.parseHospitals(resource.getInputStream());

      List<Hospital> hospitals = hospitalDataList.stream()
          .map(this::convertToEntity)
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


  private Hospital convertToEntity(CsvParser.HospitalData data) {
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
