package com.patrol.domain.lostFoundPost.service;

import com.patrol.api.lostFoundPost.dto.LostFoundPostRequestDto;
import com.patrol.api.lostFoundPost.dto.LostFoundPostResponseDto;
import com.patrol.api.member.auth.dto.MyPostsResponse;
import com.patrol.domain.animal.entity.Animal;
import com.patrol.domain.animal.enums.AnimalType;
import com.patrol.domain.animal.repository.AnimalRepository;
import com.patrol.domain.animal.service.AnimalService;
import com.patrol.domain.image.service.ImageEventProducer;
import com.patrol.domain.image.service.ImageHandlerService;
import com.patrol.domain.image.service.ImageService;
import com.patrol.domain.lostFoundPost.entity.LostFoundPost;
import com.patrol.domain.lostFoundPost.entity.PostStatus;
import com.patrol.domain.lostFoundPost.repository.LostFoundPostRepository;
import com.patrol.domain.image.entity.Image;
import com.patrol.domain.image.repository.ImageRepository;
import com.patrol.domain.member.member.entity.Member;
import com.patrol.global.error.ErrorCode;
import com.patrol.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class LostFoundPostService {
    private final LostFoundPostRepository lostFoundPostRepository;
    private final AnimalRepository animalRepository;
    private final ImageRepository imageRepository;
    private final ImageHandlerService imageHandlerService;
    private final ImageService imageService;
    private final ImageEventProducer imageEventProducer;

    private static final String FOLDER_PATH = "lostfoundpost/";
    private final AnimalService animalService;

    @Transactional
    public LostFoundPostResponseDto createLostFoundPost(LostFoundPostRequestDto requestDto, Long petId, Member author, List<MultipartFile> images) {

        // Animal 조회 (petId가 null이면 null을 할당, 아니면 실제 Animal 객체 가져오기)
        Animal pet = null;
        if (requestDto.getPetId() != null) {
            pet = animalRepository.findById(requestDto.getPetId())
                    .orElseThrow(() -> new IllegalArgumentException("Pet not found"));
            pet.markAsLost();
        }

        AnimalType animalType = requestDto.getAnimalType() != null
                ? AnimalType.valueOf(requestDto.getAnimalType())
                : null;

// LostFoundPost 객체 생성 (pet이 null일 수 있음)
        LostFoundPost lostFoundPost = new LostFoundPost(requestDto, author, pet, animalType);
        // LostFoundPost 저장
        lostFoundPostRepository.save(lostFoundPost);

        if (pet != null) {
            Image petImage = imageRepository.findFirstByPath(pet.getImageUrl());
            if (petImage != null) {
                petImage.setFoundId(lostFoundPost.getId());
                petImage.setStatus(lostFoundPost.getStatus());
                petImage.setAnimalType(lostFoundPost.getAnimalType());
                imageRepository.save(petImage);

                imageHandlerService.registerImageAndSendEvent(
                        petImage.getPath(),
                        petImage.getAnimalId(),
                        petImage.getFoundId(),
                        petImage.getStatus(),
                        petImage.getAnimalType()
                );
            }
        }
        // 이미지를 업로드하지 않아도 오류가 발생하지 않도록 처리
        if (images != null && !images.isEmpty()) {
            return getSavedImages(images, lostFoundPost);  // 이미지가 있을 경우만 저장
        } else {
            // 이미지가 없으면 기본 응답 객체 반환
            return new LostFoundPostResponseDto(lostFoundPost);  // 이미지가 없어도 정상적으로 응답
        }
    }

    @Transactional
    public LostFoundPostResponseDto updateLostFoundPost(Long postId, LostFoundPostResponseDto requestDto, List<MultipartFile> images, Member author) {

        LostFoundPost lostFoundPost = lostFoundPostRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        if (!lostFoundPost.getAuthor().equals(author)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_ACCESS);
        }

        // 요청한 내용으로 게시글 수정
        if (requestDto.getContent() != null) lostFoundPost.setContent(requestDto.getContent());
        if (requestDto.getLatitude() != null) lostFoundPost.setLatitude(requestDto.getLatitude());
        if (requestDto.getLongitude() != null) lostFoundPost.setLongitude(requestDto.getLongitude());
        if (requestDto.getLocation() != null) lostFoundPost.setLocation(requestDto.getLocation());
        if (requestDto.getFindTime() != null) lostFoundPost.setFindTime(requestDto.getFindTime());
        if (requestDto.getLostTime() != null) lostFoundPost.setLostTime(requestDto.getLostTime());
        // status가 null이 아니면 PostStatus enum으로 변환
        if (requestDto.getStatus() != null) {
            PostStatus newStatus = PostStatus.fromString(requestDto.getStatus());
            lostFoundPost.setStatus(newStatus);

            // 해당 게시글과 연결된 이미지들의 상태도 업데이트
            List<Image> relatedImages = imageRepository.findAllByFoundId(postId);
            for (Image image : relatedImages) {
                image.updateStatus(newStatus);
                imageRepository.save(image);
            }
        }

        return getSavedImages(images, lostFoundPost);
    }
    @Transactional
    public void deleteLostFoundPost(Long postId, Member author) {
        // 로깅을 위한 Logger 객체 생성
        Logger logger = LoggerFactory.getLogger(getClass());

        // 게시글 조회
        LostFoundPost lostFoundPost = lostFoundPostRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        // 게시글 작성자 확인
        if (!lostFoundPost.getAuthor().equals(author)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_ACCESS);
        }

        // 이미지 삭제 로직 제외 (아래 코드를 삭제하거나 주석 처리)
        // 이미지를 삭제하지 않으므로 이 부분을 생략합니다.

        // 게시글 삭제 (이미지 삭제와 상관없이 진행)
        try {
            lostFoundPostRepository.deleteById(postId);
            logger.info("Post with ID {} deleted successfully", postId);
        } catch (Exception e) {
            // 게시글 삭제 오류 발생 시
            logger.error("Error deleting post with ID {}: {}", postId, e.getMessage());
        }
    }



    @Transactional(readOnly = true)
    public Page<LostFoundPostResponseDto> getAllLostFoundPosts(Pageable pageable) {
        Page<LostFoundPost> findPosts = lostFoundPostRepository.findAll(pageable);
        return findPosts.map(LostFoundPostResponseDto::from);
    }

    @Transactional(readOnly = true)
    public LostFoundPostResponseDto getLostFoundPostById(Long postId) {
        LostFoundPost lostFoundPost = lostFoundPostRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        return LostFoundPostResponseDto.from(lostFoundPost);
    }

    @Transactional(readOnly = true)
    public List<LostFoundPostResponseDto> getLostFoundPostsWithinRadius(double latitude, double longitude, double radius) {
        List<LostFoundPost> lostFoundPosts = lostFoundPostRepository.findPostsWithinRadius(latitude, longitude, radius);
        return lostFoundPosts.stream()
                .map(LostFoundPostResponseDto::from)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<LostFoundPostResponseDto> getPostsByStatus(PostStatus postStatus, Pageable pageable) {
        Page<LostFoundPost> posts = lostFoundPostRepository.findByStatus(postStatus, pageable);
        return posts.map(LostFoundPostResponseDto::from);
    }

    // 내가 작성한 게시글 리스트 불러오기
    @Transactional(readOnly = true)
    public Page<MyPostsResponse> myPosts(Member member, Pageable pageable) {
        Page<LostFoundPost> postsPage = lostFoundPostRepository.findByAuthorId(member.getId(), pageable);

        return postsPage.map(post -> new MyPostsResponse(
                post.getId(),
                post.getContent(),
                post.getStatus(),
                post.getFindTime(),
                post.getLostTime(),
                post.getCreatedAt().toString()
        ));
    }

    // 마이페이지 나의 신고글 리스트 불러오기
    @Transactional(readOnly = true)
    public Page<MyPostsResponse> myReportPosts(Member member, Pageable pageable) {
        Page<LostFoundPost> reportPosts = lostFoundPostRepository.findByAuthorIdAndStatusIn(
                member.getId(),
                List.of(PostStatus.FINDING, PostStatus.FOUND),
                pageable
        );

        return reportPosts.map(post -> new MyPostsResponse(
                post.getId(),
                post.getContent(),
                post.getStatus(),
                post.getFindTime(),
                post.getLostTime(),
                post.getCreatedAt().toString()
        ));
    }

    // 마이페이지 나의 제보글 리스트 불러오기
    @Transactional(readOnly = true)
    public Page<MyPostsResponse> myWitnessPosts(Member member, Pageable pageable) {
        Page<LostFoundPost> witnessPosts = lostFoundPostRepository.findByAuthorIdAndStatusIn(
                member.getId(),
                List.of(PostStatus.SHELTER, PostStatus.FOSTERING, PostStatus.SIGHTED),
                pageable
        );

        return witnessPosts.map(post -> new MyPostsResponse(
                post.getId(),
                post.getContent(),
                post.getStatus(),
                post.getFindTime(),
                post.getLostTime(),
                post.getCreatedAt().toString()
        ));
    }

    @NotNull
    private LostFoundPostResponseDto getSavedImages(List<MultipartFile> images, LostFoundPost lostFoundPost) {
        if (images != null && !images.isEmpty()) {
            List<Image> savedImages = imageHandlerService.uploadAndRegisterImages(
                    images,
                    FOLDER_PATH,
                    null,
                    lostFoundPost.getId(),
                    lostFoundPost.getStatus(),
                    lostFoundPost.getAnimalType()
            );

            for (Image image : savedImages) {
                lostFoundPost.addImage(image);
            }
        }
        return LostFoundPostResponseDto.from(lostFoundPost);
    }

    @Transactional(readOnly = true)
    public Page<LostFoundPostResponseDto> getRewardPosts(PostStatus postStatus, Pageable pageable) {
        Page<LostFoundPost> posts = lostFoundPostRepository.findByStatusAndRewardNotNull(postStatus, pageable);
        return posts.map(LostFoundPostResponseDto::from);
    }
}
