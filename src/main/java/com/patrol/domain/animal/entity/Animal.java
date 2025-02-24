package com.patrol.domain.animal.entity;

import com.patrol.domain.animal.enums.AnimalGender;
import com.patrol.domain.animal.enums.AnimalSize;
import com.patrol.domain.member.member.entity.Member;
import com.patrol.global.jpa.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "animals")
public class Animal extends BaseEntity {
  private String breed;

  @Enumerated(EnumType.STRING)
  private AnimalGender gender;

  @Enumerated(EnumType.STRING)
  private AnimalSize size;

  private String feature;
  private String healthCondition;
  private String name;
  private String estimatedAge;

  @ManyToOne(fetch = FetchType.LAZY)
  private Member owner;

  private String registrationNo;
}
