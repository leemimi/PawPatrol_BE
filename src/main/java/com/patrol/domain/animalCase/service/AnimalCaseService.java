package com.patrol.domain.animalCase.service;


import com.patrol.api.animalCase.dto.AnimalCaseDetailResponse;
import com.patrol.api.animalCase.dto.AnimalCaseListResponse;
import com.patrol.domain.animal.entity.Animal;
import com.patrol.domain.animalCase.entity.AnimalCase;
import com.patrol.domain.animalCase.entity.CaseHistory;
import com.patrol.domain.animalCase.enums.CaseHistoryStatus;
import com.patrol.domain.animalCase.enums.CaseStatus;
import com.patrol.domain.animalCase.repository.AnimalCaseRepository;
import com.patrol.domain.findPost.entity.FindPost;
import com.patrol.domain.findPost.repository.FindPostRepository;
import com.patrol.global.error.ErrorCode;
import com.patrol.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AnimalCaseService {

  private final AnimalCaseRepository animalCaseRepository;
  private final CaseHistoryService caseHistoryService;
  private final FindPostRepository findPostRepository;


  @Transactional
  public AnimalCase createNewCase(CaseStatus status, Animal animal) {
    AnimalCase animalCase = AnimalCase.builder()
        .status(status)
        .animal(animal)
        .build();
    return animalCaseRepository.save(animalCase);
  }


  @Transactional
  public void updateStatus(AnimalCase animalCase, CaseStatus status) {
    animalCase.updateStatus(status);
  }


  public AnimalCase findByAnimal(Animal animal) {
    return animalCaseRepository.findByAnimal(animal);
  }

  public Optional<AnimalCase> findByIdAndStatus(Long caseId, CaseStatus caseStatus) {
    return animalCaseRepository.findByIdAndStatus(caseId, caseStatus);
  }

  public AnimalCase findById(Long caseId) {
    return animalCaseRepository.findById(caseId)
        .orElseThrow(() -> new CustomException(ErrorCode.ENTITY_NOT_FOUND));
  }

  public AnimalCaseDetailResponse findByIdWithHistories(Long caseId) {
    AnimalCase animalCase = animalCaseRepository.findByIdWithHistories(caseId)
        .orElseThrow(() -> new CustomException(ErrorCode.ENTITY_NOT_FOUND));

    // TODO:
    // AnimalInfo animalInfo = getAnimalInfo(animalCase.getTargetType(), animalCase.getTargetId());
    // return AnimalCaseDetailResponse.of(animalCase, animalInfo);
    return AnimalCaseDetailResponse.of(animalCase);
  }

  public AnimalCaseDetailResponse findByIdAndStatusesWithHistories(
      Long caseId, Collection<CaseStatus> statuses
  ) {
    AnimalCase animalCase = animalCaseRepository.findByIdAndStatusesWithHistories(caseId, statuses)
        .orElseThrow(() -> new CustomException(ErrorCode.ENTITY_NOT_FOUND));
    return AnimalCaseDetailResponse.of(animalCase);
  }

  public Page<AnimalCaseListResponse> findAllByStatuses(Collection<CaseStatus> statuses, Pageable pageable) {
    return animalCaseRepository.findAllByStatusIn(statuses, pageable)
        .map(AnimalCaseListResponse::of);
  }


  public Page<AnimalCaseListResponse> findAll(Pageable pageable) {
    return animalCaseRepository.findAll(pageable)
        .map(AnimalCaseListResponse::of);
  }


  @Transactional
  public void updateToTempProtectWaiting(Long caseId, Long memberId) {
    AnimalCase animalCase = animalCaseRepository.findByIdAndStatus(caseId, CaseStatus.RESCUE)
        .orElseThrow(() -> new CustomException(ErrorCode.ENTITY_NOT_FOUND));

    CaseHistory rescueHistory = validateRescuePostOwner(animalCase.getId(), memberId);
    animalCase.updateStatus(CaseStatus.TEMP_PROTECT_WAITING);

    caseHistoryService.changeToTempProtectWaiting(animalCase, rescueHistory.getContentType(), rescueHistory.getContentId(), memberId);
  }


  public CaseHistory validateRescuePostOwner(Long caseId, Long memberId) {
    CaseHistory rescueHistory = caseHistoryService.findByAnimalCaseIdAndHistoryStatus(
        caseId, CaseHistoryStatus.RESCUE_REPORT
    );

    FindPost findPost = findPostRepository.findById(rescueHistory.getContentId())
        .orElseThrow(() -> new CustomException(ErrorCode.ENTITY_NOT_FOUND));

    if (!findPost.getMemberId().equals(memberId)) {
      throw new CustomException(ErrorCode.UNAUTHORIZED_ACCESS);
    }

    return rescueHistory;
  }

}
