package com.patrol.api.protection.dto;

import com.patrol.api.animalCase.dto.AnimalCaseListResponse;
import com.patrol.domain.facility.entity.OperatingHours;
import com.patrol.domain.facility.entity.Shelter;
import lombok.Builder;
import org.springframework.data.domain.Page;

@Builder
public record ShelterCasesResponse(
    Long shelterId,
    String shelterName,
    String shelterAddress,
    String shelterTel,
    Double latitude,
    Double longitude,
    OperatingHours operatingHours,
    Page<AnimalCaseListResponse> animalCases
) {
  public static ShelterCasesResponse of(
      Shelter shelter, Page<AnimalCaseListResponse> animalCases
  ) {
    return ShelterCasesResponse.builder()
        .shelterId(shelter.getId())
        .shelterName(shelter.getName())
        .shelterAddress(shelter.getAddress())
        .shelterTel(shelter.getTel())
        .latitude(shelter.getLatitude())
        .longitude(shelter.getLongitude())
        .operatingHours(shelter.getOperatingHours())
        .animalCases(animalCases)
        .build();
  }

}
