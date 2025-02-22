package com.patrol.domain.protection.animalCase.service;

import com.patrol.domain.protection.animalCase.entity.AnimalCase;
import com.patrol.domain.protection.animalCase.entity.CaseHistory;
import com.patrol.domain.protection.animalCase.enums.CaseHistoryStatus;
import com.patrol.domain.protection.animalCase.enums.ContentType;
import com.patrol.domain.protection.animalCase.repository.CaseHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CaseHistoryService {

  private final CaseHistoryRepository caseHistoryRepository;

  @Transactional
  public void addInitialLostPost(AnimalCase animalCase, ContentType contentType, Long contentId) {
    addHistory(animalCase, CaseHistoryStatus.INITIAL_MISSING_REPORT, contentType, contentId);
  }

  @Transactional
  public void addAdditionalLostPost(AnimalCase animalCase, ContentType contentType, Long contentId) {
    addHistory(animalCase, CaseHistoryStatus.ADDITIONAL_MISSING_REPORT, contentType, contentId);
  }

  @Transactional
  public void addFindPost(AnimalCase animalCase, ContentType contentType, Long contentId) {
    addHistory(animalCase, CaseHistoryStatus.FOUND_REPORT, contentType, contentId);
  }

  @Transactional
  public void addRescueFindPost(AnimalCase animalCase, ContentType contentType, Long contentId) {
    addHistory(animalCase, CaseHistoryStatus.RESCUE_REPORT, contentType, contentId);
  }


  private void addHistory(
      AnimalCase animalCase, CaseHistoryStatus historyStatus,
      ContentType contentType, Long contentId
  ) {
    CaseHistory history = createNewHistory(animalCase, historyStatus, contentType, contentId);
    animalCase.addHistory(history);
    caseHistoryRepository.save(history);
  }


  private CaseHistory createNewHistory(
      AnimalCase animalCase, CaseHistoryStatus historyStatus,
      ContentType contentType, Long contentId
  ) {
    return CaseHistory.builder()
        .animalCase(animalCase)
        .historyStatus(historyStatus)
        .contentType(contentType)
        .contentId(contentId)
        .build();
  }

}
