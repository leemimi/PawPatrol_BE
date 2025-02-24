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
      case LOSTPOST -> animalCaseEventManager.handleLostPostEvent(event);
      case FINDPOST -> animalCaseEventManager.handleFindPostEvent(event);
    }
  }


  @EventListener
  public void handleRescueToWaiting(RescueToWaitingEvent event) {
//    animalCaseEventManager.updateStatus(
//        event.getCaseId(),
//        event.getMemberId(),
//        event.getToStatus(),
//        event.getHistoryStatus()
//    );
  }

  @EventListener
  public void handleProtectionStatusChange(ProtectionStatusChangeEvent event) {
        animalCaseEventManager.updateStatus(
            event.getProtectionId(),
            event.getMemberId(),
            event.getToStatus(),
            event.getHistoryStatus()
        );
  }

}
