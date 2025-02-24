package com.patrol.api.animal.controller;

import com.patrol.api.member.member.dto.request.PetRegisterRequest;
import com.patrol.domain.animal.service.AnimalService;
import com.patrol.global.globalDto.GlobalResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    // 반려동물 등록 (주인 없는 경우)
    @PostMapping("/register")
    public GlobalResponse<Void> petRegister(@ModelAttribute PetRegisterRequest petRegisterRequest) {

        animalService.petRegister(petRegisterRequest);

        return GlobalResponse.success();
    }
}
