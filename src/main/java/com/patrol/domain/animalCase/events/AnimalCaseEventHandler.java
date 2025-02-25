package com.patrol.domain.animalCase.events;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class AnimalCaseEventHandler {

  private final AnimalCaseEventManager animalCaseEventManager;

  @EventListener
  public void handlePostCreated(PostCreatedEvent event) {
    switch (event.getContentType()) {
      case LOSTPOST ->
          animalCaseEventManager.handleLostPost(
              event.getAnimalId(), event.getContentType(),
              event.getLostFoundPostId(), event.getMemberId()
          );

      case FINDPOST ->
          animalCaseEventManager.handleFindPost(
              event.getAnimalId(), event.getContentType(),
              event.getLostFoundPostId(), event.getMemberId()
          );
    }
  }

  @EventListener
  public void handleProtectionStatusChange(ProtectionStatusChangeEvent event) {
        animalCaseEventManager.handleProtectionStatusChange(
            event.getProtectionId(),
            event.getMemberId(),
            event.getToStatus(),
            event.getHistoryStatus()
        );
  }

  @EventListener
  public void handleAnimalCaseCreated(AnimalCaseCreatedEvent event) {
    animalCaseEventManager.handleAnimalCaseCreated(event);
  }

}
