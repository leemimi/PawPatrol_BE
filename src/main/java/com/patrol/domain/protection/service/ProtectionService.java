package com.patrol.domain.protection.service;


import com.patrol.api.animalCase.dto.AnimalCaseDetailResponse;
import com.patrol.api.animalCase.dto.AnimalCaseListResponse;
import com.patrol.api.protection.dto.ProtectionResponse;
import com.patrol.domain.animalCase.entity.AnimalCase;
import com.patrol.domain.animalCase.enums.CaseStatus;
import com.patrol.domain.animalCase.service.AnimalCaseEventPublisher;
import com.patrol.domain.animalCase.service.AnimalCaseService;
import com.patrol.domain.member.member.entity.Member;
import com.patrol.domain.member.member.service.MemberService;
import com.patrol.domain.protection.entity.Protection;
import com.patrol.domain.protection.enums.ProtectionStatus;
import com.patrol.domain.protection.repository.ProtectionRepository;
import com.patrol.global.error.ErrorCode;
import com.patrol.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProtectionService {

  private final ProtectionRepository protectionRepository;
  private final AnimalCaseService animalCaseService;
  private final MemberService memberService;
  private final AnimalCaseEventPublisher animalCaseEventPublisher;


  public AnimalCaseDetailResponse findPossibleAnimalCase(Long caseId) {
    Collection<CaseStatus> possibleStatuses = List.of(
        CaseStatus.TEMP_PROTECT_WAITING,
        CaseStatus.TEMP_PROTECTING,
        CaseStatus.SHELTER_PROTECTING
    );
    return animalCaseService.findByIdAndStatusesWithHistories(caseId, possibleStatuses);
  }

  public Page<AnimalCaseListResponse> findPossibleAnimalCases(Pageable pageable) {
    return animalCaseService.findAllByStatuses(
        List.of(
            CaseStatus.TEMP_PROTECT_WAITING,
            CaseStatus.TEMP_PROTECTING,
            CaseStatus.SHELTER_PROTECTING
        ),
        pageable
    );
  }

  public Page<ProtectionResponse> findMyProtections(Long memberId, Pageable pageable) {
    return protectionRepository.findAllByApplicantId(memberId, pageable)
        .map(ProtectionResponse::of);
  }

  public Optional<Protection> findById(Long protectionId) {
    return protectionRepository.findByIdWithFetchAll(protectionId);
  }

  @Transactional
  public ProtectionResponse applyProtection(Long caseId, Long memberId, String reason) {
    Member applicant = memberService.findById(memberId)
        .orElseThrow(() -> new CustomException(ErrorCode.ENTITY_NOT_FOUND));

    AnimalCase animalCase = animalCaseService.findByIdAndStatus(caseId, CaseStatus.TEMP_PROTECT_WAITING)
        .orElseThrow(() -> new CustomException(ErrorCode.ENTITY_NOT_FOUND));

    if (animalCase.getCurrentFoster() == null) {
      throw new CustomException(ErrorCode.NOT_ASSIGNED_PROTECTION);
    }

    Protection protection = Protection.builder()
        .applicant(applicant)
        .animalCase(animalCase)
        .reason(reason)
        .protectionStatus(ProtectionStatus.PENDING)
        .build();


    protectionRepository.save(protection);
    return ProtectionResponse.of(protection);
  }



  @Transactional
  public void cancelProtection(Long protectionId, Long memberId) {
    Protection protection = protectionRepository.findById(protectionId)
        .orElseThrow(() -> new CustomException(ErrorCode.ENTITY_NOT_FOUND));

    if (!protection.getApplicant().getId().equals(memberId)) {
      throw new CustomException(ErrorCode.UNAUTHORIZED_ACCESS);
    }

    if (protection.getProtectionStatus() != ProtectionStatus.PENDING) {
      throw new CustomException(ErrorCode.INVALID_STATUS_CHANGE);
    }

    protection.cancel();
  }



  @Transactional
  public void acceptProtection(Long protectionId, Long memberId) {
    Protection protection = protectionRepository.findById(protectionId)
        .orElseThrow(() -> new CustomException(ErrorCode.ENTITY_NOT_FOUND));

    if (protection.getProtectionStatus() != ProtectionStatus.PENDING) {
      throw new CustomException(ErrorCode.INVALID_STATUS_CHANGE);
    }

    protection.approve();
    protection.getAnimalCase().updateStatus(CaseStatus.TEMP_PROTECTING);
    animalCaseEventPublisher.acceptProtection(protection.getId(), memberId);
  }


  @Transactional
  public void rejectProtection(Long protectionId, Long memberId, String rejectReason) {
    Protection protection = protectionRepository.findById(protectionId)
        .orElseThrow(() -> new CustomException(ErrorCode.ENTITY_NOT_FOUND));

    // 상태 검증
    if (protection.getProtectionStatus() != ProtectionStatus.PENDING) {
      throw new CustomException(ErrorCode.INVALID_STATUS_CHANGE);
    }

    // 케이스 상태 검증
    if (protection.getAnimalCase().getStatus() != CaseStatus.TEMP_PROTECT_WAITING) {
      throw new CustomException(ErrorCode.INVALID_STATUS_CHANGE);
    }

    // 권한 검증 - 구조글 작성자만 거절 가능
    animalCaseService.validateRescuePostOwner(protection.getAnimalCase().getId(), memberId);

    protection.reject(rejectReason);
    animalCaseEventPublisher.rejectProjection(protection.getId(), memberId);
  }

  public void createAnimalCase() {

  }
}
