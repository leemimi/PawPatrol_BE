package com.patrol.api.facility.dto;

import com.patrol.domain.facility.entity.Facility;
import com.patrol.domain.facility.entity.OperatingHours;
import lombok.Builder;

@Builder
public record FacilitiesResponse(
    Long id, String name, String address, String tel, Double latitude, Double longitude,
    OperatingHours operatingHours
) {

  public static FacilitiesResponse of(Facility facility) {
    return FacilitiesResponse.builder().id(facility.getId())
        .name(facility.getName())
        .address(facility.getAddress())
        .tel(facility.getTel())
        .latitude(facility.getLatitude())
        .longitude(facility.getLongitude())
        .build();
  }
}
