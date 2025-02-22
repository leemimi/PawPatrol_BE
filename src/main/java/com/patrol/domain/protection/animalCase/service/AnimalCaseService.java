package com.patrol.domain.protection.animalCase.service;

import com.patrol.domain.findPost.entity.FindPost;
import com.patrol.domain.findPost.repository.FindPostRepository;
import com.patrol.domain.protection.animal.repository.AnimalRepository;
import com.patrol.domain.protection.animalCase.entity.AnimalCase;
import com.patrol.domain.protection.animalCase.enums.CaseStatus;
import com.patrol.domain.protection.animalCase.enums.ContentType;
import com.patrol.domain.protection.animalCase.enums.TargetType;
import com.patrol.domain.protection.animalCase.events.PostCreatedEvent;
import com.patrol.domain.protection.animalCase.repository.AnimalCaseRepository;
import com.patrol.global.error.ErrorCode;
import com.patrol.global.exception.CustomException;
import com.patrol.global.exceptions.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AnimalCaseService {

  private final AnimalCaseRepository animalCaseRepository;
  private final CaseHistoryService caseHistoryService;
  private final FindPostRepository findPostRepository;
  private final AnimalRepository animalRepository;

  @Transactional
  public void handleLostPostEvent(PostCreatedEvent event) {
    handleLostPost(
        event.getTargetType(), event.getTargetId(), event.getContentType(), event.getContentId()
    );
  }

  @Transactional
  public void handleFindPostEvent(PostCreatedEvent event) {
    FindPost findPost = findPostRepository.findById(event.getContentId())
        .orElseThrow(() -> new CustomException(ErrorCode.ENTITY_NOT_FOUND));

    if (!findPost.isRescue()) {
      handleFindPost(
          event.getTargetType(), event.getTargetId(), event.getContentType(), event.getContentId()
      );

    } else {
      handleRescueFindPost(
          event.getTargetType(), event.getTargetId(), event.getContentType(), event.getContentId()
      );
    }
  }


  private void handleLostPost(
      TargetType targetType, Long targetId, ContentType contentType, Long contentId
  ) {
    AnimalCase animalCase = findByTarget(targetType, targetId);
    if (animalCase == null) {
      animalCase = createNewCase(CaseStatus.MISSING, targetType, targetId);
      validateLostCase(animalCase);
      animalCaseRepository.save(animalCase);
      caseHistoryService.addInitialLostPost(animalCase, contentType, contentId);

    } else {
      caseHistoryService.addAdditionalLostPost(animalCase, contentType, contentId);
    }
  }

  private void handleFindPost(
      TargetType targetType, Long targetId, ContentType contentType, Long contentId
  ) {
    AnimalCase animalCase = findByTarget(targetType, targetId);
    if (animalCase == null) {
      animalCase = createNewCase(CaseStatus.UNKNOWN, targetType, targetId);
      validateFoundCase(animalCase);
      animalCaseRepository.save(animalCase);
      caseHistoryService.addFindPost(animalCase, contentType, contentId);

    } else {
      caseHistoryService.addFindPost(animalCase, contentType, contentId);
    }
  }


  private void handleRescueFindPost(
      TargetType targetType, Long targetId, ContentType contentType, Long contentId
  ) {
    AnimalCase animalCase = findByTarget(targetType, targetId);
    if (animalCase == null) {
      animalCase = createNewCase(CaseStatus.RESCUE, targetType, targetId);
      validateFoundCase(animalCase);
      animalCaseRepository.save(animalCase);
      caseHistoryService.addRescueFindPost(animalCase, contentType, contentId);

    } else {
      animalCase.updateStatus(CaseStatus.RESCUE);
      caseHistoryService.addRescueFindPost(animalCase, contentType, contentId);
    }
  }

  private AnimalCase createNewCase(CaseStatus caseStatus, TargetType targetType, Long targetId) {
    return AnimalCase.builder()
        .status(caseStatus)
        .targetType(targetType)
        .targetId(targetId)
        .build();
  }

  public AnimalCase findByTarget(TargetType targetType, Long targetId) {
    return animalCaseRepository.findByTargetTypeAndTargetId(targetType, targetId);
  }

  private void validateLostCase(AnimalCase animalCase) {
    if (animalCase.getTargetType() == TargetType.MY_PET) {
//      myPetRepository.findById(animalCase.getTargetId())
//          .orElseThrow(() -> new CustomException(ErrorCode.ENTITY_NOT_FOUND));
    }

    if (animalCase.getTargetType() == TargetType.ANIMAL) {
      throw new CustomException(ErrorCode.INVALID_CASE);
    }
  }

  private void validateFoundCase(AnimalCase animalCase) {
    switch (animalCase.getTargetType()) {
      case MY_PET -> animalRepository.findById(animalCase.getTargetId())  // MyPet 미구현에 따라 미구현
          .orElseThrow(() -> new CustomException(ErrorCode.ENTITY_NOT_FOUND));
      case ANIMAL -> animalRepository.findById(animalCase.getTargetId())
          .orElseThrow(() -> new CustomException(ErrorCode.ENTITY_NOT_FOUND));
    }
  }

}
