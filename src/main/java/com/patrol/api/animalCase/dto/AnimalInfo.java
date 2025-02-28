package com.patrol.api.animalCase.dto;


import com.patrol.domain.animal.entity.Animal;
import com.patrol.domain.animal.enums.AnimalGender;
import com.patrol.domain.animal.enums.AnimalSize;
import com.patrol.domain.animal.enums.AnimalType;
import lombok.Builder;

@Builder
public record AnimalInfo(
    String name, String age, String breed, AnimalGender gender,
    AnimalSize size, String feature, String healthCondition, AnimalType animalType,
    String imageUrl, String registrationNo
) {

  public static AnimalInfo of(Animal animal) {
    return AnimalInfo.builder()
        .name(animal.getName())
        .age(animal.getEstimatedAge())
        .breed(animal.getBreed())
        .gender(animal.getGender())
        .size(animal.getSize())
        .feature(animal.getFeature())
        .healthCondition(animal.getHealthCondition())
        .animalType(animal.getAnimalType())
        .imageUrl(animal.getImageUrl())
        .registrationNo(animal.getRegistrationNo())
        .build();
  }
}
