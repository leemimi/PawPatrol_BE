package com.patrol.domain.protection.animal.entity;

import com.patrol.global.jpa.BaseAnimalEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
@Table(name = "animals")
public class Animal extends BaseAnimalEntity {

  private String name;
  private String estimatedAge;  // 3살 추정

}
