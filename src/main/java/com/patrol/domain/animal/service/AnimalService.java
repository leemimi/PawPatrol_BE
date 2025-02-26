package com.patrol.domain.animal.service;

import com.patrol.api.animal.dto.MyPetListResponse;
import com.patrol.api.animal.dto.PetResponseDto;
import com.patrol.api.animal.dto.request.DeleteMyPetInfoRequest;
import com.patrol.api.animal.dto.request.ModiPetInfoRequest;
import com.patrol.api.animal.dto.PetResponseDto;
import com.patrol.api.member.member.dto.request.PetRegisterRequest;
import com.patrol.domain.animal.entity.Animal;
import com.patrol.domain.animal.repository.AnimalRepository;
import com.patrol.domain.member.member.entity.Member;
import com.patrol.global.error.ErrorCode;
import com.patrol.global.exception.CustomException;
import com.patrol.global.storage.FileStorageHandler;
import com.patrol.global.storage.FileUploadRequest;
import com.patrol.global.storage.FileUploadResult;
import com.patrol.global.storage.StorageConfig;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * packageName    : com.patrol.domain.animal.service
 * fileName       : AnimalService
 * author         : sungjun
 * date           : 2025-02-24
 * description    : 자동 주석 생성
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-02-24        kyd54       최초 생성
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AnimalService {
    private final AnimalRepository animalRepository;
    private final FileStorageHandler fileStorageHandler;
    private final StorageConfig storageConfig;
    private final Logger logger = LoggerFactory.getLogger(AnimalService.class.getName());
    // 주인 있는 반려동물 등록
    @Transactional
    public void myPetRegister(Member member,
                            PetRegisterRequest petRegisterRequest) {
        logger.info("주인 있는 반려동물 등록");
        // 이미지 업로드
        FileUploadResult uploadResult = fileStorageHandler.handleFileUpload(
                FileUploadRequest.builder()
                        .folderPath("petRegister/" + member.getId())
                        .file(petRegisterRequest.imageFile())
                        .build()
        );

        String imageUrl = storageConfig.getEndpoint()
                + "/"
                + storageConfig.getBucketname()
                + "/"
                + uploadResult.getFullPath();

        // 동물 등록
        if(uploadResult != null) {
            Animal animal = petRegisterRequest.buildAnimal(member, imageUrl);
            animalRepository.save(animal);
        }

    }


    // 주인 없는 반려동물 등록
    @Transactional
    public void petRegister(PetRegisterRequest petRegisterRequest) {
        logger.info("주인 없는 반려동물 등록");
        // 이미지 업로드
        FileUploadResult uploadResult = fileStorageHandler.handleFileUpload(
                FileUploadRequest.builder()
                        .folderPath("petRegister/homeless")
                        .file(petRegisterRequest.imageFile())
                        .build()
        );

        // 네이버 S3 이미지 URL
        String imageUrl = storageConfig.getEndpoint()
                + "/"
                + storageConfig.getBucketname()
                + "/"
                + uploadResult.getFullPath();

        // 동물 등록
        if(uploadResult != null) {
            Animal animal = petRegisterRequest.buildAnimal(imageUrl);
            animalRepository.save(animal);
        }
    }


    @Transactional
    public Animal registerWithImageUrl(PetRegisterRequest petRegisterRequest, String imageUrl) {
      Animal animal = petRegisterRequest.buildAnimal(imageUrl);
      return animalRepository.save(animal);
    }

    public Optional<Animal> findById(Long animalId) {
      return animalRepository.findById(animalId);
    }


    // 등록된 나의 반려동물 리스트 가져오기 (마이페이지)
    @Transactional
    public List<MyPetListResponse> myPetList(Member member) {
        logger.info("등록된 나의 반려동물 리스트 가져오기 (마이페이지)");
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
                        .build())
                .collect(Collectors.toList());
    }


    // 내 반려동물 정보 수정 (마이페이지)
    @Transactional
    public void modifyMyPetInfo(Member member, ModiPetInfoRequest modiPetInfoRequest) {
        logger.info("내 반려동물 정보 수정 (마이페이지)");
        Animal animal = animalRepository.findById(modiPetInfoRequest.getId()).orElseThrow();

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
            // 이미지 업로드
            FileUploadResult uploadResult = fileStorageHandler.handleFileUpload(
                    FileUploadRequest.builder()
                            .folderPath("petRegister/" + member.getId())
                            .file(modiPetInfoRequest.getImageFile())
                            .build()
            );

            String imageUrl = storageConfig.getEndpoint()
                    + "/"
                    + storageConfig.getBucketname()
                    + "/"
                    + uploadResult.getFullPath();

            // 이미지 URL 업데이트
            animal.setImageUrl(imageUrl);
        }
    }

    // 내 반려동물 정보 삭제 (마이페이지)
    @Transactional
    public void deleteMyPetInfo(Member member,
                                DeleteMyPetInfoRequest deleteMyPetInfoRequest) {
        logger.info("내 반려동물 정보 삭제 (마이페이지)");
        Animal animal = animalRepository.findById(deleteMyPetInfoRequest.id()).orElseThrow();

        // 반려동물 소유자 검증
        validateOwner(animal, member);

        animalRepository.delete(animal);
    }

    // 반려동물 소유자 검증
    public void validateOwner(Animal animal, Member member) {
        if (!Objects.equals(animal.getOwner().getId(), member.getId())) {
            logger.error("해당 반려동물의 소유자가 아닙니다. 본인이 등록한 반려동물만 수정할 수 있습니다.");
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
