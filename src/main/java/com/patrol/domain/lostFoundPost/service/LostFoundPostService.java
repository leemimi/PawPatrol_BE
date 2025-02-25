package com.patrol.domain.lostFoundPost.service;

import com.patrol.api.lostFoundPost.dto.LostFoundPostRequestDto;
import com.patrol.api.lostFoundPost.dto.LostFoundPostResponseDto;
import com.patrol.domain.animal.entity.Animal;
import com.patrol.domain.animal.repository.AnimalRepository;
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
    private final FileStorageHandler fileStorageHandler;
    private final LostFoundPostRepository lostFoundPostRepository;
    private final AnimalRepository animalRepository;
    private final ImageRepository imageRepository;
    private final NcpObjectStorageService ncpObjectStorageService;

    @Value("${ncp.storage.endpoint}")
    private String endPoint;

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
                imageRepository.save(petImage);
            }
        }

        if (images != null && !images.isEmpty()) {
            uploadImages(images, lostFoundPost);
        }

        return LostFoundPostResponseDto.from(lostFoundPost);
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

        if (images != null && !images.isEmpty()) {
            uploadImages(images, lostFoundPost);
        }

        return LostFoundPostResponseDto.from(lostFoundPost);
    }

    private void uploadImages(List<MultipartFile> images, LostFoundPost lostFoundPost) {
        List<String> uploadedPaths = new ArrayList<>();

        try {
            for (MultipartFile image : images) {
                FileUploadResult uploadResult = fileStorageHandler.handleFileUpload(
                        FileUploadRequest.builder()
                                .folderPath(FOLDER_PATH)
                                .file(image)
                                .build()
                );

                if (uploadResult != null) {
                    String fileName = uploadResult.getFileName();
                    uploadedPaths.add(fileName);

                    Image imageEntity = Image.builder()
                            .path(endPoint+"/paw-patrol/"+FOLDER_PATH+fileName)
                            .foundId(lostFoundPost.getId())
                            .build();

                    lostFoundPost.addImage(imageEntity);
                    imageRepository.save(imageEntity);
                }
            }
        } catch (Exception e) {
            for (String path : uploadedPaths) {
                ncpObjectStorageService.delete(path);
            }
            throw new CustomException(ErrorCode.DATABASE_ERROR);
        }
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

    private void deletePostImages(Long postId) {
        List<Image> images = imageRepository.findAllByFoundId(postId);
        images.forEach(image -> {
            ncpObjectStorageService.delete(image.getPath());
            imageRepository.delete(image);
        });
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
}
