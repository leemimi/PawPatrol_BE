package com.patrol.api.animal.controller;

import com.patrol.api.animal.dto.PetResponseDto;
import com.patrol.api.member.member.dto.request.PetRegisterRequest;
import com.patrol.domain.animal.service.AnimalService;
import com.patrol.domain.lostFoundPost.repository.LostFoundPostRepository;
import com.patrol.global.globalDto.GlobalResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * packageName    : com.patrol.api.animal.controller
 * fileName       : ApiV1AnimalController
 * author         : sungjun
 * date           : 2025-02-24
 * description    : 자동 주석 생성
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-02-24        kyd54       최초 생성
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/animals")
public class ApiV1AnimalController {
    private final AnimalService animalService;
    private final LostFoundPostRepository lostFoundPostRepository;

    // 반려동물 등록 (주인 없는 경우)
    @PostMapping("/register")
    public GlobalResponse<Void> petRegister(@ModelAttribute PetRegisterRequest petRegisterRequest) {

        animalService.petRegister(petRegisterRequest);

        return GlobalResponse.success();
    }
    @GetMapping("/list")
    public GlobalResponse<List<PetResponseDto>> getAllAnimals() {
        // 동물 목록을 모두 가져옵니다.
        List<PetResponseDto> animals = animalService.getAllAnimals();

        // 이미 LostFoundPost에 등록된 petId들을 조회합니다.
        List<Long> registeredPetIds = lostFoundPostRepository.findAllRegisteredPetIds();

        // 등록된 petId를 제외한 동물만 필터링합니다.
        List<PetResponseDto> filteredAnimals = animals.stream()
                .filter(animal -> !registeredPetIds.contains(animal.id()))  // 등록된 petId는 제외
                .collect(Collectors.toList());

        // 필터링된 동물 목록을 반환합니다.
        return GlobalResponse.success(filteredAnimals);
    }
}
