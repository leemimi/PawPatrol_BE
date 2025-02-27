package com.patrol.domain.lostFoundPost.service;

import com.patrol.api.lostFoundPost.dto.LostFoundPostRequestDto;
import com.patrol.api.lostFoundPost.dto.LostFoundPostResponseDto;
import com.patrol.domain.animal.entity.Animal;
import com.patrol.domain.animal.repository.AnimalRepository;
import com.patrol.domain.image.service.ImageHandlerService;
import com.patrol.domain.image.service.ImageService;
import com.patrol.domain.lostFoundPost.entity.LostFoundPost;
import com.patrol.domain.lostFoundPost.repository.LostFoundPostRepository;
import com.patrol.domain.image.entity.Image;
import com.patrol.domain.image.repository.ImageRepository;
import com.patrol.domain.member.member.entity.Member;
import com.patrol.global.error.ErrorCode;
import com.patrol.global.exception.CustomException;
import com.patrol.global.storage.FileStorageHandler;
import com.patrol.global.storage.FileUploadRequest;
import com.patrol.global.storage.FileUploadResult;
import com.patrol.global.storage.NcpObjectStorageService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LostFoundPostService {
    private final LostFoundPostRepository lostFoundPostRepository;
    private final AnimalRepository animalRepository;
    private final ImageRepository imageRepository;
    private final ImageHandlerService imageHandlerService;

    private static final String FOLDER_PATH = "lostfoundpost/";

    @Transactional
    public LostFoundPostResponseDto createLostFoundPost(LostFoundPostRequestDto requestDto, Long petId, Member author, List<MultipartFile> images) {
        Animal pet = null;
        if (petId != null) {
            pet = animalRepository.findById(petId)
                    .orElseThrow(() -> new EntityNotFoundException("Pet not found"));
        }

        LostFoundPost lostFoundPost = new LostFoundPost(requestDto, author, pet);
        lostFoundPostRepository.save(lostFoundPost);

        if (petId != null) {
            Image petImage = imageRepository.findByAnimalId(petId);
            if (petImage != null) {
                petImage.setFoundId(lostFoundPost.getId());
                imageHandlerService.registerImage(petImage.getPath(), petImage.getAnimalId(), lostFoundPost.getId());
            } // 일반 saveImage 메소드 사용
        }

        return getImageSave(images, lostFoundPost);
    }

    @Transactional
    public LostFoundPostResponseDto updateLostFoundPost(Long postId, LostFoundPostRequestDto requestDto, List<MultipartFile> images, Member author) {
        LostFoundPost lostFoundPost = lostFoundPostRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));

        if (!lostFoundPost.getAuthor().equals(author)) {
            throw new RuntimeException("게시글 수정 권한이 없습니다.");
        }

        lostFoundPost.setContent(requestDto.getContent());
        lostFoundPost.setFindTime(requestDto.getFindTime());

        return getImageSave(images, lostFoundPost);
    }

    @Transactional
    public void deleteLostFoundPost(Long postId, Member author) {
        LostFoundPost lostFoundPost = lostFoundPostRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));

        if (!lostFoundPost.getAuthor().equals(author)) {
            throw new RuntimeException("게시글 삭제 권한이 없습니다.");
        }

        deletePostImages(postId);
        lostFoundPostRepository.deleteById(postId);
    }

    @Transactional(readOnly = true)
    public Page<LostFoundPostResponseDto> getAllLostFoundPosts(Pageable pageable) {
        Page<LostFoundPost> findPosts = lostFoundPostRepository.findAll(pageable);
        return findPosts.map(LostFoundPostResponseDto::from);
    }

    @Transactional(readOnly = true)
    public LostFoundPostResponseDto getLostFoundPostById(Long postId) {
        LostFoundPost lostFoundPost = lostFoundPostRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));

        return LostFoundPostResponseDto.from(lostFoundPost);
    }

    @Transactional(readOnly = true)
    public List<LostFoundPostResponseDto> getLostFoundPostsWithinRadius(double latitude, double longitude, double radius) {
        List<LostFoundPost> lostFoundPosts = lostFoundPostRepository.findPostsWithinRadius(latitude, longitude, radius);
        return lostFoundPosts.stream()
                .map(LostFoundPostResponseDto::from)
                .collect(Collectors.toList());
    }

    @NotNull
    private LostFoundPostResponseDto getImageSave(List<MultipartFile> images, LostFoundPost lostFoundPost) {
        if (images != null && !images.isEmpty()) {
            // 새로운 uploadAndRegisterImages 메소드 사용
            // (animalId는 null, foundId는 게시글 ID)
            List<Image> savedImages = imageHandlerService.uploadAndRegisterImages(images, FOLDER_PATH, null, lostFoundPost.getId());

            // 게시글에 이미지 추가
            for (Image image : savedImages) {
                lostFoundPost.addImage(image);
            }
        }

        return LostFoundPostResponseDto.from(lostFoundPost);
    }

    private void deletePostImages(Long postId) {
        List<Image> images = imageRepository.findAllByFoundId(postId);
        for (Image image : images) {
            imageHandlerService.deleteImage(image); // 이미지 핸들러로 이미지 삭제
        }
    }
}
