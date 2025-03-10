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
      case LOSTPOST -> animalCaseEventManager.handleLostPost(event);
      case FINDPOST -> animalCaseEventManager.handleFindPost(event);
    }
  }

  @EventListener
  public void handleProtectionStatusChange(ProtectionStatusChangeEvent event) {
        animalCaseEventManager.handleProtectionStatusChange(event);
  }

  @EventListener
  public void handleAnimalCaseCreated(AnimalCaseCreatedEvent event) {
    animalCaseEventManager.handleAnimalCaseCreated(event);
  }
}
