package com.patrol.domain.facility.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.patrol.domain.animalCase.entity.AnimalCase;
import com.patrol.domain.member.member.entity.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.ALL;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Table(name = "shelters")
public class Shelter extends Facility {

  private Integer vetPersonCount;  // 수의사 수
  private String saveTargetAnimal; // 구조대상동물

  @JsonIgnore
  @OneToOne(mappedBy = "shelter", cascade = ALL, orphanRemoval = true)
  private Member shelterMember;

  @JsonIgnore
  @OneToMany(mappedBy = "shelter", cascade = CascadeType.ALL, orphanRemoval = true)
  @Builder.Default
  private List<AnimalCase> animalCases = new ArrayList<>();
}
