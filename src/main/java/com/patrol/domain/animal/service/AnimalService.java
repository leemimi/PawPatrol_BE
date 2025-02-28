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
import com.patrol.global.storage.FileStorageHandler;
import com.patrol.global.storage.FileUploadRequest;
import com.patrol.global.storage.FileUploadResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class AnimalService {
    private final AnimalRepository animalRepository;
    private final FileStorageHandler fileStorageHandler;
    private final ImageHandlerService imageHandlerService;
    private static final String FOLDER_PATH = "petRegister/homeless/";


    // 주인 없는 반려동물 등록
    @Transactional
    public void petRegister(PetRegisterRequest petRegisterRequest) {
        log.info("주인 없는 반려동물 등록 시작");
        // 이미지 업로드
        FileUploadResult uploadResult = fileStorageHandler.handleFileUpload(
                FileUploadRequest.builder()
                        .folderPath(FOLDER_PATH)
                        .file(petRegisterRequest.imageFile())
                        .build()
        );

        if (uploadResult != null) {
            // ImageHandlerService를 통해 이미지 URL 생성
            String imageUrl = imageHandlerService.createImageUrl(FOLDER_PATH, uploadResult.getFileName());
            log.info("이미지 URL 생성 완료: {}", imageUrl);

            // 동물 등록
            Animal animal = petRegisterRequest.buildAnimal(imageUrl);
            log.info("동물 객체 생성 완료: {}", animal);

            Animal savedAnimal = animalRepository.save(animal);
            log.info("동물 저장 완료: ID={}", savedAnimal.getId());

            // 중요: 이미지 등록 및 Kafka 이벤트 발행
            try {
                Image registeredImage = imageHandlerService.registerImage(imageUrl, savedAnimal.getId(), null);
                log.info("동물 이미지 등록 완료: 동물 ID={}, 이미지 ID={}, 이미지 경로={}",
                        savedAnimal.getId(), registeredImage.getId(), registeredImage.getPath());
            } catch (Exception e) {
                log.error("이미지 등록 중 오류 발생: {}", e.getMessage(), e);
                // 필요한 경우 예외 처리 추가
                throw e; // 트랜잭션 롤백을 위해 예외를 다시 throw
            }
        } else {
            log.warn("파일 업로드 결과가 null입니다.");
        }
        log.info("주인 없는 반려동물 등록 완료");
    }


    @Transactional
    public Animal registerWithImageUrl(PetRegisterRequest petRegisterRequest, String imageUrl) {
        log.info("이미지 URL을 통한 반려동물 등록 시작: {}", imageUrl);
        // 동물 등록
        Animal animal = petRegisterRequest.buildAnimal(imageUrl);
        Animal savedAnimal = animalRepository.save(animal);
        log.info("동물 저장 완료: ID={}", savedAnimal.getId());

        // 이미지 등록 및 Kafka 이벤트 발행
        try {
            Image registeredImage = imageHandlerService.registerImage(imageUrl, savedAnimal.getId(), null);
            log.info("동물 이미지 등록 완료: 동물 ID={}, 이미지 ID={}, 이미지 경로={}",
                    savedAnimal.getId(), registeredImage.getId(), registeredImage.getPath());
        } catch (Exception e) {
            log.error("이미지 등록 중 오류 발생: {}", e.getMessage(), e);
            // 필요한 경우 예외 처리 추가
            throw e; // 트랜잭션 롤백을 위해 예외를 다시 throw
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

            // 기존 파일 삭제
            fileStorageHandler.handleFileDelete(modiPetInfoRequest.getImageUrl());

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

        // S3에 있는 반려동물 이미지 삭제
        fileStorageHandler.handleFileDelete(animal.getImageUrl());

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
