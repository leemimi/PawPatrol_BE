package com.patrol.domain.lostPost.service;

import com.patrol.api.findPost.dto.FindPostResponseDto;
import com.patrol.api.LostPost.dto.LostPostRequestDto;
import com.patrol.api.LostPost.dto.LostPostResponseDto;
import com.patrol.domain.findPost.entity.FindPost;
import com.patrol.domain.findPost.repository.FindPostRepository;
import com.patrol.domain.lostPost.entity.LostPost;
import com.patrol.domain.lostPost.repository.LostPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
        Page<FindPost> findPosts = findPostRepository.findByLostPost_Id(lostId, pageable);
        return findPosts.map(post -> new FindPostResponseDto(post));
    }

    @Transactional(readOnly=true)
    public List<LostPostResponseDto> getLostPostsWithinRadius(double latitude, double longitude, double radius) {
        // 위도/경도 기반으로 반경 내의 게시물 조회
        List<LostPost> lostPosts = lostPostRepository.lostPostsWithinRadius(latitude, longitude, radius);
        return lostPosts.stream()
                .map(LostPostResponseDto::from)
                .collect(Collectors.toList());
    }
}
