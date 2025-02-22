package com.patrol.domain.protection.animalCase.events;

import com.patrol.domain.findPost.entity.FindPost;
import com.patrol.domain.findPost.repository.FindPostRepository;
import com.patrol.domain.protection.animalCase.service.AnimalCaseService;
import com.patrol.domain.protection.animalCase.service.CaseHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class AnimalCaseEventHandler {

  private final AnimalCaseService animalCaseService;

  @EventListener
  public void handlePostCreated(PostCreatedEvent event) {
    switch (event.getContentType()) {
      case LOSTPOST -> animalCaseService.handleLostPostEvent(event);
      case FINDPOST -> animalCaseService.handleFindPostEvent(event);
      case PROTECTION -> {} // 추후 구현
    }
  }
}
