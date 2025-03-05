package com.patrol.domain.animal.service;

import com.patrol.api.animal.dto.MyPetListResponse;
import com.patrol.api.animal.dto.PetResponseDto;
import com.patrol.api.animal.dto.request.DeleteMyPetInfoRequest;
import com.patrol.api.animal.dto.request.ModiPetInfoRequest;
import com.patrol.api.member.member.dto.request.PetRegisterRequest;
import com.patrol.domain.animal.entity.Animal;
import com.patrol.domain.animal.repository.AnimalRepository;
import com.patrol.domain.image.entity.Image;
import com.patrol.domain.image.service.ImageHandlerService;
import com.patrol.domain.member.member.entity.Member;
import com.patrol.global.error.ErrorCode;
import com.patrol.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class AnimalService {
    private final AnimalRepository animalRepository;
    private final ImageHandlerService imageHandlerService;
    private static final String HOMELESS_FOLDER_PATH = "petRegister/homeless/";
    private static final String MEMBER_FOLDER_PATH_PREFIX = "petRegister/";

    @Transactional
    public void petRegister(PetRegisterRequest petRegisterRequest) {
        // 이미지 업로드
        List<Image> savedImages = imageHandlerService.uploadAndRegisterImages(
                List.of(petRegisterRequest.imageFile()),
                HOMELESS_FOLDER_PATH,
                null,
                null
        );

        if (!savedImages.isEmpty()) {
            String imageUrl = savedImages.get(0).getPath();
            Animal animal = petRegisterRequest.buildAnimal(imageUrl);
            Animal savedAnimal = animalRepository.save(animal);

            // 이미지에 동물 ID 업데이트
            Image image = savedImages.get(0);
            image.setAnimalId(savedAnimal.getId());
        } else {
            throw new CustomException(ErrorCode.FILE_UPLOAD_ERROR);
        }
    }

    @Transactional
    public void myPetRegister(Member member, PetRegisterRequest petRegisterRequest) {
        String folderPath = MEMBER_FOLDER_PATH_PREFIX + member.getId() + "/";

        // 이미지 업로드
        List<Image> savedImages = imageHandlerService.uploadAndRegisterImages(
                List.of(petRegisterRequest.imageFile()),
                folderPath,
                null,
                null
        );

        if (!savedImages.isEmpty()) {
            // 이미지 URL 가져오기
            String imageUrl = savedImages.get(0).getPath();
            // 동물 등록 (주인 정보 포함)
            Animal animal = petRegisterRequest.buildAnimal(member, imageUrl);
            Animal savedAnimal = animalRepository.save(animal);
            Image image = savedImages.get(0);
            image.setAnimalId(savedAnimal.getId());
        } else {
            throw new CustomException(ErrorCode.FILE_UPLOAD_ERROR);
        }
    }

    @Transactional
    public Animal registerWithImageUrl(PetRegisterRequest petRegisterRequest, String imageUrl) {
        // 동물 등록
        Animal animal = petRegisterRequest.buildAnimal(imageUrl);
        Animal savedAnimal = animalRepository.save(animal);

        // 이미지 등록 및 Kafka 이벤트 발행
        try {
            Image registeredImage = imageHandlerService.registerImage(imageUrl, savedAnimal.getId(), null);
        } catch (Exception e) {
            log.error("이미지 등록 중 오류 발생: {}", e.getMessage(), e);
            throw e;
        }
        log.info("이미지 URL을 통한 반려동물 등록 완료: 동물 ID={}", savedAnimal.getId());
        return savedAnimal;
    }

    public Optional<Animal> findById(Long animalId) {
        return animalRepository.findById(animalId);
    }

    // 등록된 나의 반려동물 리스트 가져오기 (마이페이지)
    @Transactional
    public List<MyPetListResponse> myPetList(Member member) {
        return animalRepository.findByOwnerId(member.getId())
                .stream()
                .map(animal -> MyPetListResponse.builder()
                        .id(animal.getId())
                        .name(animal.getName())
                        .breed(animal.getBreed())
                        .feature(animal.getFeature())
                        .estimatedAge(animal.getEstimatedAge())
                        .healthCondition(animal.getHealthCondition())
                        .size(animal.getSize())
                        .registrationNo(animal.getRegistrationNo())
                        .imageUrl(animal.getImageUrl())
                        .gender(animal.getGender())
                        .animalType(animal.getAnimalType())
                        .build())
                .collect(Collectors.toList());
    }

    // 내 반려동물 정보 수정 (마이페이지)
    @Transactional
    public void modifyMyPetInfo(Member member, ModiPetInfoRequest modiPetInfoRequest) {
        Animal animal = animalRepository.findById(modiPetInfoRequest.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.ANIMAL_NOT_FOUND));

        // 반려동물 소유자 검증
        validateOwner(animal, member);

        // 반려동물 정보 업데이트
        animal.setEstimatedAge(modiPetInfoRequest.getEstimatedAge());
        animal.setFeature(modiPetInfoRequest.getFeature());
        animal.setHealthCondition(modiPetInfoRequest.getHealthCondition());
        animal.setSize(modiPetInfoRequest.getSize());
        animal.setRegistrationNo(modiPetInfoRequest.getRegistrationNo());

        // 이미지 파일이 제공된 경우에만 처리
        if (modiPetInfoRequest.getImageFile() != null && !modiPetInfoRequest.getImageFile().isEmpty()) {
            String folderPath = "petRegister/" + member.getId() + "/";

            // 기존 이미지 삭제
            if (animal.getImageUrl() != null && !animal.getImageUrl().isEmpty()) {
                imageHandlerService.deleteImageByPath(animal.getImageUrl());
            }

            // 새 이미지 업로드 및 등록
            List<Image> savedImages = imageHandlerService.uploadAndRegisterImages(
                    List.of(modiPetInfoRequest.getImageFile()),
                    folderPath,
                    animal.getId(),
                    null
            );

            if (!savedImages.isEmpty()) {
                // 이미지 URL 업데이트
                animal.setImageUrl(savedImages.get(0).getPath());
            }
        }
    }

    // 내 반려동물 정보 삭제 (마이페이지)
    @Transactional
    public void deleteMyPetInfo(Member member, DeleteMyPetInfoRequest deleteMyPetInfoRequest) {
        Animal animal = animalRepository.findById(deleteMyPetInfoRequest.id())
                .orElseThrow(() -> new CustomException(ErrorCode.ANIMAL_NOT_FOUND));

        // 반려동물 소유자 검증
        validateOwner(animal, member);

        // 반려동물 이미지 삭제
        if (animal.getImageUrl() != null && !animal.getImageUrl().isEmpty()) {
            imageHandlerService.deleteImageByPath(animal.getImageUrl());
        }

        animalRepository.delete(animal);
    }

    // 반려동물 소유자 검증
    public void validateOwner(Animal animal, Member member) {
        if (!Objects.equals(animal.getOwner().getId(), member.getId())) {
            throw new CustomException(ErrorCode.PET_OWNER_MISMATCH);
        }
    }

    public List<PetResponseDto> getAllAnimals() {
        // Fetch all animals from the repository and convert to PetResponseDto
        return animalRepository.findAll().stream()
                .map(PetResponseDto::new)  // Convert Animal to PetResponseDto using the constructor
                .collect(Collectors.toList());  // Collect them into a List
    }

}
