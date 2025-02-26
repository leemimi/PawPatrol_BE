package com.patrol.domain.protection.repository;

import com.patrol.domain.protection.entity.Protection;
import com.patrol.domain.protection.enums.ProtectionStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface ProtectionRepository extends JpaRepository<Protection, Long> {
  @Query("SELECT p FROM Protection p " +
      "JOIN FETCH p.applicant " +
      "JOIN FETCH p.animalCase ac " +
      "JOIN FETCH ac.animal " +
      "WHERE p.applicant.id = :applicantId AND p.deletedAt IS NULL")
  Page<Protection> findAllByApplicantIdAndDeletedAtIsNull(@Param("applicantId") Long applicantId, Pageable pageable);

  @Query("SELECT p FROM Protection p " +
      "JOIN FETCH p.applicant " +
      "JOIN FETCH p.animalCase ac " +
      "JOIN FETCH ac.animal " +
      "WHERE p.id = :id")
  Optional<Protection> findByIdWithFetchAll(@Param("id") Long id);

  boolean existsByApplicantIdAndAnimalCaseIdAndProtectionStatusAndDeletedAtIsNull(
      Long applicantId, Long animalCaseId, ProtectionStatus status);
}
