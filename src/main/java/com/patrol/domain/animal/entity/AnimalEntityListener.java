package com.patrol.domain.animal.entity;

import jakarta.persistence.PostUpdate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class AnimalEntityListener {
    private static final Logger log = LoggerFactory.getLogger(AnimalEntityListener.class);

    @PostUpdate
    public void afterUpdate(Animal animal) {
        if (animal.isLost()) {
            log.info("🚨 동물 실종 상태 변경: animalId={}, name={}", animal.getId(), animal.getName());
        }
    }
}
