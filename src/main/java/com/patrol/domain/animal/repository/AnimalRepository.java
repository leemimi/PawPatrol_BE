package com.patrol.domain.animal.repository;

import com.patrol.domain.animal.entity.Animal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnimalRepository extends JpaRepository<Animal, Long> {
}
