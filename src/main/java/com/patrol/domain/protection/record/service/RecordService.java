package com.patrol.domain.protection.record.service;

import com.patrol.domain.protection.protection.repository.ProtectionRepository;
import com.patrol.domain.protection.record.repository.RecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecordService {

  private final RecordRepository recordRepository;

}
