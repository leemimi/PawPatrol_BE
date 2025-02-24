package com.patrol.domain.facility.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Table(name = "shelters")
public class Shelter extends Facility {

  private Integer vetPersonCount;  // 수의사 수
  private String saveTargetAnimal; // 구조대상동물
}
