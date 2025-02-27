package com.patrol.domain.lostFoundPost.service;

import com.patrol.api.lostFoundPost.dto.LostFoundPostRequestDto;
import com.patrol.api.lostFoundPost.dto.LostFoundPostResponseDto;
import com.patrol.api.member.auth.dto.MyPostsResponse;
import com.patrol.domain.animal.entity.Animal;
import com.patrol.domain.animal.repository.AnimalRepository;
import com.patrol.domain.lostFoundPost.entity.AnimalType;
import com.patrol.domain.lostFoundPost.entity.LostFoundPost;
import com.patrol.domain.lostFoundPost.entity.PostStatus;
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

        // Animal 조회 (petId가 null이면 null을 할당, 아니면 실제 Animal 객체 가져오기)
        Animal pet = null;
        if (requestDto.getPetId() != null) {
            pet = animalRepository.findById(requestDto.getPetId())
                    .orElseThrow(() -> new IllegalArgumentException("Pet not found"));
        }

        AnimalType animalType = requestDto.getAnimalType() != null
                ? AnimalType.valueOf(requestDto.getAnimalType())
                : null;

        // LostFoundPost 객체 생성
        LostFoundPost lostFoundPost = new LostFoundPost(requestDto, author, pet, animalType);
        System.out.println("Received petId: " + requestDto.getPetId());
        System.out.println("📌 LostFoundPost created with pet: " + lostFoundPost.getPet());


        lostFoundPostRepository.save(lostFoundPost);
        System.out.println("💾 LostFoundPost saved with pet: " + lostFoundPost.getPet());

        // 이미지 처리
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
        System.out.println("Received petId: " + requestDto.getPetId());

        return LostFoundPostResponseDto.from(lostFoundPost);
    }





    @Transactional
    public LostFoundPostResponseDto updateLostFoundPost(Long postId, LostFoundPostResponseDto requestDto, List<MultipartFile> images, Member author) {
        LostFoundPost lostFoundPost = lostFoundPostRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));

        if (!lostFoundPost.getAuthor().equals(author)) {
            throw new RuntimeException("게시글 수정 권한이 없습니다.");
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
            lostFoundPost.setStatus(PostStatus.fromString(requestDto.getStatus()));  // fromString 사용
        }


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

        // 로그인한 사용자(author)가 게시글 작성자와 일치하는지 확인
        if (!lostFoundPost.getAuthor().equals(author)) {
            throw new RuntimeException("게시글 수정 권한이 없습니다.");
        }

        // 이미지 조회 및 삭제
        List<Image> images = imageRepository.findAllByFoundId(postId);
        images.forEach(image -> {
            System.out.println("Deleting file: " + image.getPath());  // 경로 확인용 로그
            try {
                // 파일 삭제 시, 예외 발생 시 무시하고 계속 진행
                try {
                    ncpObjectStorageService.delete(image.getPath());
                    imageRepository.delete(image);
                    System.out.println("File deleted successfully: " + image.getPath());
                } catch (Exception e) {
                    System.err.println("Error deleting file (ignored): " + image.getPath());  // 에러 로그
                    // 파일이 없거나 삭제가 실패해도 예외를 던지지 않고 무시
                }
            } catch (Exception e) {
                System.err.println("Unexpected error: " + e.getMessage());
            }
        });

        // 게시글 삭제
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

    @Transactional(readOnly = true)
    public Page<LostFoundPostResponseDto> getPostsByStatus(PostStatus postStatus, Pageable pageable) {
        Page<LostFoundPost> posts = lostFoundPostRepository.findByStatus(postStatus, pageable);
        return posts.map(LostFoundPostResponseDto::from);
    }

    // 내가 작성한 게시글 리스트 불러오기
    @Transactional
    public Page<MyPostsResponse> myPosts(Member member, Pageable pageable) {
        Page<LostFoundPost> postsPage = lostFoundPostRepository.findByAuthorId(member.getId(), pageable);

        return postsPage.map(post -> new MyPostsResponse(
                post.getContent(),
                post.getStatus(),
                post.getFindTime(),
                post.getLostTime(),
                post.getCreatedAt().toString()
        ));
    }
    // 마이페이지 나의 신고글 리스트 불러오기
    @Transactional
    public Page<MyPostsResponse> myReportPosts(Member member, Pageable pageable) {
        Page<LostFoundPost> reportPosts = lostFoundPostRepository.findByAuthorIdAndStatusIn(
                member.getId(),
                List.of(PostStatus.FINDING, PostStatus.FOUND),
                pageable
        );

        return reportPosts.map(post -> new MyPostsResponse(
                post.getContent(),
                post.getStatus(),
                post.getFindTime(),
                post.getLostTime(),
                post.getCreatedAt().toString()
        ));
    }
    // 마이페이지 나의 제보글 리스트 불러오기
    @Transactional
    public Page<MyPostsResponse> myWitnessPosts(Member member, Pageable pageable) {
        Page<LostFoundPost> witnessPosts = lostFoundPostRepository.findByAuthorIdAndStatusIn(
                member.getId(),
                List.of(PostStatus.SHELTER, PostStatus.FOSTERING, PostStatus.SIGHTED),
                pageable
        );

        return witnessPosts.map(post -> new MyPostsResponse(
                post.getContent(),
                post.getStatus(),
                post.getFindTime(),
                post.getLostTime(),
                post.getCreatedAt().toString()
        ));
    }
}
