package com.patrol.domain.animalCase.service;


import com.patrol.api.animalCase.dto.AnimalCaseDetailDto;
import com.patrol.api.animalCase.dto.AnimalCaseListResponse;
import com.patrol.domain.animal.entity.Animal;
import com.patrol.domain.animalCase.entity.AnimalCase;
import com.patrol.domain.animalCase.enums.CaseStatus;
import com.patrol.domain.animalCase.repository.AnimalCaseRepository;
import com.patrol.domain.member.member.entity.Member;
import com.patrol.global.error.ErrorCode;
import com.patrol.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AnimalCaseService {

  private final AnimalCaseRepository animalCaseRepository;

  @Transactional
  public AnimalCase createNewCase(CaseStatus status, Animal animal) {
    AnimalCase animalCase = AnimalCase.builder()
        .status(status)
        .animal(animal)
        .build();
    return animalCaseRepository.save(animalCase);
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

  // 관리자용
  public AnimalCaseDetailDto findByIdWithHistories(Long caseId) {
    AnimalCase animalCase = animalCaseRepository.findByIdWithHistories(caseId)
        .orElseThrow(() -> new CustomException(ErrorCode.ENTITY_NOT_FOUND));
    return AnimalCaseDetailDto.of(animalCase);
  }

  public Optional<AnimalCase> findByIdAndStatusesWithHistories(Long caseId, Collection<CaseStatus> statuses) {
    return animalCaseRepository.findByIdAndStatusesWithHistories(caseId, statuses);
  }

  public Page<AnimalCaseListResponse> findAllByStatuses(Collection<CaseStatus> statuses, Pageable pageable) {
    return animalCaseRepository.findAllByStatusIn(statuses, pageable)
        .map(AnimalCaseListResponse::of);
  }

  // 관리자용
  public Page<AnimalCaseListResponse> findAll(Pageable pageable) {
    return animalCaseRepository.findAll(pageable)
        .map(AnimalCaseListResponse::of);
  }

  public Page<AnimalCase> findAllByCurrentFoster(Member currentFoster, Pageable pageable) {
    return animalCaseRepository.findAllByCurrentFoster(currentFoster, pageable);
  }

  public Page<AnimalCase> findAllByCurrentFosterAndStatus(
      Member currentFoster, Collection<CaseStatus> statuses, Pageable pageable
  ) {
    return animalCaseRepository.findAllByCurrentFosterAndStatusIn(currentFoster, statuses, pageable);
  }

  @Transactional
  public void softDeleteAnimalCase(AnimalCase animalCase, Long memberId) {
    if (!animalCase.getCurrentFoster().getId().equals(memberId)) {
      throw new CustomException(ErrorCode.UNAUTHORIZED_ACCESS);
    }
    animalCase.setDeletedAt(LocalDateTime.now());
  }

  @Transactional
  public void saveAll(List<AnimalCase> animalCases) {
    animalCaseRepository.saveAll(animalCases);
  }
}
