package com.patrol.domain.LostFound.service;

import com.patrol.api.LostFound.dto.FindPostRequestDto;
import com.patrol.api.LostFound.dto.FindPostResponseDto;
import com.patrol.domain.LostFound.entity.FindPost;
import com.patrol.domain.LostFound.repository.FindPostRepository;
import com.patrol.domain.LostPost.entity.LostPost;
import com.patrol.domain.LostPost.repository.LostPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class FindPostService {
    private final FindPostRepository findPostRepository;
    private final LostPostRepository lostPostRepository;  // Inject LostPostRepository
    //private final S3Service s3Service;

    // 신고글 연계 제보 게시글 등록
    @Transactional
    public FindPostResponseDto createFindPost(FindPostRequestDto requestDto, Long lostPostId, MultipartFile image) {
        // LostPost 객체를 lostPostId로 조회
        LostPost lostPost = null;
        if (lostPostId != null) {
            lostPost = lostPostRepository.findById(lostPostId)
                    .orElseThrow(() -> new RuntimeException("실종 게시글을 찾을 수 없습니다."));
        }

        // FindPost 객체 생성 시 lostPost를 전달
        FindPost findPost = new FindPost(requestDto, lostPost);
        findPostRepository.save(findPost);

        // FindPostResponseDto 생성
        return new FindPostResponseDto(
                findPost.getFoundId(),
                findPost.getMemberId(),

                findPost.getTitle(),
                findPost.getContent(),
                findPost.getLatitude(),
                findPost.getLongitude(),
                findPost.getFindTime(),
                findPost.getTags(),
                findPost.getCreatedAt(),
                findPost.getModifiedAt(),
                findPost.getBirthDate(),        // 출생일
                findPost.getBreed(),           // 품종
                findPost.getName(),            // 이름
                findPost.getCharacteristics(), // 특징
                findPost.getSize(),            // 크기
                findPost.getGender()           // 성별
        );
    }

    // 신고글 연계 제보 게시글 수정
    @Transactional
    public FindPostResponseDto updateFindPost(Long postId, Long lostPostId, FindPostRequestDto requestDto) {
        FindPost findPost = findPostRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));

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

        findPost.setModifiedAt(LocalDateTime.now());

        return new FindPostResponseDto(
                findPost.getFoundId(),
                findPost.getMemberId(),

                findPost.getTitle(),
                findPost.getContent(),
                findPost.getLatitude(),
                findPost.getLongitude(),
                findPost.getFindTime(),
                findPost.getTags(),
                findPost.getCreatedAt(),
                findPost.getModifiedAt(),
                findPost.getBirthDate(),        // 출생일
                findPost.getBreed(),           // 품종
                findPost.getName(),            // 이름
                findPost.getCharacteristics(), // 특징
                findPost.getSize(),            // 크기
                findPost.getGender()           // 성별
        );
    }

    // 신고글 연계 제보 게시글 삭제
    @Transactional
    public void deleteFindPost(Long postId) {
        findPostRepository.deleteById(postId);
    }

    @Transactional(readOnly = true)
    public Page<FindPostResponseDto> getAllFindPosts(Pageable pageable) {
        return findPostRepository.findAll(pageable).map(post -> new FindPostResponseDto(
                post.getFoundId(), post.getMemberId(), post.getTitle(), post.getContent(), post.getLatitude(), post.getLongitude(), post.getFindTime(), post.getTags(), post.getCreatedAt(), post.getModifiedAt(),post.getBirthDate(),        // 출생일
                post.getBreed(),           // 품종
                post.getName(),            // 이름
                post.getCharacteristics(), // 특징
                post.getSize(),            // 크기
                post.getGender()           // 성별
        ));
    }

    // Create a new standalone find post
    @Transactional
    public FindPostResponseDto createStandaloneFindPost(FindPostRequestDto requestDto, MultipartFile image) {
        // 연계 없는 제보 게시글 생성
        FindPost findPost = new FindPost(requestDto, null);  // 연계된 실종 게시글 ID 없이 생성
        findPostRepository.save(findPost);

        return new FindPostResponseDto(
                findPost.getFoundId(),
                findPost.getMemberId(),
                findPost.getTitle(),
                findPost.getContent(),
                findPost.getLatitude(),
                findPost.getLongitude(),
                findPost.getFindTime(),
                findPost.getTags(),
                findPost.getCreatedAt(),
                findPost.getModifiedAt(),
                findPost.getBirthDate(),        // 출생일
                findPost.getBreed(),           // 품종
                findPost.getName(),            // 이름
                findPost.getCharacteristics(), // 특징
                findPost.getSize(),            // 크기
                findPost.getGender()           // 성별
        );
    }

    // 연계 없는 제보 게시글 수정
    @Transactional
    public FindPostResponseDto updateStandaloneFindPost(Long postId, FindPostRequestDto requestDto) {
        FindPost findPost = findPostRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));

        // 연계 없는 제보 게시글 업데이트
        findPost.setTitle(requestDto.getTitle());
        findPost.setContent(requestDto.getContent());
        findPost.setFindTime(requestDto.getFindTime());
        findPost.setTags(String.join("#", requestDto.getTags()));
        findPost.setModifiedAt(LocalDateTime.now());

        // Save the updated post
        findPostRepository.save(findPost);

        return new FindPostResponseDto(
                findPost.getFoundId(),
                findPost.getMemberId(),
                findPost.getTitle(),
                findPost.getContent(),
                findPost.getLatitude(),
                findPost.getLongitude(),
                findPost.getFindTime(),
                findPost.getTags(),
                findPost.getCreatedAt(),
                findPost.getModifiedAt(),
                findPost.getBirthDate(),        // 출생일
                findPost.getBreed(),           // 품종
                findPost.getName(),            // 이름
                findPost.getCharacteristics(), // 특징
                findPost.getSize(),            // 크기
                findPost.getGender()           // 성별
        );
    }

    // Delete a standalone find post
    @Transactional
    public void deleteStandaloneFindPost(Long postId) {
        findPostRepository.deleteById(postId);
    }

    // Get all standalone find posts with pagination
    @Transactional(readOnly = true)
    public Page<FindPostResponseDto> getAllStandaloneFindPosts(Pageable pageable) {
        return findPostRepository.findAll(pageable).map(post -> new FindPostResponseDto(
                post.getFoundId(),
                post.getMemberId(),
                post.getTitle(),
                post.getContent(),
                post.getLatitude(),
                post.getLongitude(),
                post.getFindTime(),
                post.getTags(),
                post.getCreatedAt(),
                post.getModifiedAt(),
                post.getBirthDate(),        // 출생일
                post.getBreed(),           // 품종
                post.getName(),            // 이름
                post.getCharacteristics(), // 특징
                post.getSize(),            // 크기
                post.getGender()           // 성별

        ));
    }

    // Get a specific standalone find post by ID
    @Transactional(readOnly = true)
    public FindPostResponseDto getStandaloneFindPostById(Long postId) {
        FindPost findPost = findPostRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));

        return new FindPostResponseDto(
                findPost.getFoundId(),
                findPost.getMemberId(),

                findPost.getTitle(),
                findPost.getContent(),
                findPost.getLatitude(),
                findPost.getLongitude(),
                findPost.getFindTime(),
                findPost.getTags(),
                findPost.getCreatedAt(),
                findPost.getModifiedAt(),
                findPost.getBirthDate(),        // 출생일
                findPost.getBreed(),           // 품종
                findPost.getName(),            // 이름
                findPost.getCharacteristics(), // 특징
                findPost.getSize(),            // 크기
                findPost.getGender()           // 성별
        );
    }
}


