package com.patrol.api.protection.dto;

import com.patrol.domain.animal.entity.Animal;
import com.patrol.domain.animal.enums.AnimalGender;
import com.patrol.domain.animal.enums.AnimalSize;
import com.patrol.domain.animal.enums.AnimalType;

import java.util.List;

public record CreateAnimalCaseRequest(
    String title,
    String description,
    String location,
    String breed,
    AnimalGender gender,
    AnimalSize size,
    String feature,
    String healthCondition,
    String name,
    String estimatedAge,
    String registrationNo,
    AnimalType animalType,
    List<String> animalImageUrls
) {
  public Animal toAnimal() {
    return Animal.builder()
        .breed(breed)
        .gender(gender)
        .size(size)
        .feature(feature)
        .healthCondition(healthCondition)
        .name(name)
        .estimatedAge(estimatedAge)
        .registrationNo(registrationNo)
        .animalType(animalType)
        .build();
  }
}
