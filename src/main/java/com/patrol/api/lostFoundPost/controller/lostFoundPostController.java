package com.patrol.api.lostFoundPost.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.patrol.api.lostFoundPost.dto.lostFoundPostRequestDto;
import com.patrol.api.lostFoundPost.dto.lostFoundPostResponseDto;
import com.patrol.domain.lostFoundPost.service.LostFoundPostService;
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
@RequestMapping("/api/v1/lost-foundposts")
@RequiredArgsConstructor
@Tag(name = "제보 게시글 API", description = "제보글")
public class lostFoundPostController {

    private final LostFoundPostService lostFoundPostService;
    private final ObjectMapper objectMapper;

    @PostMapping
    @Operation(summary = "제보 게시글 등록")
    public RsData<lostFoundPostResponseDto> createStandaloneFindPost(
            @RequestParam("metadata") String metadataJson,
            @RequestParam(value = "images") List<MultipartFile> images,
            @RequestParam(value = "petId", required = false) Long petId,
            @LoginUser Member loginUser) {
        try {
            lostFoundPostRequestDto requestDto = objectMapper.readValue(metadataJson, lostFoundPostRequestDto.class);
            lostFoundPostResponseDto responseDto = lostFoundPostService.createLostFoundPost(requestDto, petId, loginUser, images);
            return new RsData<>("200", "제보 게시글을 성공적으로 등록했습니다.", responseDto);
        } catch (JsonProcessingException e) {
            return new RsData<>("400", "잘못된 JSON 형식입니다.", null);
        }
    }

    @PutMapping("/{postId}")
    @Operation(summary = "제보 게시글 수정")
    public RsData<lostFoundPostResponseDto> updateStandaloneFindPost(
            @PathVariable(name = "postId") Long postId,
            @RequestParam("metadata") String metadataJson,
            @RequestParam(value = "images", required = false) List<MultipartFile> images,
            @LoginUser Member loginUser) {
        try {
            lostFoundPostRequestDto requestDto = objectMapper.readValue(metadataJson, lostFoundPostRequestDto.class);
            lostFoundPostResponseDto responseDto = lostFoundPostService.updateLostFoundPost(postId, requestDto, images,loginUser);
            return new RsData<>("200", "제보 게시글을 성공적으로 수정했습니다.", responseDto);
        } catch (JsonProcessingException e) {
            return new RsData<>("400", "잘못된 JSON 형식입니다.", null);
        }
    }

    @DeleteMapping("/{postId}")
    @Operation(summary = "제보 게시글 삭제")
    public RsData<Void> deleteFindPost(@PathVariable(name = "postId") Long postId,@LoginUser Member loginUser) {
        lostFoundPostService.deleteLostFoundPost(postId,loginUser);
        return new RsData<>("200", "제보 게시글을 성공적으로 삭제했습니다.");
    }

    @GetMapping("/map")
    @Operation(summary = "반경 내의 모든 신고글 연계 제보 게시글 조회 (페이징 지원)")
    public RsData<List<lostFoundPostResponseDto>> getAllFindPosts(
            @RequestParam(name = "latitude") double latitude,
            @RequestParam(name = "longitude") double longitude,
            @RequestParam(name = "radius") double radius) {

        List<lostFoundPostResponseDto> posts = lostFoundPostService.getLostFoundPostsWithinRadius(latitude, longitude, radius);
        return new RsData<>("200", "반경 내의 제보 게시글을 성공적으로 호출했습니다.", posts);
    }

    @GetMapping
    @Operation(summary = "제보 게시글 목록 조회")
    public RsData<Page<lostFoundPostResponseDto>> getAllFindPosts(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<lostFoundPostResponseDto> posts = lostFoundPostService.getAllLostFoundPosts(pageable);
        return new RsData<>("200", "제보 게시글 목록을 성공적으로 호출했습니다.", posts);
    }

    @GetMapping("/{postId}")
    @Operation(summary = "제보 게시글 상세 조회")
    public RsData<lostFoundPostResponseDto> getFindPostById(@PathVariable(name = "postId") Long postId) {
        lostFoundPostResponseDto responseDto = lostFoundPostService.getLostFoundPostById(postId);
        return new RsData<>("200", "제보 게시글을 성공적으로 조회했습니다.", responseDto);
    }

}
