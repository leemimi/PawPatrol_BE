package com.patrol.api.animal.dto;

import com.patrol.domain.animal.entity.Animal;
import com.patrol.domain.animal.enums.AnimalGender;
import com.patrol.domain.animal.enums.AnimalSize;
import com.patrol.domain.animal.enums.AnimalType;

public record PetResponseDto(
        Long id,
        String name,
        String registrationNo,
        AnimalType animalType,
        String breed,
        AnimalGender gender,
        AnimalSize size,
        String imageUrl,
        String estimatedAge,
        String healthCondition,
        String feature,
        Long ownerId  // ownerId를 추가
) {
    public PetResponseDto(Animal animal) {
        this(
                animal.getId(),
                animal.getName(),
                animal.getRegistrationNo(),
                animal.getAnimalType(),
                animal.getBreed(),
                animal.getGender(),
                animal.getSize(),
                animal.getImageUrl(),
                animal.getEstimatedAge(),
                animal.getHealthCondition(),
                animal.getFeature(),
                animal.getOwner() != null ? animal.getOwner().getId() : null // owner의 id를 가져오기
        );
    }
}

