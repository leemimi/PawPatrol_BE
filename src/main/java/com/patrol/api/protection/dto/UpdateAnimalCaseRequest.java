package com.patrol.api.protection.dto;

import com.patrol.domain.animal.entity.Animal;
import com.patrol.domain.animal.enums.AnimalGender;
import com.patrol.domain.animal.enums.AnimalSize;
import com.patrol.domain.animal.enums.AnimalType;
import com.patrol.domain.animalCase.entity.AnimalCase;

public record UpdateAnimalCaseRequest(
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
    AnimalType animalType
) {

  public Animal updateAnimal(AnimalCase animalCase) {
    animalCase.setTitle(title);
    animalCase.setDescription(description);
    animalCase.setLocation(location);
    Animal animal = animalCase.getAnimal();
    animal.setBreed(breed);
    animal.setGender(gender);
    animal.setSize(size);
    animal.setFeature(feature);
    animal.setHealthCondition(healthCondition);
    animal.setName(name);
    animal.setEstimatedAge(estimatedAge);
    animal.setRegistrationNo(registrationNo);
    animal.setAnimalType(animalType);

    return animal;
  }
}
