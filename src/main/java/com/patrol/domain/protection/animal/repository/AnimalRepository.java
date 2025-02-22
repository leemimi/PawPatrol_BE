package com.patrol.domain.protection.animal.repository;

import com.patrol.domain.protection.animal.entity.Animal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnimalRepository extends JpaRepository<Animal, Long> {
}
