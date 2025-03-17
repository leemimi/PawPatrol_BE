package com.patrol.api.animal.controller;

import com.patrol.api.animal.dto.PetResponseDto;
import com.patrol.api.member.member.dto.request.PetRegisterRequest;
import com.patrol.domain.animal.service.AnimalService;
import com.patrol.domain.lostFoundPost.repository.LostFoundPostRepository;
import com.patrol.domain.member.member.entity.Member;
import com.patrol.global.globalDto.GlobalResponse;
import com.patrol.global.webMvc.LoginUser;
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
    public GlobalResponse<List<PetResponseDto>> getAllAnimals(@LoginUser Member member) {
        Long loggedInUserId = member.getId();

        List<PetResponseDto> allAnimals = animalService.getAllAnimals().stream()
                .filter(animal -> animal.ownerId() != null && animal.ownerId().equals(loggedInUserId)) // null 체크 후 비교
                .collect(Collectors.toList());

        List<Long> registeredPetIds = lostFoundPostRepository.findAllRegisteredPetIds();

        List<PetResponseDto> filteredAnimals = allAnimals.stream()
                .filter(animal -> !registeredPetIds.contains(animal.id())) // 등록된 petId는 제외
                .collect(Collectors.toList());

        return GlobalResponse.success(filteredAnimals);
    }
}
