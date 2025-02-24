package com.patrol.domain.animalCase.repository;

import com.patrol.domain.animal.entity.Animal;
import com.patrol.domain.animalCase.entity.AnimalCase;
import com.patrol.domain.animalCase.enums.CaseStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.Optional;

public interface AnimalCaseRepository extends JpaRepository<AnimalCase, Long> {
  AnimalCase findByAnimal(Animal animal);
  Optional<AnimalCase> findByIdAndStatus(Long caseId, CaseStatus caseStatus);

  @Query("SELECT ac FROM AnimalCase ac LEFT JOIN FETCH ac.histories WHERE ac.id = :id")
  Optional<AnimalCase> findByIdWithHistories(@Param("id") Long id);

  @Query("SELECT ac FROM AnimalCase ac " +
      "LEFT JOIN FETCH ac.histories " +
      "WHERE ac.id = :id AND ac.status IN :statuses")
  Optional<AnimalCase> findByIdAndStatusesWithHistories(
      @Param("id") Long id,
      @Param("statuses") Collection<CaseStatus> statuses
  );

  Page<AnimalCase> findAllByStatusIn(Collection<CaseStatus> statuses, Pageable pageable);


}
