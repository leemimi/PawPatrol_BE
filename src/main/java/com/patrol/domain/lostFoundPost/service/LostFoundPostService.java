package com.patrol.domain.lostFoundPost.service;

import com.patrol.api.lostFoundPost.dto.lostFoundPostRequestDto;
import com.patrol.api.lostFoundPost.dto.lostFoundPostResponseDto;
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

    @Transactional
    public lostFoundPostResponseDto createLostFoundPost(lostFoundPostRequestDto requestDto, Long petId, Member author, List<MultipartFile> images) {

        Animal pet = null;
        if (petId != null) {
            pet = animalRepository.findById(petId)
                    .orElseThrow(() -> new EntityNotFoundException("Pet not found"));
        }

        LostFoundPost lostFoundPost = new LostFoundPost(requestDto, author, pet);
        lostFoundPostRepository.save(lostFoundPost);

        if (images != null && !images.isEmpty()) {
            List<String> uploadedPaths = new ArrayList<>();

            try {
                for (MultipartFile image : images) {
                    FileUploadResult uploadResult = fileStorageHandler.handleFileUpload(
                            FileUploadRequest.builder()
                                    .folderPath("lostfoundpost/")
                                    .file(image)
                                    .build()
                    );

                    if (uploadResult != null) {
                        uploadedPaths.add(uploadResult.getFileName());

                        Image imageEntity = Image.builder()
                                .path(uploadResult.getFileName())
                                .foundId(lostFoundPost.getId())
                                .build();

                        lostFoundPost.addImage(imageEntity);
                        imageRepository.save(imageEntity);
                    }
                }

            } catch (Exception e) {
                // 중간에 에러 발생 시 이미지 삭제
                for (String path : uploadedPaths) {
                    ncpObjectStorageService.delete(path);
                }
                throw new CustomException(ErrorCode.DATABASE_ERROR);
            }
        }

        return lostFoundPostResponseDto.from(lostFoundPost);
    }

    @Transactional
    public lostFoundPostResponseDto updateLostFoundPost(Long postId, lostFoundPostRequestDto requestDto, List<MultipartFile> images, Member author) {
        LostFoundPost lostFoundPost = lostFoundPostRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));

        if (!lostFoundPost.getAuthor().equals(author)) {
            throw new RuntimeException("게시글 수정 권한이 없습니다.");
        }

        lostFoundPost.setContent(requestDto.getContent());
        lostFoundPost.setFindTime(requestDto.getFindTime());

        if (images != null && !images.isEmpty()) {
            List<String> uploadedPaths = new ArrayList<>();

            try {
                for (MultipartFile image : images) {
                    FileUploadResult uploadResult = fileStorageHandler.handleFileUpload(
                            FileUploadRequest.builder()
                                    .folderPath("lostfoundpost/")
                                    .file(image)
                                    .build()
                    );

                    if (uploadResult != null) {
                        uploadedPaths.add(uploadResult.getFileName());

                        Image imageEntity = Image.builder()
                                .path(uploadResult.getFileName())
                                .foundId(lostFoundPost.getId())
                                .build();

                        imageRepository.save(imageEntity);
                    }
                }
            } catch (Exception e) {
                // 에러 발생 시 업로드된 이미지 삭제
                for (String path : uploadedPaths) {
                    ncpObjectStorageService.delete(path);
                }
                throw new CustomException(ErrorCode.DATABASE_ERROR);
            }
        }

        return lostFoundPostResponseDto.from(lostFoundPost);
    }


    @Transactional
    public void deleteLostFoundPost(Long postId,Member author) {
        LostFoundPost lostFoundPost = lostFoundPostRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));

        // 로그인한 사용자(author)가 게시글 작성자와 일치하는지 확인
        if (!lostFoundPost.getAuthor().equals(author)) {
            throw new RuntimeException("게시글 수정 권한이 없습니다.");
        }
        // 이미지 조회 및 삭제
        List<Image> images = imageRepository.findAllByFoundId(postId);
        images.forEach(image -> {
            ncpObjectStorageService.delete(image.getPath());
            imageRepository.delete(image);
        });

        lostFoundPostRepository.deleteById(postId);
    }


    @Transactional(readOnly = true)
    public Page<lostFoundPostResponseDto> getAllLostFoundPosts(Pageable pageable) {
        Page<LostFoundPost> findPosts = lostFoundPostRepository.findAll(pageable) ;
        return findPosts.map(lostFoundPostResponseDto::from);
    }


    @Transactional(readOnly = true)
    public lostFoundPostResponseDto getLostFoundPostById(Long postId){
        LostFoundPost lostFoundPost = lostFoundPostRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));

        return lostFoundPostResponseDto.from(lostFoundPost);
    }

    @Transactional(readOnly=true)
    public List<lostFoundPostResponseDto> getLostFoundPostsWithinRadius(double latitude, double longitude, double radius) {
        List<LostFoundPost> lostFoundPosts = lostFoundPostRepository.findPostsWithinRadius(latitude, longitude, radius);
        return lostFoundPosts.stream()
                .map(lostFoundPostResponseDto::from)
                .collect(Collectors.toList());
    }

}


