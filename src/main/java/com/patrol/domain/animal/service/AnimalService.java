package com.patrol.domain.animal.service;

import com.patrol.api.animal.dto.MyPetListResponse;
import com.patrol.api.member.member.dto.request.PetRegisterRequest;
import com.patrol.domain.animal.entity.Animal;
import com.patrol.domain.animal.repository.AnimalRepository;
import com.patrol.domain.image.entity.Image;
import com.patrol.domain.image.service.ImageHandlerService;
import com.patrol.domain.member.member.entity.Member;
import com.patrol.global.storage.FileStorageHandler;
import com.patrol.global.storage.FileUploadRequest;
import com.patrol.global.storage.FileUploadResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        return animalRepository.findByOwnerId(member.getId())
                .stream()
                .map(animal -> MyPetListResponse.builder()
                        .id(animal.getId())
                        .name(animal.getName())
                        .breed(animal.getBreed())
                        .characteristics(animal.getFeature())
                        .size(animal.getSize().toString())
                        .registrationNumber(animal.getRegistrationNo())
                        .imageUrl(animal.getImageUrl())
                        .build())
                .collect(Collectors.toList());
    }
}
