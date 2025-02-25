package com.patrol.global.jpa;



import com.patrol.domain.animal.enums.AnimalGender;
import com.patrol.domain.animal.enums.AnimalSize;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@MappedSuperclass
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public abstract class BaseAnimalEntity extends BaseEntity {

  private String breed;

  @Enumerated(EnumType.STRING)
  private AnimalGender gender;

  @Enumerated(EnumType.STRING)
  private AnimalSize size;

  private String feature;
  private String healthCondition;
}
