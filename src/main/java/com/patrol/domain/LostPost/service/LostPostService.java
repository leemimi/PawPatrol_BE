package com.patrol.domain.LostPost.service;

import com.patrol.api.LostFound.dto.FindPostResponseDto;
import com.patrol.api.LostPost.dto.LostPostRequestDto;
import com.patrol.api.LostPost.dto.LostPostResponseDto;
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
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LostPostService {
    private final LostPostRepository lostPostRepository;
    private final FindPostRepository findPostRepository;

    @Transactional
    public LostPostResponseDto createLostPost(LostPostRequestDto requestDto, MultipartFile image) {
        LostPost lostPost = new LostPost(requestDto);
        lostPostRepository.save(lostPost);
        return new LostPostResponseDto(lostPost);
    }

    @Transactional
    public LostPostResponseDto updateLostPost(Long postId, LostPostRequestDto requestDto) {
        LostPost lostPost = lostPostRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));
        lostPost.update(requestDto);
        return new LostPostResponseDto(lostPost);
    }

    @Transactional
    public void deleteLostPost(Long postId) {
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
        Page<FindPost> findPosts = findPostRepository.findByLostPost_LostId(lostId, pageable);
        return findPosts.map(post -> new FindPostResponseDto(
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
                post.getBirthDate(),
                post.getBreed(),
                post.getName(),
                post.getCharacteristics(),
                post.getSize(),
                post.getGender()
        ));
    }
}
