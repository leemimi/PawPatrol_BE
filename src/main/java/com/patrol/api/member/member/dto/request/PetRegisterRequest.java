package com.patrol.api.member.member.dto.request;

import com.patrol.domain.animal.entity.Animal;
import com.patrol.domain.animal.enums.AnimalGender;
import com.patrol.domain.animal.enums.AnimalSize;
import com.patrol.domain.animal.enums.AnimalType;
import com.patrol.domain.member.member.entity.Member;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.multipart.MultipartFile;

/**
 * packageName    : com.patrol.api.member.member.dto.request
 * fileName       : PetRegisterRequest
 * author         : sungjun
 * date           : 2025-02-24
 * description    : 자동 주석 생성
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-02-24        kyd54       최초 생성
 */
public record PetRegisterRequest (
        @NotBlank
        String name,    // 이름
        @NotBlank
        String registrationNo,  // 동물등록번호
        @NotBlank
        @Enumerated(EnumType.STRING)
        AnimalType animalType,   // 동물 타입
        @NotBlank
        String breed,   // 품종
        @NotBlank
        @Enumerated(EnumType.STRING)
        AnimalGender gender,  // 성별
        @NotBlank
        @Enumerated(EnumType.STRING)
        AnimalSize size,        // 크기
//        @NotBlank
        MultipartFile imageFile,      // 사진
        String estimatedAge,    // 나이
        String healthCondition, // 건강상태
        String feature // 특징
) {
        // 반려동물 등록시 Animal 객체 생성에 사용
        public Animal toEntity(Member owner, String imageUrl) {
                return Animal.builder()
                        .owner(owner)
                        .name(this.name)
                        .registrationNo(this.registrationNo)
                        .animalType(this.animalType)
                        .breed(this.breed)
                        .gender(this.gender)
                        .size(this.size)
                        .imageUrl(imageUrl)
                        .estimatedAge(this.estimatedAge)
                        .healthCondition(this.healthCondition)
                        .feature(this.feature)
                        .build();
        }
}
