package com.patrol.domain.protection.animalCase.repository;

import com.patrol.domain.protection.animalCase.entity.CaseHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CaseHistoryRepository extends JpaRepository<CaseHistory, Long> {
}
