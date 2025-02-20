package com.patrol.domain.protection.facility.service;

import com.patrol.domain.protection.facility.repository.HospitalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HospitalService {

  private final HospitalRepository hospitalRepository;
  private final CsvParser csvParser;




}
