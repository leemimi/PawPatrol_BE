package com.patrol.api.member.member.dto.request;

import com.patrol.domain.animal.entity.Animal;
import com.patrol.domain.animal.enums.AnimalGender;
import com.patrol.domain.animal.enums.AnimalSize;
import com.patrol.domain.animal.enums.AnimalType;
import com.patrol.domain.image.entity.Image;
import com.patrol.domain.member.member.entity.Member;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

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
        String name,    // 이름
        String registrationNo,  // 동물등록번호
        @Enumerated(EnumType.STRING)
        AnimalType animalType,   // 동물 타입
        String breed,   // 품종
        @Enumerated(EnumType.STRING)
        AnimalGender gender,  // 성별
        @Enumerated(EnumType.STRING)
        AnimalSize size,        // 크기
//        @NotBlank
        MultipartFile imageFile,      // 사진
        String estimatedAge,    // 나이
        String healthCondition, // 건강상태
        String feature // 특징
) {
        // 반려동물 등록시 Animal 객체 생성에 사용 (주인 있는 경우)
        public Animal buildAnimal(Member owner, String imageUrl) {

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

        // 반려동물 등록시 Animal 객체 생성에 사용 (주인 없는 경우)
        // 주인 ID, 반려동물 이름, 동물등록번호,
        public Animal buildAnimal(String imageUrl) {
                Animal.AnimalBuilder builder = Animal.builder()
                        .animalType(this.animalType)    // 필수
                        .imageUrl(imageUrl);


                // 선택적 파라미터들은 null 체크 후 설정
                Optional.ofNullable(this.breed).ifPresent(builder::breed);
                Optional.ofNullable(this.gender).ifPresent(builder::gender);
                Optional.ofNullable(this.size).ifPresent(builder::size);
                Optional.ofNullable(this.estimatedAge).ifPresent(builder::estimatedAge);
                Optional.ofNullable(this.healthCondition).ifPresent(builder::healthCondition);
                Optional.ofNullable(this.feature).ifPresent(builder::feature);

                return builder.build();
        }
}
