package com.patrol.api.findPost.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.patrol.api.findPost.dto.FindPostRequestDto;
import com.patrol.api.findPost.dto.FindPostResponseDto;
import com.patrol.domain.findPost.service.FindPostService;
import com.patrol.domain.member.member.entity.Member;
import com.patrol.global.rsData.RsData;
import com.patrol.global.webMvc.LoginUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/lost-found")
@RequiredArgsConstructor
@Tag(name = "제보 게시글 API", description = "제보글")
public class FindPostController {

    private final FindPostService findPostService;
    private final ObjectMapper objectMapper;

    // 제보 게시글 등록
    @PostMapping
    @Operation(summary = "제보 게시글 등록")
    public RsData<FindPostResponseDto> createFindPost(
            @RequestParam("metadata") String metadataJson,
            @RequestParam(value = "lostPostId", required = false) Long lostPostId,
            @RequestParam(value = "images", required = false) List<MultipartFile> images,
            @LoginUser Member loginUser) {

        ObjectMapper objectMapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .registerModule(new JavaTimeModule());

        try {
            FindPostRequestDto requestDto = objectMapper.readValue(metadataJson, FindPostRequestDto.class);
            FindPostResponseDto responseDto = findPostService.createFindPost(requestDto, lostPostId,loginUser.getId(), images);
            return new RsData<>("200", "제보 게시글을 성공적으로 등록했습니다.", responseDto);
        } catch (JsonProcessingException e) {
            return new RsData<>("400", "잘못된 JSON 형식입니다.", null);
        }
    }

    // 신고글 연계 제보 게시글 수정
    @PutMapping("/{postId}")
    @Operation(summary = "신고글 연계 제보 게시글 수정")
    public RsData<FindPostResponseDto> updateFindPost(
            @PathVariable(name = "postId") Long postId,
            @RequestParam("metadata") String metadataJson,
            @RequestParam(value = "lostPostId", required = false) Long lostPostId,
            @RequestParam(value = "images", required = false) List<MultipartFile> images,
            @LoginUser Member loginUser) {
        try {
            FindPostRequestDto requestDto = objectMapper.readValue(metadataJson, FindPostRequestDto.class);
            FindPostResponseDto responseDto = findPostService.updateFindPost(postId, lostPostId, requestDto, images);
            return new RsData<>("200", "제보 게시글을 성공적으로 수정했습니다.", responseDto);
        } catch (JsonProcessingException e) {
            return new RsData<>("400", "잘못된 JSON 형식입니다.", null);
        }
    }

    // 신고글 연계 제보 게시글 삭제
    @DeleteMapping("/{postId}")
    @Operation(summary = "신고글 연계 제보 게시글 삭제")
    public RsData<Void> deleteFindPost(@PathVariable Long postId) {
        findPostService.deleteFindPost(postId);
        return new RsData<>("200", "제보 게시글을 성공적으로 삭제했습니다.");
    }

    // 모든 신고글 연계 제보 게시글 조회 (페이징 지원)
    @GetMapping("/find/map")
    @Operation(summary = "반경 내의 모든 신고글 연계 제보 게시글 조회 (페이징 지원)")
    public RsData<List<FindPostResponseDto>> getAllFindPosts(
            @RequestParam(name = "latitude") double latitude,
            @RequestParam(name = "longitude") double longitude,
            @RequestParam(name = "radius") double radius) {

        List<FindPostResponseDto> posts = findPostService.getFindPostsWithinRadius(latitude, longitude, radius);
        return new RsData<>("200", "반경 내의 제보 게시글을 성공적으로 호출했습니다.", posts);
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
            @RequestParam("metadata") String metadataJson,
            @RequestParam(value = "images", required = false) List<MultipartFile> images,
            @LoginUser Member loginUser) {
        try {
            FindPostRequestDto requestDto = objectMapper.readValue(metadataJson, FindPostRequestDto.class);
            FindPostResponseDto responseDto = findPostService.createStandaloneFindPost(requestDto, images, loginUser.getId());
            return new RsData<>("200", "독립적인 제보 게시글을 성공적으로 등록했습니다.", responseDto);
        } catch (JsonProcessingException e) {
            return new RsData<>("400", "잘못된 JSON 형식입니다.", null);
        }
    }

    // 독립적인 제보 게시글 수정
    @PutMapping("/find-standalone/{postId}")
    @Operation(summary = "독립적인 제보 게시글 수정")
    public RsData<FindPostResponseDto> updateStandaloneFindPost(
            @PathVariable(name = "postId") Long postId,
            @RequestParam("metadata") String metadataJson,
            @RequestParam(value = "images", required = false) List<MultipartFile> images,
            @LoginUser Member loginUser) {
        try {
            FindPostRequestDto requestDto = objectMapper.readValue(metadataJson, FindPostRequestDto.class);
            FindPostResponseDto responseDto = findPostService.updateStandaloneFindPost(postId, requestDto, images);
            return new RsData<>("200", "독립적인 제보 게시글을 성공적으로 수정했습니다.", responseDto);
        } catch (JsonProcessingException e) {
            return new RsData<>("400", "잘못된 JSON 형식입니다.", null);
        }
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
