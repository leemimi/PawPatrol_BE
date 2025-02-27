package com.patrol.api.animal.dto.request;

import jakarta.validation.constraints.NotBlank;

/**
 * packageName    : com.patrol.api.animal.dto.request
 * fileName       : DeleteMyPetInfoRequest
 * author         : sungjun
 * date           : 2025-02-26
 * description    : 자동 주석 생성
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-02-26        kyd54       최초 생성
 */
public record DeleteMyPetInfoRequest (
        @NotBlank
        Long id
){}
