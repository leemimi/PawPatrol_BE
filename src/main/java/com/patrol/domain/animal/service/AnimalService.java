package com.patrol.domain.animal.service;

import com.patrol.api.animal.dto.MyPetListResponse;
import com.patrol.api.member.member.dto.request.PetRegisterRequest;
import com.patrol.domain.animal.entity.Animal;
import com.patrol.domain.animal.repository.AnimalRepository;
import com.patrol.domain.member.member.entity.Member;
import com.patrol.global.storage.FileStorageHandler;
import com.patrol.global.storage.FileUploadRequest;
import com.patrol.global.storage.FileUploadResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
@Transactional
public class AnimalService {
    private final AnimalRepository animalRepository;
    private final FileStorageHandler fileStorageHandler;


    // 주인 없는 반려동물 등록
    @Transactional
    public void petRegister(PetRegisterRequest petRegisterRequest) {

        // 이미지 업로드
        FileUploadResult uploadResult = fileStorageHandler.handleFileUpload(
                FileUploadRequest.builder()
                        .folderPath("petRegister/homeless")
                        .file(petRegisterRequest.imageFile())
                        .build()
        );

        // 동물 등록
        if(uploadResult != null) {
            Animal animal = petRegisterRequest.buildAnimal(uploadResult.getFullPath());
            animalRepository.save(animal);
        }
    }

    // 등록된 나의 반려동물 리스트 가져오기
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
