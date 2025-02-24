package com.patrol.api.animal.dto;

import com.patrol.domain.animal.entity.Animal;
import com.patrol.domain.animal.enums.AnimalGender;
import com.patrol.domain.animal.enums.AnimalSize;
import com.patrol.domain.animal.enums.AnimalType;

public record PetResponseDto(
        String name,
        String registrationNo,
        AnimalType animalType,
        String breed,
        AnimalGender gender,
        AnimalSize size,
        String imageUrl,
        String estimatedAge,
        String healthCondition,
        String feature
) {
    public PetResponseDto(Animal animal) {
        this(
                animal.getName(),
                animal.getRegistrationNo(),
                animal.getAnimalType(),
                animal.getBreed(),
                animal.getGender(),
                animal.getSize(),
                animal.getImageUrl(),
                animal.getEstimatedAge(),
                animal.getHealthCondition(),
                animal.getFeature()
        );
    }
}

