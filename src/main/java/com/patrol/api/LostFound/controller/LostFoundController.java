package com.patrol.api.LostFound.controller;

import com.patrol.api.LostFound.dto.FindPostRequestDto;
import com.patrol.api.LostFound.dto.FindPostResponseDto;
import com.patrol.domain.LostFound.service.FindPostService;
import com.patrol.global.rsData.RsData;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/lost-found")
@RequiredArgsConstructor
public class LostFoundController {

    private final FindPostService findPostService;

    // 제보 게시글 등록
    @PostMapping
    @Operation(summary = "제보 게시글 등록")
    public RsData<FindPostResponseDto> createFindPost(
            @RequestBody FindPostRequestDto requestDto,
            @RequestParam(value = "lostPostId") Long lostPostId,  // 실종 게시글의 postId를 받음
            @RequestParam(value = "image", required = false) MultipartFile image) {

        FindPostResponseDto responseDto = findPostService.createFindPost(requestDto, lostPostId, image);
        return new RsData<>("200", "제보 게시글을 성공적으로 등록했습니다.", responseDto);
    }

    // 신고글 연계 제보 게시글 수정
    @PutMapping("/{postId}")
    @Operation(summary = "신고글 연계 제보 게시글 수정")
    public RsData<FindPostResponseDto> updateFindPost(
            @PathVariable(name = "postId") Long postId,
            @RequestParam(value = "lostPostId") Long lostPostId, // 실종 게시글의 postId를 받음
            @RequestBody FindPostRequestDto requestDto) {

        FindPostResponseDto responseDto = findPostService.updateFindPost(postId, lostPostId, requestDto);
        return new RsData<>("200", "제보 게시글을 성공적으로 수정했습니다.", responseDto);
    }

    // 신고글 연계 제보 게시글 삭제
    @DeleteMapping("/{postId}")
    @Operation(summary = "신고글 연계 제보 게시글 삭제")
    public RsData<Void> deleteFindPost(@PathVariable Long postId) {
        findPostService.deleteFindPost(postId);
        return new RsData<>("200", "제보 게시글을 성공적으로 삭제했습니다.");
    }

    // 모든 신고글 연계 제보 게시글 조회 (페이징 지원)
    @GetMapping("/find")
    @Operation(summary = "모든 신고글 연계 제보 게시글 조회 (페이징 지원)")
    public RsData<Page<FindPostResponseDto>> getAllFindPosts(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<FindPostResponseDto> posts = findPostService.getAllFindPosts(pageable);
        return new RsData<>("200", "모든 제보 게시글을 성공적으로 호출했습니다.", posts);
    }

    // 독립적인 제보 게시글 등록
    @PostMapping("/find-standalone")
    @Operation(summary = "독립적인 제보 게시글 등록")
    public RsData<FindPostResponseDto> createStandaloneFindPost(
            @RequestBody FindPostRequestDto requestDto,
            @RequestPart(value = "image", required = false) MultipartFile image) {
        FindPostResponseDto responseDto = findPostService.createStandaloneFindPost(requestDto, image);
        return new RsData<>("200", "독립적인 제보 게시글을 성공적으로 등록했습니다.", responseDto);
    }

    // 독립적인 제보 게시글 수정
    @PutMapping("/find-standalone/{postId}")
    @Operation(summary = "독립적인 제보 게시글 수정")
    public RsData<FindPostResponseDto> updateStandaloneFindPost(
            @PathVariable(name = "postId") Long postId,
            @RequestBody FindPostRequestDto requestDto) {
        FindPostResponseDto responseDto = findPostService.updateStandaloneFindPost(postId, requestDto);
        return new RsData<>("200", "독립적인 제보 게시글을 성공적으로 수정했습니다.", responseDto);
    }

    // 독립적인 제보 게시글 삭제
    @DeleteMapping("/find-standalone/{postId}")
    @Operation(summary = "독립적인 제보 게시글 삭제")
    public RsData<Void> deleteStandaloneFindPost(@PathVariable(name = "postId") Long postId) {
        findPostService.deleteStandaloneFindPost(postId);
        return new RsData<>("200", "독립적인 제보 게시글을 성공적으로 삭제했습니다.");
    }

    // 모든 독립적인 제보 게시글 목록 조회
    @GetMapping("/find-standalone")
    @Operation(summary = "독립적인 제보 게시글 목록 조회")
    public RsData<Page<FindPostResponseDto>> getAllStandaloneFindPosts(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<FindPostResponseDto> posts = findPostService.getAllStandaloneFindPosts(pageable);
        return new RsData<>("200", "독립적인 제보 게시글 목록을 성공적으로 호출했습니다.", posts);
    }

    // 독립적인 제보 게시글 상세 조회
    @GetMapping("/find-standalone/{postId}")
    @Operation(summary = "독립적인 제보 게시글 상세 조회")
    public RsData<FindPostResponseDto> getStandaloneFindPostById(@PathVariable(name = "postId") Long postId) {
        FindPostResponseDto responseDto = findPostService.getStandaloneFindPostById(postId);
        return new RsData<>("200", "독립적인 제보 게시글을 성공적으로 조회했습니다.", responseDto);
    }
}
