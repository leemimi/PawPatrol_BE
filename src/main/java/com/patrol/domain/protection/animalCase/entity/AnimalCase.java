package com.patrol.domain.protection.animalCase.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.patrol.domain.protection.animalCase.enums.CaseStatus;
import com.patrol.domain.protection.animalCase.enums.TargetType;
import com.patrol.global.jpa.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;


@Entity
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "animal_cases")
public class AnimalCase extends BaseEntity {

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private CaseStatus status;  // 상태 : 실종, 발견, 구조, 임시보호, 완료 등

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private TargetType targetType;  // 대상 : Animal(미정) or MyPet(내 애완동물)

  @Column(nullable = false)
  private Long targetId;  // 대상의 ID : animalId or myPetId

  @JsonIgnore
  @OneToMany(mappedBy = "animalCase")
  private List<CaseHistory> histories = new ArrayList<>();


  public void addHistory(CaseHistory caseHistory) {
    histories.add(caseHistory);
  }

  public void updateStatus(CaseStatus status) {
    this.status = status;
  }

}
