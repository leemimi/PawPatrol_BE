package com.patrol.domain.protection.facility.entity;

import com.patrol.global.jpa.BaseEntity;
import jakarta.persistence.Embedded;

import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@MappedSuperclass
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public abstract class Facility extends BaseEntity {

  private String name;
  private String address;
  private String tel;
  private Double latitude;
  private Double longitude;
  @Embedded
  private OperatingHours operatingHours;
}
