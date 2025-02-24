package com.patrol.domain.animalCase.repository;


import com.patrol.domain.animalCase.entity.CaseHistory;
import com.patrol.domain.animalCase.enums.CaseHistoryStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CaseHistoryRepository extends JpaRepository<CaseHistory, Long> {

  Optional<CaseHistory> findByAnimalCaseIdAndHistoryStatus(Long animalCaseId, CaseHistoryStatus historyStatus);
}
