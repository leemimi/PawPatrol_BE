package com.patrol.domain.animalCase.repository;

import com.patrol.domain.animal.entity.Animal;
import com.patrol.domain.animalCase.entity.AnimalCase;
import com.patrol.domain.animalCase.enums.CaseStatus;
import com.patrol.domain.member.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.Optional;

public interface AnimalCaseRepository extends JpaRepository<AnimalCase, Long> {
  @Query("SELECT ac FROM AnimalCase ac LEFT JOIN FETCH ac.animal WHERE ac.animal = :animal")
  AnimalCase findByAnimal(@Param("animal") Animal animal);

  @Query("SELECT ac FROM AnimalCase ac " +
      "LEFT JOIN FETCH ac.animal " +
      "LEFT JOIN FETCH ac.currentFoster " +
      "WHERE ac.id = :id AND ac.status = :status")
  Optional<AnimalCase> findByIdAndStatus(
      @Param("id") Long id,
      @Param("status") CaseStatus caseStatus
  );

  @Query("SELECT ac FROM AnimalCase ac " +
      "LEFT JOIN FETCH ac.animal " +
      "LEFT JOIN FETCH ac.currentFoster " +
      "LEFT JOIN FETCH ac.histories " +
      "WHERE ac.id = :id")
  Optional<AnimalCase> findByIdWithHistories(@Param("id") Long id);

  @Query("SELECT ac FROM AnimalCase ac " +
      "LEFT JOIN FETCH ac.animal " +
      "LEFT JOIN FETCH ac.currentFoster " +
      "LEFT JOIN FETCH ac.histories " +
      "WHERE ac.id = :id AND ac.status IN :statuses")
  Optional<AnimalCase> findByIdAndStatusesWithHistories(
      @Param("id") Long id,
      @Param("statuses") Collection<CaseStatus> statuses
  );


  @Query(value = "SELECT ac FROM AnimalCase ac " +
      "LEFT JOIN FETCH ac.animal " +
      "LEFT JOIN FETCH ac.currentFoster " +
      "WHERE ac.status IN :statuses",
      countQuery = "SELECT COUNT(ac) FROM AnimalCase ac WHERE ac.status IN :statuses")
  Page<AnimalCase> findAllByStatusIn(
      @Param("statuses") Collection<CaseStatus> statuses,
      Pageable pageable
  );

  @Query(value = "SELECT ac FROM AnimalCase ac " +
      "LEFT JOIN FETCH ac.animal " +
      "LEFT JOIN FETCH ac.currentFoster " +
      "WHERE ac.currentFoster = :currentFoster",
      countQuery = "SELECT COUNT(ac) FROM AnimalCase ac WHERE ac.currentFoster = :currentFoster")
  Page<AnimalCase> findAllByCurrentFoster(
      @Param("currentFoster") Member currentFoster,
      Pageable pageable
  );

  // 기본 findById 오버라이딩
  @Query("SELECT ac FROM AnimalCase ac " +
      "LEFT JOIN FETCH ac.animal " +
      "LEFT JOIN FETCH ac.currentFoster " +
      "WHERE ac.id = :id")
  Optional<AnimalCase> findById(@Param("id") Long id);
}
