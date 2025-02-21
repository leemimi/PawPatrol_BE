package com.patrol.domain.protection.animial.service;

import com.patrol.domain.protection.animial.repository.AnimalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AnimalService {

  private final AnimalRepository animalRepository;

}
