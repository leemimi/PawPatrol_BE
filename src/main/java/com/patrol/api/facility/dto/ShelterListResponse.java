package com.patrol.api.facility.dto;

import com.patrol.api.animalCase.dto.AnimalInfo;
import com.patrol.domain.facility.entity.OperatingHours;
import com.patrol.domain.facility.entity.Shelter;
import lombok.Builder;

import java.util.List;


@Builder
public record ShelterListResponse(
    Long id, String name, String address, String tel, Double latitude, Double longitude,
    OperatingHours operatingHours, String memberNickname, List<AnimalInfo> animals

) {

  public static ShelterListResponse of(Shelter shelter) {
    String memberNickname = null;
    if (shelter.getShelterMember() != null) {
      memberNickname = shelter.getShelterMember().getNickname();
    }

    List<AnimalInfo> animals = shelter.getAnimalCases().stream()
        .filter(animalCase -> animalCase.getAnimal() != null)
        .map(animalCase -> AnimalInfo.of(animalCase.getAnimal()))
        .toList();

    return ShelterListResponse.builder()
        .id(shelter.getId())
        .name(shelter.getName())
        .address(shelter.getAddress())
        .tel(shelter.getTel())
        .latitude(shelter.getLatitude())
        .longitude(shelter.getLongitude())
        .operatingHours(shelter.getOperatingHours())
        .memberNickname(memberNickname)
        .animals(animals)
        .build();
  }
}
