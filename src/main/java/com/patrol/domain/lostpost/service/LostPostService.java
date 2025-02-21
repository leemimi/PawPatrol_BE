package com.patrol.domain.lostpost.service;

import com.patrol.api.findPost.dto.FindPostRequestDto;
import com.patrol.api.findPost.dto.FindPostResponseDto;
import com.patrol.api.lostpost.dto.LostPostRequestDto;
import com.patrol.api.lostpost.dto.LostPostResponseDto;
import com.patrol.domain.findPost.entity.FindPost;
import com.patrol.domain.findPost.repository.FindPostRepository;
import com.patrol.domain.image.entity.Image;
import com.patrol.domain.image.repository.ImageRepository;
import com.patrol.domain.lostpost.entity.LostPost;
import com.patrol.domain.lostpost.repository.LostPostRepository;
import com.patrol.domain.member.member.entity.Member;
import com.patrol.domain.member.member.repository.MemberRepository;
import com.patrol.global.error.ErrorCode;
import com.patrol.global.exception.CustomException;
import com.patrol.global.storage.FileStorageHandler;
import com.patrol.global.storage.FileUploadRequest;
import com.patrol.global.storage.FileUploadResult;
import com.patrol.global.storage.NcpObjectStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LostPostService {
    private final LostPostRepository lostPostRepository;
    private final FindPostRepository findPostRepository;
    private final FileStorageHandler fileStorageHandler;
  // Inject LostPostRepository
    private final ImageRepository imageRepository;
    private final NcpObjectStorageService ncpObjectStorageService;
    private final MemberRepository memberRepository;

    @Transactional
    public LostPostResponseDto createLostPost(LostPostRequestDto requestDto, List<MultipartFile> images, Member author) {
        // 실종 게시글 조회 또는 새로 생성
        LostPost lostPost;

        lostPost = new LostPost(requestDto,author);  // 새 게시글 생성


        lostPostRepository.save(lostPost);  // 저장하여 ID 생성

        // 여러 개 이미지 업로드
        if (images != null && !images.isEmpty()) {
            List<String> uploadedPaths = new ArrayList<>();

            try {
                for (MultipartFile image : images) {
                    FileUploadResult uploadResult = fileStorageHandler.handleFileUpload(
                            FileUploadRequest.builder()
                                    .folderPath("lostpost/")
                                    .file(image)
                                    .build()
                    );

                    if (uploadResult != null) {
                        uploadedPaths.add(uploadResult.getFileName());

                        Image imageEntity = Image.builder()
                                .path(uploadResult.getFileName())
                                .lostId(lostPost.getId())
                                .build();

                        lostPost.addImage(imageEntity);
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

        // 최종적으로 생성된 LostPost 반환
        return LostPostResponseDto.from(lostPost);
    }



    @Transactional
    public LostPostResponseDto updateLostPost(Long postId, LostPostRequestDto requestDto,  List<MultipartFile> images, Member loginUser) {
        LostPost lostPost = lostPostRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));


        // 로그인한 사용자(author)가 게시글 작성자와 일치하는지 확인
        if (!lostPost.getAuthor().equals(loginUser)) {
            throw new RuntimeException("게시글 수정 권한이 없습니다.");
        }

        lostPost.setTitle(requestDto.getTitle());
        lostPost.setContent(requestDto.getContent());
        lostPost.setLostTime(requestDto.getLostTime());
        lostPost.setTags(String.join("#", requestDto.getTags()));


        // 새로운 이미지 업로드
        if (images != null && !images.isEmpty()) {
            List<String> uploadedPaths = new ArrayList<>();

            try {
                for (MultipartFile image : images) {
                    FileUploadResult uploadResult = fileStorageHandler.handleFileUpload(
                            FileUploadRequest.builder()
                                    .folderPath("lostpost/")
                                    .file(image)
                                    .build()
                    );

                    if (uploadResult != null) {
                        uploadedPaths.add(uploadResult.getFileName());

                        Image imageEntity = Image.builder()
                                .path(uploadResult.getFileName())
                                .foundId(lostPost.getId())
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

        return LostPostResponseDto.from(lostPost);
    }


    @Transactional
    public void deleteLostPost(Long postId,Member loginUser) {
        LostPost lostPost = lostPostRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));

        // 삭제하려는 게시글의 작성자와 로그인한 사용자가 일치하는지 확인
        if (!lostPost.getAuthor().equals(loginUser)) {
            throw new RuntimeException("본인이 작성한 게시글만 삭제할 수 있습니다.");
        }
        lostPostRepository.deleteById(postId);
    }

    @Transactional(readOnly = true)
    public Page<LostPostResponseDto> getAllLostPosts(Pageable pageable) {
        return lostPostRepository.findAll(pageable).map(LostPostResponseDto::new);
    }

    @Transactional(readOnly = true)
    public LostPostResponseDto getLostPostById(Long postId) {
        LostPost lostPost = lostPostRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));
        return new LostPostResponseDto(lostPost);
    }

    // 특정 실종 신고글에 연결된 제보글 목록 조회
    @Transactional(readOnly = true)
    public Page<FindPostResponseDto> getFindPostsByLostId(Long lostId, Pageable pageable) {
        Page<FindPost> findPosts = findPostRepository.findByLostPost_Id(lostId, pageable);
        return findPosts.map(FindPostResponseDto::from);  // 기존 DTO 변환 메서드 사용
    }
}
