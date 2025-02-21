package com.patrol.domain.protection.protection.repository;

import com.patrol.domain.protection.protection.entity.Protection;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProtectionRepository extends JpaRepository<Protection, Long> {
  
}
