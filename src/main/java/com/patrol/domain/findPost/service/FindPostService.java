package com.patrol.domain.findPost.service;

import com.patrol.api.findPost.dto.FindPostRequestDto;
import com.patrol.api.findPost.dto.FindPostResponseDto;
import com.patrol.domain.findPost.entity.FindPost;
import com.patrol.domain.findPost.repository.FindPostRepository;
import com.patrol.domain.lostpost.entity.LostPost;
import com.patrol.domain.lostpost.repository.LostPostRepository;
import com.patrol.domain.image.entity.Image;
import com.patrol.domain.image.repository.ImageRepository;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FindPostService {
    private final FileStorageHandler fileStorageHandler;
    private final FindPostRepository findPostRepository;
    private final LostPostRepository lostPostRepository;  // Inject LostPostRepository
    private final ImageRepository imageRepository;
    private final NcpObjectStorageService ncpObjectStorageService;
    private final MemberRepository memberRepository;

    @Transactional
    public FindPostResponseDto createFindPost(FindPostRequestDto requestDto, Long lostPostId,  Member author, List<MultipartFile> images) {

        // LostPost 객체를 lostPostId로 조회
        LostPost lostPost = null;
        if (lostPostId != null) {
            lostPost = lostPostRepository.findById(lostPostId)
                    .orElseThrow(() -> new RuntimeException("실종 게시글을 찾을 수 없습니다."));
        }

        // FindPost 객체 생성 시 lostPost를 전달
        FindPost findPost = new FindPost(requestDto, lostPost, author);
        findPostRepository.save(findPost);  // Id를 생성하기 위해 먼저 저장


        // 여러 개 이미지 업로드
        if (images != null && !images.isEmpty()) {
            List<String> uploadedPaths = new ArrayList<>();

            try {
                for (MultipartFile image : images) {
                    FileUploadResult uploadResult = fileStorageHandler.handleFileUpload(
                            FileUploadRequest.builder()
                                    .folderPath("findpost/")
                                    .file(image)
                                    .build()
                    );

                    if (uploadResult != null) {
                        uploadedPaths.add(uploadResult.getFileName());

                        Image imageEntity = Image.builder()
                                .path(uploadResult.getFileName())
                                .foundId(findPost.getId())
                                .build();

                        findPost.addImage(imageEntity);
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

        return FindPostResponseDto.from(findPost);
    }

    @Transactional
    public FindPostResponseDto updateFindPost(Long postId, Long lostPostId, FindPostRequestDto requestDto, List<MultipartFile> images,Member author) {
        FindPost findPost = findPostRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));


        // 로그인한 사용자(author)가 게시글 작성자와 일치하는지 확인
        if (!findPost.getAuthor().equals(author)) {
            throw new RuntimeException("게시글 수정 권한이 없습니다.");
        }

        findPost.setTitle(requestDto.getTitle());
        findPost.setContent(requestDto.getContent());
        findPost.setFindTime(requestDto.getFindTime());
        findPost.setTags(String.join("#", requestDto.getTags()));

        // LostPost 연계
        if (lostPostId != null) {
            LostPost lostPost = lostPostRepository.findById(lostPostId)
                    .orElseThrow(() -> new RuntimeException("실종 게시글을 찾을 수 없습니다."));
            findPost.setLostPost(lostPost);
        }

        // 새로운 이미지 업로드
        if (images != null && !images.isEmpty()) {
            List<String> uploadedPaths = new ArrayList<>();

            try {
                for (MultipartFile image : images) {
                    FileUploadResult uploadResult = fileStorageHandler.handleFileUpload(
                            FileUploadRequest.builder()
                                    .folderPath("findpost/")
                                    .file(image)
                                    .build()
                    );

                    if (uploadResult != null) {
                        uploadedPaths.add(uploadResult.getFileName());

                        Image imageEntity = Image.builder()
                                .path(uploadResult.getFileName())
                                .foundId(findPost.getId())
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

        return FindPostResponseDto.from(findPost);
    }



    @Transactional
    public FindPostResponseDto createStandaloneFindPost(FindPostRequestDto requestDto, Member author,List<MultipartFile> images) {


        // 연계 없는 제보 게시글 생성
        FindPost findPost = new FindPost(requestDto, null,author);  // 연계된 실종 게시글 ID 없이 생성
        findPostRepository.save(findPost);  // Id를 생성하기 위해 먼저 저장
        // 이미지 업로드 처리
        if (images != null && !images.isEmpty()) {
            List<String> uploadedPaths = new ArrayList<>();

            try {
                for (MultipartFile image : images) {
                    FileUploadResult uploadResult = fileStorageHandler.handleFileUpload(
                            FileUploadRequest.builder()
                                    .folderPath("findpost/")
                                    .file(image)
                                    .build()
                    );

                    if (uploadResult != null) {
                        uploadedPaths.add(uploadResult.getFileName());

                        Image imageEntity = Image.builder()
                                .path(uploadResult.getFileName())
                                .foundId(findPost.getId())
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

        return FindPostResponseDto.from(findPost);
    }

    @Transactional
    public FindPostResponseDto updateStandaloneFindPost(Long postId, FindPostRequestDto requestDto, List<MultipartFile> images,Member author) {
        FindPost findPost = findPostRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));


        // 로그인한 사용자(author)가 게시글 작성자와 일치하는지 확인
        if (!findPost.getAuthor().equals(author)) {
            throw new RuntimeException("게시글 수정 권한이 없습니다.");
        }

        findPost.setTitle(requestDto.getTitle());
        findPost.setContent(requestDto.getContent());
        findPost.setFindTime(requestDto.getFindTime());
        findPost.setTags(String.join("#", requestDto.getTags()));

        // 이미지 업로드 처리
        if (images != null && !images.isEmpty()) {
            List<String> uploadedPaths = new ArrayList<>();

            try {
                for (MultipartFile image : images) {
                    FileUploadResult uploadResult = fileStorageHandler.handleFileUpload(
                            FileUploadRequest.builder()
                                    .folderPath("findpost/")
                                    .file(image)
                                    .build()
                    );

                    if (uploadResult != null) {
                        uploadedPaths.add(uploadResult.getFileName());

                        Image imageEntity = Image.builder()
                                .path(uploadResult.getFileName())
                                .foundId(findPost.getId())
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

        return FindPostResponseDto.from(findPost);
    }

    @Transactional
    public void deleteFindPost(Long postId,Member author) {
        FindPost findPost = findPostRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));

        // 로그인한 사용자(author)가 게시글 작성자와 일치하는지 확인
        if (!findPost.getAuthor().equals(author)) {
            throw new RuntimeException("게시글 수정 권한이 없습니다.");
        }
        // 이미지 조회 및 삭제
        List<Image> images = imageRepository.findAllByFoundId(postId);
        images.forEach(image -> {
            ncpObjectStorageService.delete(image.getPath());
            imageRepository.delete(image);
        });

        findPostRepository.deleteById(postId);
    }

    @Transactional
    public void deleteStandaloneFindPost(Long postId,Member author) {
        FindPost findPost = findPostRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));

        // 로그인한 사용자(author)가 게시글 작성자와 일치하는지 확인
        if (!findPost.getAuthor().equals(author)) {
            throw new RuntimeException("게시글 삭제 권한이 없습니다.");
        }


        // 이미지 조회 및 삭제
        List<Image> images = imageRepository.findAllByFoundId(postId);
        images.forEach(image -> {
            ncpObjectStorageService.delete(image.getPath());
            imageRepository.delete(image);
        });

        findPostRepository.deleteById(postId);
    }

    @Transactional(readOnly = true)
    public Page<FindPostResponseDto> getAllFindPosts(Pageable pageable) {
        Page<FindPost> findPosts = findPostRepository.findByLostPostIsNotNull(pageable) ;
        return findPosts.map(FindPostResponseDto::from);
    }

    // 신고글과 엮이지 않은 제보글 조회
    @Transactional(readOnly = true)
    public Page<FindPostResponseDto> getAllStandaloneFindPosts(Pageable pageable) {
        return findPostRepository.findByLostPostIsNull(pageable) // ✅ 수정된 부분
                .map(FindPostResponseDto::from);
    }

    // 신고글과 엮이지 않은 게시글 하나만 조회
    @Transactional(readOnly = true)
    public FindPostResponseDto getStandaloneFindPostById(Long postId) {
        FindPost findPost = findPostRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));

        if (findPost.getLostPost() != null) { // 🚨 신고글과 연계된 게시글이라면 예외 처리
            throw new RuntimeException("이 게시글은 독립적인 제보 게시글이 아닙니다.");
        }

        return FindPostResponseDto.from(findPost);
    }

    @Transactional(readOnly = true)
    public FindPostResponseDto getFindPostById(Long postId){
        FindPost findPost = findPostRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));

        if (findPost.getLostPost() == null) { // 🚨 독립적인 제보글이라면 예외 처리
            throw new RuntimeException("이 게시글은 신고글과 연계되지 않은 독립적인 제보 게시글입니다.");
        }

        return FindPostResponseDto.from(findPost);
    }
}


