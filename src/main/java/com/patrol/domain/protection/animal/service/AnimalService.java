package com.patrol.domain.protection.animal.service;

import com.patrol.domain.protection.animal.entity.Animal;
import com.patrol.domain.protection.animal.repository.AnimalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AnimalService {

  private final AnimalRepository animalRepository;


  // DTO 필요
  public Animal createAnimal() {
//    Animal animal = Animal.builder()
//        .breed()
//        .gender()
//        .size()
//        .healthCondition()
//        .feature()
//        .name()
//        .estimatedAge()
//        .build();

    // return animalRepository.save(animal);
    return null;
  }

}
