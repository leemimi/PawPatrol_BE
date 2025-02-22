package com.patrol.domain.protection.animalCase.repository;

import com.patrol.domain.protection.animalCase.entity.AnimalCase;
import com.patrol.domain.protection.animalCase.enums.TargetType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnimalCaseRepository extends JpaRepository<AnimalCase, Long> {
  AnimalCase findByTargetTypeAndTargetId(TargetType targetType, Long targetId);
}
