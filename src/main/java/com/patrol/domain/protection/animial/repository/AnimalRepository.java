package com.patrol.domain.protection.animial.repository;

import com.patrol.domain.protection.animial.entity.Animal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnimalRepository extends JpaRepository<Animal, Long> {

}
