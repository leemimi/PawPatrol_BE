package com.patrol.domain.protection.animalCase.repository;

import com.patrol.domain.protection.animalCase.entity.AnimalCase;
import com.patrol.domain.protection.animalCase.enums.TargetType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AnimalCaseRepository extends JpaRepository<AnimalCase, Long> {
  AnimalCase findByTargetTypeAndTargetId(TargetType targetType, Long targetId);

  @Query("SELECT ac FROM AnimalCase ac LEFT JOIN FETCH ac.histories WHERE ac.id = :id")
  Optional<AnimalCase> findByIdWithHistories(@Param("id") Long id);
}
