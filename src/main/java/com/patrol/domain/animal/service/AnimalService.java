package com.patrol.domain.animal.service;

import com.patrol.domain.animal.entity.Animal;
import com.patrol.domain.animal.repository.AnimalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AnimalService {

  private final AnimalRepository animalRepository;

  public Optional<Animal> findById(Long animalId) {
    return animalRepository.findById(animalId);
  }
}
